import React from 'react'
import { Container, Header, Segment } from 'semantic-ui-react'
import { useAuth } from '../context/AuthContext'

function Home() {
  const { user } = useAuth()

  return (
      <Container text>
        {user ? (
            <Segment>
              <Header as="h2">Welcome!</Header>
              <p><strong>Email:</strong> {user.data.email}</p>
              <p><strong>Token Expiration:</strong> {new Date(user.data.exp * 1000).toLocaleString()}</p>
              <p><strong>Access Token:</strong> {user.accessToken}</p>
              <p><strong>Refresh Token:</strong> {user.refreshToken}</p>
              <p><strong>Refresh Token:</strong> {user.data.roles}</p>
            </Segment>
        ) : (
            <Header as="h2" textAlign="center">
              Please log in to see your information.
            </Header>
        )}
      </Container>
  )
}

export default Home