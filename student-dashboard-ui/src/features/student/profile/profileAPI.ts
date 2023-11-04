import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { API_URL } from "../../../app/constants"
import { getAuthHeader } from "../../../utils"
import { ApplicationsResponse, SubmitApplicationModel } from "./types"

export const profileApi = createApi({
  reducerPath: 'profile',
  baseQuery: fetchBaseQuery({ baseUrl: API_URL }),
  endpoints: (builder) => ({
    getApplications: builder.query<ApplicationsResponse, void>({
      query() {
        return {
          url: "/applications",
          headers: { ...getAuthHeader() }
        }
      },
    }),
    submitApplication: builder.mutation<string, SubmitApplicationModel>({
      query: (application) => ({
        url: "/applications",
        method: "POST",
        headers: {
          ...getAuthHeader()
        },
        body: {
          ...application
        },
        responseHandler: "text",
      }),
    }),
  }),
})


export const {
  useGetApplicationsQuery,
  useSubmitApplicationMutation,
} = profileApi