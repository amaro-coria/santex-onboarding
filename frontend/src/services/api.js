import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// Create axios instance with default configuration
const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Function to set the authorization token
export const setAuthToken = (token) => {
  if (token) {
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`
  } else {
    delete api.defaults.headers.common['Authorization']
  }
}

// User API endpoints
export const userApi = {
  getAll: () => api.get('/api/users'),
  getById: (id) => api.get(`/api/users/${id}`),
  getByEmail: (email) => api.get(`/api/users/email/${email}`),
  create: (userData) => api.post('/api/users', userData),
  update: (id, userData) => api.put(`/api/users/${id}`, userData),
  delete: (id) => api.delete(`/api/users/${id}`),
  exists: (email) => api.get(`/api/users/exists/${email}`),
}

// Task API endpoints
export const taskApi = {
  getAll: () => api.get('/api/tasks'),
  getById: (id) => api.get(`/api/tasks/${id}`),
  getByUserId: (userId) => api.get(`/api/tasks/user/${userId}`),
  getByStatus: (status) => api.get(`/api/tasks/status/${status}`),
  create: (taskData) => api.post('/api/tasks', taskData),
  update: (id, taskData) => api.put(`/api/tasks/${id}`, taskData),
  delete: (id) => api.delete(`/api/tasks/${id}`),
  updateStatus: (id, status) => api.patch(`/api/tasks/${id}/status`, { status }),
  assignToUser: (id, userId) => api.patch(`/api/tasks/${id}/assign/${userId}`),
  getOverdueTasks: () => api.get('/api/tasks/overdue'),
}

// Test endpoint (public, doesn't require auth)
export const testApi = {
  hello: () => api.get('/api/hello'),
}

export default api
