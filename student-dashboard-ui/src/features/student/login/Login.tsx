import { Button, Snackbar, TextField, Typography } from "@mui/material"
import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { setAuthCookies, setCookieByName } from "../../../utils"
import styles from "./Login.module.css"
import { useLoginMutation } from "./loginAPI"

const errorMessage = <Typography variant="h6" color="red">Please enter all the fields</Typography>


export default function Login() {
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [showFailedLoginSnackbar, setShowFailedLoginSnackbar] = useState(false)

  const [login, { data, isError, isSuccess }] = useLoginMutation()


  useEffect(() => {
    if (isSuccess && data) {
      setAuthCookies(data.accessToken, data.refreshToken)
      setCookieByName("username", username)
      navigate("/profile")
    }
  }, [isSuccess, data])
  const [validationError, setValidationError] = useState(false)

  const navigate = useNavigate()

  const handleUsername = (e: any) => setUsername(e.target.value)
  const handlePassword = (e: any) => setPassword(e.target.value)

  const handleSubmit = (e: any) => {
    e.preventDefault()
    if (!username || !password) {
      setValidationError(true)
      return
    }

    login({
      username,
      password
    }).then((response: any) => {
      if (response.error) {
        setShowFailedLoginSnackbar(true)
      }
    })
  }

  const handleFailedLoginSnackbarClose = () => setShowFailedLoginSnackbar(false)

  return (
    <div>
      <Typography variant="h4" sx={{ mt: 2, mb: 1 }}>User Login</Typography>

      {validationError ? errorMessage : null}

      <div className={styles.form}>
        <div className={styles.inputForm}>
          <TextField sx={{ m: 2 }} label="Username" variant="outlined" onChange={handleUsername} />
          <TextField sx={{ m: 2 }} label="Password" type="password" variant="outlined" onChange={handlePassword} />
          <Button sx={{ m: 2 }} onClick={handleSubmit} variant="contained">Submit</Button>
        </div>
      </div>

      <Snackbar
        open={showFailedLoginSnackbar}
        autoHideDuration={5000}
        message="Login or password is incorrect"
        onClose={handleFailedLoginSnackbarClose}
      />
    </div>
  )
}
