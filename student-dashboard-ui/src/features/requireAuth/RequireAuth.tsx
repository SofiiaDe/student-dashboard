import { Button } from "@mui/material"
import { useEffect, useState } from "react"
import { Navigate, useLocation } from "react-router-dom"
import { deleteAuthCookies, getUserNameCookie, isAuthorized } from "../../utils"
import styles from './RequireAuth.module.css'
import { useLogoutMutation } from "./requireAuthAPI"

export function RequireAuth({ children }: { children: JSX.Element }) {

    let isLoggedIn = isAuthorized()
    const location = useLocation()
    const [toLogout, setToLogout] = useState(false)
    const [logout] = useLogoutMutation()

    useEffect(() => {
        if (toLogout) {
            logout().then(() => {
                setToLogout(false)
            })

            deleteAuthCookies()
            isLoggedIn = false
        }
    }, [toLogout])


    if (!isLoggedIn) return <Navigate to="/login" state={{ from: location }} />

    const handleLogoutClick = () => setToLogout(true)

    return <>
        <div className={styles.header}>

            <Button onClick={handleLogoutClick}>Logout</Button>
            Hi, {getUserNameCookie()}
        </div>
        {children}
    </>;
}
