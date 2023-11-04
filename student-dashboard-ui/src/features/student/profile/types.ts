export interface Application {
    id: number
    universityName: string
    universityCourse: string
}

export interface SubmitApplicationModel extends Omit<Application, "id"> {
}


export interface ApplicationsResponse {
    total: number
    items: Application[]
}
