import React from 'react'
import { Navigate } from 'react-router-dom'
import { Container } from 'semantic-ui-react'
import { useAuth } from '../context/AuthContext'

function UserPage() {
  const Auth = useAuth()
  const user = Auth.getUser()
  const isUser = user.data.roles[0] === 'ROLE_USER'

  if (!isUser) {
    return <Navigate to='/' />
  }

  return (
    <Container>
    </Container>
  )
}

export default UserPage