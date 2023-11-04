import { Paper } from "@mui/material"
import { Route, Routes } from "react-router-dom"
import "./App.css"
import { HomePage } from "./features/home"
import NotFoundPage from "./features/notFound"
import { RequireAuth } from "./features/requireAuth/RequireAuth"
import Login from "./features/student/login/Login"
import Profile from "./features/student/profile/Profile"
import Register from "./features/student/register/Register"

function App() {
  return (
    <div className="App">
      <Paper sx={{ width: 940, mt: 4, height: 'fit-content' }} >
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/profile" element={<RequireAuth><Profile /></RequireAuth>} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Paper>
    </div>
  )
}

export default App
