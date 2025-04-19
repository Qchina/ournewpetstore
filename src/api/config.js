// API配置文件

// 后端API基础URL
const API_BASE_URL = 'http://localhost:9090';

// 账户相关API
const API_ENDPOINTS = {
  Captcha: `${API_BASE_URL}/api/v1/captcha`,
  // 用户登录
  LOGIN: `${API_BASE_URL}/api/v1/accounts/login`,
  // 用户注册
  REGISTER: `${API_BASE_URL}/api/v1/accounts/register`,
  // 获取用户信息
  PROFILE: `${API_BASE_URL}/api/v1/accounts/profile`,
  // 更新用户信息
  UPDATE_PROFILE: `${API_BASE_URL}/api/v1/accounts/profile`,
  // 用户登出
  LOGOUT: `${API_BASE_URL}/api/v1/accounts/logout`
};

export default API_ENDPOINTS;