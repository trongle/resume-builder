export const login = async (userName: string, password: string) => {
    const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({userName, password})
    })

    if (!response.ok) {
        const error = await response.json()
        throw new Error(JSON.stringify(error))
    }

    const {token, user} = await response.json()

    window.localStorage.setItem('token', token as string)
    window.localStorage.setItem('user', user as string)
}

export const logout = () => {
    window.localStorage.removeItem('token')
    window.localStorage.removeItem('user')
}