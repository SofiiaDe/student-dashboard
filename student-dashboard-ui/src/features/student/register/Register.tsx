import { Button, Snackbar, TextField, Typography } from "@mui/material"
import { useState } from "react"
import styles from "./Register.module.css"
import { UserLoginData } from "./UserLoginData"
import { useRegisterUserMutation } from "./registerAPI"

const validationErrorMessage = <Typography variant="h6" color="red">Please enter all the fields</Typography>

export default function Register() {
  const [firstName, setFirstName] = useState("")
  const [lastName, setLastName] = useState("")
  const [validationError, setValidationError] = useState(false)
  const [showSnackbar, setShowSnackbar] = useState(false)

  const [registerUser, { isSuccess, data }] = useRegisterUserMutation()

  const handleFirstName = (e: any) => setFirstName(e.target.value)
  const handleLastName = (e: any) => setLastName(e.target.value)

  const handleSubmit = (e: any) => {
    e.preventDefault()

    if (!firstName || !lastName) {
      setValidationError(true)
      return
    }

    validationError && setValidationError(false)

    registerUser({
      firstName,
      lastName
    }).then((response: any) => {
      if (response.error) {
        setShowSnackbar(true)
      }
    })
  }

  const handleSnackbarClose = () => setShowSnackbar(false)

  return (
    <div>
      <Typography variant="h4" sx={{ mt: 2, mb: 1 }}>User Registration</Typography>
      <div>
        {validationError ? validationErrorMessage : null}
      </div>
      {(isSuccess && data) ?
        (<UserLoginData firstName={firstName} userName={data.username} password={data.password} />)
        : null}

      {!isSuccess && (<div className={styles.formParent}>
        <div className={styles.form}>
          <TextField sx={{ m: 2 }} label="First Name" variant="outlined" onChange={handleFirstName} />
          <TextField sx={{ m: 2 }} label="Last Name" variant="outlined" onChange={handleLastName} />
          <Button sx={{ m: 2 }} onClick={handleSubmit} variant="contained">Submit</Button>
        </div>
      </div>)}

      <Snackbar
        open={showSnackbar}
        autoHideDuration={5000}
        message="Failed to register. Please, try again later"
        onClose={handleSnackbarClose}
      />

    </div>
  )
}
