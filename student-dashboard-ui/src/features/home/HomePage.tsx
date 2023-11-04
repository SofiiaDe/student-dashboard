import { Button, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";

export default function HomePage() {

    const navigate = useNavigate()

    const handleLoginClick = () => navigate("/login")
    const handleRegisterClick = () => navigate("/register")

    return <>
        <Typography variant="h3" sx={{ m: 4 }}>Student dashboard</Typography>
        <Button sx={{ m: 2 }} onClick={handleLoginClick} variant="contained">Login</Button>
        <Button sx={{ m: 2 }} onClick={handleRegisterClick} variant="contained">Register</Button>
    </>
}