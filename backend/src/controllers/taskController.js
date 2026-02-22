const TaskModel = require('../models/taskModel');

// Standard response helper
const sendResponse = (res, statusCode, success, message, data = null) => {
    const response = {
        success,
        message,
        data,
        timestamp: new Date().toISOString()
    };
    return res.status(statusCode).json(response);
};

class TaskController {
    // GET /api/tasks
    static async getAllTasks(req, res) {
        try {
            const tasks = await TaskModel.getAll();
            return sendResponse(res, 200, true, 'Tasks retrieved successfully', tasks);
        } catch (error) {
            console.error('Error in getAllTasks:', error);
            return sendResponse(res, 500, false, 'Failed to retrieve tasks');
        }
    }

    // GET /api/tasks/:id
    static async getTaskById(req, res) {
        try {
            const { id } = req.params;
            const task = await TaskModel.getById(id);
            
            if (!task) {
                return sendResponse(res, 404, false, 'Task not found');
            }
            
            return sendResponse(res, 200, true, 'Task retrieved successfully', task);
        } catch (error) {
            console.error('Error in getTaskById:', error);
            return sendResponse(res, 500, false, 'Failed to retrieve task');
        }
    }

    // POST /api/tasks
    static async createTask(req, res) {
        try {
            const { title, description, is_completed } = req.body;
            
            // Validation
            if (!title || title.trim() === '') {
                return sendResponse(res, 400, false, 'Title is required');
            }
            
            const taskData = {
                title: title.trim(),
                description: description ? description.trim() : null,
                is_completed: is_completed || false
            };
            
            const newTask = await TaskModel.create(taskData);
            return sendResponse(res, 201, true, 'Task created successfully', newTask);
        } catch (error) {
            console.error('Error in createTask:', error);
            return sendResponse(res, 500, false, 'Failed to create task');
        }
    }

    // PUT /api/tasks/:id
    static async updateTask(req, res) {
        try {
            const { id } = req.params;
            const { title, description, is_completed } = req.body;
            
            // Check if task exists
            const existingTask = await TaskModel.getById(id);
            if (!existingTask) {
                return sendResponse(res, 404, false, 'Task not found');
            }
            
            // Validation
            if (!title || title.trim() === '') {
                return sendResponse(res, 400, false, 'Title is required');
            }
            
            const taskData = {
                title: title.trim(),
                description: description !== undefined ? description.trim() : existingTask.description,
                is_completed: is_completed !== undefined ? is_completed : existingTask.is_completed
            };
            
            const updated = await TaskModel.update(id, taskData);
            
            if (updated) {
                const updatedTask = await TaskModel.getById(id);
                return sendResponse(res, 200, true, 'Task updated successfully', updatedTask);
            }
            
            return sendResponse(res, 400, false, 'Failed to update task');
        } catch (error) {
            console.error('Error in updateTask:', error);
            return sendResponse(res, 500, false, 'Failed to update task');
        }
    }

    // DELETE /api/tasks/:id
    static async deleteTask(req, res) {
        try {
            const { id } = req.params;
            
            // Check if task exists
            const existingTask = await TaskModel.getById(id);
            if (!existingTask) {
                return sendResponse(res, 404, false, 'Task not found');
            }
            
            const deleted = await TaskModel.delete(id);
            
            if (deleted) {
                return sendResponse(res, 200, true, 'Task deleted successfully');
            }
            
            return sendResponse(res, 400, false, 'Failed to delete task');
        } catch (error) {
            console.error('Error in deleteTask:', error);
            return sendResponse(res, 500, false, 'Failed to delete task');
        }
    }

    // PATCH /api/tasks/:id/toggle
    static async toggleTaskComplete(req, res) {
        try {
            const { id } = req.params;
            
            const existingTask = await TaskModel.getById(id);
            if (!existingTask) {
                return sendResponse(res, 404, false, 'Task not found');
            }
            
            const toggled = await TaskModel.toggleComplete(id);
            
            if (toggled) {
                const updatedTask = await TaskModel.getById(id);
                return sendResponse(res, 200, true, 'Task status toggled successfully', updatedTask);
            }
            
            return sendResponse(res, 400, false, 'Failed to toggle task status');
        } catch (error) {
            console.error('Error in toggleTaskComplete:', error);
            return sendResponse(res, 500, false, 'Failed to toggle task status');
        }
    }
}

module.exports = TaskController;