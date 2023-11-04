import { Action, ThunkAction, configureStore } from "@reduxjs/toolkit";
import { loginApi } from "../features/student/login/loginAPI";
import { profileApi } from "../features/student/profile/profileAPI";
import { registerApi } from "../features/student/register/registerAPI";
import { requireAuthApi } from "../features/requireAuth/requireAuthAPI";

export const store = configureStore({
  reducer: {
    [registerApi.reducerPath]: registerApi.reducer,
    [loginApi.reducerPath]: loginApi.reducer,
    [profileApi.reducerPath]: profileApi.reducer,
    [requireAuthApi.reducerPath]: requireAuthApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware()
      .concat(registerApi.middleware)
      .concat(loginApi.middleware)
      .concat(profileApi.middleware)
      .concat(requireAuthApi.middleware),
})

export type AppDispatch = typeof store.dispatch
export type RootState = ReturnType<typeof store.getState>
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>
