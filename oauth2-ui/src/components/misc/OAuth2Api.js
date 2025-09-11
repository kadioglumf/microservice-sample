import axios from 'axios'
import { config } from '../../Constants'
import { parseJwt } from './Helpers'

export const OAuth2Api = {
  authenticate,
  signup
}

async function authenticate(email, password) {
  try {
    const response = await instance.post(
        '/auth/login',
        { email, password },
        { headers: { 'Content-Type': 'application/json' } }
    )

    const { accessToken, refreshToken } = response.data

    const data = parseJwt(accessToken)

    return {
      data,            // User data from the access token
      accessToken,     // JWT access token
      refreshToken     // Refresh token
    }
  } catch (error) {
    throw new Error(
        error.response?.data?.message || 'An error occurred during login.'
    )
  }
}

async function signup(user) {
  try {
    const response = await instance.post(
        '/auth/signup',
        user,
        { headers: { 'Content-Type': 'application/json' } }
    )
    return response.data
  } catch (error) {
    throw new Error(
        error.response?.data?.message || 'An error occurred during signup.'
    )
  }
}

const instance = axios.create({
  baseURL: config.url.API_BASE_URL
})

instance.interceptors.request.use(
    function (config) {
      if (config.headers.Authorization) {
        const token = config.headers.Authorization.split(' ')[1]
        const data = parseJwt(token)
        if (Date.now() > data.exp * 1000) {
          // Redirect user to login if token is expired
          window.location.href = '/login'
        }
      }
      return config
    },
    function (error) {
      return Promise.reject(error)
    }
)

// -- Helper functions
function bearerAuth(user) {
  return `Bearer ${user.accessToken}`
}