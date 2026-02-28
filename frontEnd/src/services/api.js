import axios from 'axios';

// Base HTTP client configuration for REST API communication
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000 
});

// Interceptor for centralized API error handling
api.interceptors.response.use(
  response => response,
  error => {
    const message = error.response?.data?.message || "Something went wrong";
    console.error("API Error:", message);
    return Promise.reject(error);
  }
);

export default api;