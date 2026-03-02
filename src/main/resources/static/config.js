// config.js
const isLocal = window.location.hostname === 'localhost'
             || window.location.hostname === '127.0.0.1';

window.APP_CONFIG = {
  API_BASE: isLocal
    ? 'http://localhost:8080/api'
    : 'https://rspl-hr-onboarding-backend-production.up.railway.app/api',
  APP_BASE: isLocal
    ? 'http://localhost:8080'
    : 'https://rspl-hr-onboarding-backend-production.up.railway.app',
  ENV: isLocal ? 'local' : 'production'
};

// Backward compatibility
window.API_BASE = window.APP_CONFIG.API_BASE;
