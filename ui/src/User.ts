export interface User {
    id?: number;
    userName: string;
    fullName: string;
    email: string;
    isAdmin: boolean;
}

export interface CreateNewUser extends User {
    password: string;
}

export const addUser = async (user: CreateNewUser) => {
    const response = await fetch('http://localhost:8080/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })

    if (!response.ok) {
        const error = await response.json()
        throw new Error(JSON.stringify(error))
    }
}

export const getUserFromLocalStorage = (): User => {
    const userData = window.localStorage.getItem('user')

    if (!userData) {
        throw new Error("Unauthorized")
    }
    return JSON.parse(userData)
}

export const fetchUsers = async (searchTerm: string | null = null, page = 1, sorting: {column: string, by: string} | null = null): Promise<{users: User[], lastPage: number }> => {
    let queries = ""
    if (searchTerm) {
        queries += `searchTerm=${searchTerm}&`
    }
    if (sorting) {
        queries += `sortColumn=${sorting.column}&sortBy=${sorting.by}&`
    }
    queries += `page=${page}`

    const response = await fetch(`http://localhost:8080/users?${queries}`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${window.localStorage.getItem('token')}`
        },
    })

    if (!response.ok && response.status === 401) {
        throw new Error("Unauthorized")
    }

    const data = await response.json()
    return {
        users: JSON.parse(data.users),
        lastPage: parseInt(data.lastPage)
    }
}