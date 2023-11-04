import { Button, Snackbar, Typography } from "@mui/material"
import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"

export function UserLoginData(props: { firstName: string, userName: string, password: string }) {
    const { firstName, userName, password } = props

    const navigate = useNavigate()
    const [showSnackbar, setShowSnackbar] = useState(true)

    useEffect(() => {
        setTimeout(() => setShowSnackbar(false), 1500)
    }, [])

    const handleSnackbarClose = () => setShowSnackbar(false)

    const handleLoginClick = () => {
        navigate("/login")
    }

    return <>
        <Typography variant="body1"> Your login: {userName}</Typography>
        <br />
        <Typography variant="body2"> Your password: {password}</Typography>
        <br />
        <Button onClick={handleLoginClick} variant="contained" sx={{ mb: 3 }}>Login</Button>

        <Snackbar
            open={showSnackbar}
            autoHideDuration={1500}
            message={<>User {firstName} successfully registered</>}
            onClose={handleSnackbarClose}
        />
    </>
}
