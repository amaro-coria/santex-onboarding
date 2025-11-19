import { useState, useEffect } from 'react'
import { useApi } from './useApi'

/**
 * Custom hook for managing tasks
 * @param {Object} filters - Optional filters for tasks
 * @returns {Object} Tasks data and operations
 */
export function useTasks(filters = {}) {
  const { taskApi, isAuthenticated } = useApi()
  const [tasks, setTasks] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const fetchTasks = async () => {
    if (!isAuthenticated) {
      setTasks([])
      setLoading(false)
      return
    }

    try {
      setLoading(true)
      setError(null)
      const response = await taskApi.getAll()
      setTasks(response.data)
    } catch (err) {
      console.error('Error fetching tasks:', err)
      setError(err.response?.data?.message || 'Failed to fetch tasks')
      setTasks([])
    } finally {
      setLoading(false)
    }
  }

  const createTask = async (taskData) => {
    try {
      const response = await taskApi.create(taskData)
      setTasks((prev) => [...prev, response.data])
      return response.data
    } catch (err) {
      console.error('Error creating task:', err)
      throw err
    }
  }

  const updateTask = async (id, taskData) => {
    try {
      const response = await taskApi.update(id, taskData)
      setTasks((prev) => prev.map((task) => (task.id === id ? response.data : task)))
      return response.data
    } catch (err) {
      console.error('Error updating task:', err)
      throw err
    }
  }

  const deleteTask = async (id) => {
    try {
      await taskApi.delete(id)
      setTasks((prev) => prev.filter((task) => task.id !== id))
    } catch (err) {
      console.error('Error deleting task:', err)
      throw err
    }
  }

  const updateTaskStatus = async (id, status) => {
    try {
      const response = await taskApi.updateStatus(id, status)
      setTasks((prev) => prev.map((task) => (task.id === id ? response.data : task)))
      return response.data
    } catch (err) {
      console.error('Error updating task status:', err)
      throw err
    }
  }

  useEffect(() => {
    fetchTasks()
  }, [isAuthenticated])

  return {
    tasks,
    loading,
    error,
    createTask,
    updateTask,
    deleteTask,
    updateTaskStatus,
    refetch: fetchTasks,
  }
}

/**
 * Custom hook for managing a single task
 * @param {number} taskId - Task ID
 * @returns {Object} Task data and operations
 */
export function useTask(taskId) {
  const { taskApi, isAuthenticated } = useApi()
  const [task, setTask] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const fetchTask = async () => {
    if (!isAuthenticated || !taskId) {
      setLoading(false)
      return
    }

    try {
      setLoading(true)
      setError(null)
      const response = await taskApi.getById(taskId)
      setTask(response.data)
    } catch (err) {
      console.error('Error fetching task:', err)
      setError(err.response?.data?.message || 'Failed to fetch task')
      setTask(null)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchTask()
  }, [taskId, isAuthenticated])

  return {
    task,
    loading,
    error,
    refetch: fetchTask,
  }
}
