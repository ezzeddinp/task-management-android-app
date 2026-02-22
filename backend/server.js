const app = require('./src/app');
require('dotenv').config();

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
    console.log(`
    - Server running on port ${PORT}
    - Environment: ${process.env.NODE_ENV || 'development'}
    - API Base URL: http://localhost:${PORT}/api
    - Health Check: http://localhost:${PORT}/health
    `);
});