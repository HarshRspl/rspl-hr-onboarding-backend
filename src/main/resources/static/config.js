// config.js
window.APP_CONFIG = {
  API_BASE: 'http://localhost:8080/api',
  APP_BASE: 'http://localhost:8080',
  ENV: 'local'
};

// For production, switch to:
// window.APP_CONFIG = {
//   API_BASE: 'https://rspl-hr-onboarding-backend-production.up.railway.app/api',
//   APP_BASE: 'https://rspl-hr-onboarding-backend-production.up.railway.app',
//   ENV: 'production'
// };

// Backward compatibility:
window.API_BASE = window.APP_CONFIG.API_BASE;
