import { Button, TextField } from "@mui/material"
import { ChangeEvent, useState } from "react"
import styles from "./Profile.module.css"
import { useSubmitApplicationMutation } from "./profileAPI"

interface Props {
    onSubmit: () => void
}
export default function SubmitApplication(props: Props) {
    const { onSubmit } = props

    const [name, setName] = useState('')
    const [course, setCourse] = useState('')

    const [submitApplication, { isSuccess, isError, error }] = useSubmitApplicationMutation()
    const handleSubmit = () => {
        submitApplication({
            universityName: name,
            universityCourse: course,
        }).then(() => {
            onSubmit()
        })
    }

    const handleNameChange = (event: ChangeEvent<HTMLInputElement>) => setName(event.target.value)
    const handleCourseChange = (event: ChangeEvent<HTMLInputElement>) => setCourse(event.target.value)


    return (<div className={styles.formParent}>
        <div className={styles.form}>
            <TextField sx={{ m: 2 }} label="University name" variant="outlined" onChange={handleNameChange} />
            <TextField sx={{ m: 2 }} label="University course" variant="outlined" onChange={handleCourseChange} />
            <Button onClick={handleSubmit} variant="text" size="small">Submit</Button>

            {isError ? "Failed to add application: " + JSON.stringify(error) : null}
        </div>
    </div>
    )
}