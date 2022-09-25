export interface Cv {
    id?: string | undefined;
    name: string;
    email: string;
    mobile: string;
    github: string;
    linkedln: string;
    description: string;
    jobTitle: string;
    company: string;
    period: string;
    jobDescription: string;
    university: string;
    faculty: string;
    gpa: string;
    skills: string;
}

export interface CreateNewCv extends Cv {
}

export const addCv = async (user: CreateNewCv) => {
    const response = await fetch('http://localhost:8080/cvs', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${window.localStorage.getItem('token')}`
        },
        body: JSON.stringify(user)
    })

    if (!response.ok) {
        const error = await response.json()
        throw new Error(JSON.stringify(error))
    }
}

export const fetchCvs = async (): Promise<Cv[]> => {
    const response = await fetch(`http://localhost:8080/cvs`, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${window.localStorage.getItem('token')}`
        },
    })

    if (!response.ok && response.status === 401) {
        throw new Error("Unauthorized")
    }

    return await response.json()
}

export const deleteCv = async (id: number) => {
    const response = await fetch(`http://localhost:8080/cvs/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${window.localStorage.getItem('token')}`
        },
    })

    if (!response.ok && response.status === 401) {
        throw new Error("Unauthorized")
    }
}