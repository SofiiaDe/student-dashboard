import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { API_URL } from "../../../app/constants"
import { RegisterUserModel, RegisterUserResponse } from "./types"

export const registerApi = createApi({
  reducerPath: 'register',
  baseQuery: fetchBaseQuery({ baseUrl: API_URL }),
  endpoints: (builder) => ({
    registerUser: builder.mutation<RegisterUserResponse, RegisterUserModel>({
      query: (user) => ({
        url: "/auth/register",
        method: "POST",
        body: {
          ...user
        },
      }),
    }),
  }),
})


export const {
  useRegisterUserMutation
} = registerApi