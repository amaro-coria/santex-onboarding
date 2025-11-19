import { useEffect } from 'react'
import { useAuth0 } from '@auth0/auth0-react'
import { setAuthToken, userApi, taskApi, testApi } from '../services/api'

/**
 * Custom hook that integrates Auth0 authentication with API calls
 * Automatically sets the JWT token in axios headers when user is authenticated
 */
export const useApi = () => {
  const { isAuthenticated, getAccessTokenSilently } = useAuth0()

  useEffect(() => {
    const updateToken = async () => {
      if (isAuthenticated) {
        try {
          const token = await getAccessTokenSilently({
            authorizationParams: {
              audience: import.meta.env.VITE_AUTH0_AUDIENCE,
              scope: "openid profile email",
            },
          })
          setAuthToken(token)
        } catch (error) {
          console.error('Error getting access token:', error)
          setAuthToken(null)
        }
      } else {
        setAuthToken(null)
      }
    }

    updateToken()
  }, [isAuthenticated, getAccessTokenSilently])

  return {
    userApi,
    taskApi,
    testApi,
    isAuthenticated,
  }
}

export default useApi
