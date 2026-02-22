const { pool } = require('../config/database');

class TaskModel {
    // Get all tasks
    static async getAll() {
        const [rows] = await pool.query(
            'SELECT * FROM tasks ORDER BY created_at DESC'
        );
        return rows;
    }

    // Get task by ID
    static async getById(id) {
        const [rows] = await pool.query(
            'SELECT * FROM tasks WHERE id = ?',
            [id]
        );
        return rows[0];
    }

    // Create new task
    static async create(taskData) {
        const { title, description, is_completed = false } = taskData;
        
        const [result] = await pool.query(
            `INSERT INTO tasks (title, description, is_completed, created_at) 
             VALUES (?, ?, ?, NOW())`,
            [title, description, is_completed]
        );
        
        return {
            id: result.insertId,
            title,
            description,
            is_completed,
            created_at: new Date()
        };
    }

    // Update task
    static async update(id, taskData) {
        const { title, description, is_completed } = taskData;
        
        const [result] = await pool.query(
            `UPDATE tasks 
             SET title = ?, description = ?, is_completed = ? 
             WHERE id = ?`,
            [title, description, is_completed, id]
        );
        
        return result.affectedRows > 0;
    }

    // Delete task
    static async delete(id) {
        const [result] = await pool.query(
            'DELETE FROM tasks WHERE id = ?',
            [id]
        );
        return result.affectedRows > 0;
    }

    // Toggle task completion status
    static async toggleComplete(id) {
        const [result] = await pool.query(
            `UPDATE tasks 
             SET is_completed = NOT is_completed 
             WHERE id = ?`,
            [id]
        );
        return result.affectedRows > 0;
    }
}

module.exports = TaskModel;