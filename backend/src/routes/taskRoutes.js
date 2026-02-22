const express = require('express');
const TaskController = require('../controllers/taskController');

const router = express.Router();

// Routes
router.get('/', TaskController.getAllTasks);
router.get('/:id', TaskController.getTaskById);
router.post('/', TaskController.createTask);
router.put('/:id', TaskController.updateTask);
router.delete('/:id', TaskController.deleteTask);
router.patch('/:id/toggle', TaskController.toggleTaskComplete);

module.exports = router;