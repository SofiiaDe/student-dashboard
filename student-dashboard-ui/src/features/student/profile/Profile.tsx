import { Button, CircularProgress, Snackbar, Typography } from "@mui/material"
import { useState } from "react"
import ApplicationList from "./ApplicationList"
import styles from "./Profile.module.css"
import SubmitApplication from "./SubmitApplication"
import { useGetApplicationsQuery } from "./profileAPI"

export default function Profile() {

    const [showApplicationForm, setShowApplicationForm] = useState(false)
    const [showSnackbar, setShowSnackbar] = useState(false)

    const { data, isLoading, isError, refetch } = useGetApplicationsQuery()

    const handleSubmit = () => setShowApplicationForm(true)

    const handlePostSubmit = () => {
        setShowApplicationForm(false)
        setShowSnackbar(true)
        refetch()
    }

    const handleSnackbarClose = () => {
        setShowSnackbar(false)
    }

    return <>
        {isLoading ? <div className={styles.loader}><CircularProgress /></div> : null}
        {isError ? <Typography>Failed to fetch applications</Typography> : null}

        {data?.items ? <ApplicationList applications={data.items} /> : null}
        {!showApplicationForm && <Button sx={{ m: 3 }} onClick={handleSubmit} variant="outlined" >Create application</Button>}
        {showApplicationForm && <SubmitApplication onSubmit={handlePostSubmit} />}

        <Snackbar
            open={showSnackbar}
            autoHideDuration={5000}
            message="Added an application"
            onClose={handleSnackbarClose}
        />
    </>
}
