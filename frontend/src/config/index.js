const config = {
  development: {
    API_BASE_URL: 'http://localhost:8080',
  },
  production: {
    API_BASE_URL: 'https://your-production-api.com',
  },
  // test
};

export default config[process.env.NODE_ENV || 'development'];