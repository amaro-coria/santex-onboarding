import * as yup from 'yup'

/**
 * Validation schema for task creation and editing
 */
export const taskSchema = yup.object({
  title: yup
    .string()
    .required('Title is required')
    .min(3, 'Title must be at least 3 characters')
    .max(255, 'Title must not exceed 255 characters'),

  description: yup
    .string()
    .max(1000, 'Description must not exceed 1000 characters'),

  status: yup
    .string()
    .oneOf(['TODO', 'IN_PROGRESS', 'DONE'], 'Invalid status')
    .required('Status is required'),

  dueDate: yup
    .date()
    .nullable()
    .min(new Date(), 'Due date must be in the future'),

  assignedUserId: yup
    .number()
    .nullable()
    .positive('User ID must be a positive number'),
})

/**
 * Validation schema for user creation and editing
 */
export const userSchema = yup.object({
  email: yup
    .string()
    .email('Invalid email address')
    .required('Email is required'),

  firstName: yup
    .string()
    .required('First name is required')
    .min(2, 'First name must be at least 2 characters')
    .max(100, 'First name must not exceed 100 characters'),

  lastName: yup
    .string()
    .max(100, 'Last name must not exceed 100 characters'),
})
