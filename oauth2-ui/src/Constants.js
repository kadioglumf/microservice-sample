const dev = {
  url: {
    API_BASE_URL: 'http://localhost:8081/auth-service',
    OAUTH2_REDIRECT_URI: 'http://localhost:3000/oauth2/redirect'
  }
}

export const config = process.env.NODE_ENV === 'development' ? dev : {};
