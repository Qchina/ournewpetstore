<template>
 <div class="login-container">
  <div class="auth-form">
    <h2>Login in to Your Account</h2>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label>Username</label>
        <input v-model="loginForm.username" type="text" required>
      </div>
      <div class="form-group">
        <label>Password</label>
        <input v-model="loginForm.password" type="password" required>
      </div>
      <div class="form-group">
        <label>Captcha</label>
        <div class="captcha-container">
          <input v-model="loginForm.captcha" type="text" required>
          <img :src="captchaImage" @click="refreshCaptcha" alt="验证码" class="captcha-image">
        </div>
      </div>
      <button type="submit">登录</button>
    </form>
    <p>Need an account ？<a href="#" @click="switchToRegister">Register now !</a></p>
  </div>
</div>
</template>

<script>
import axios from 'axios'; 
export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        captcha: ''
      },
      captchaImage: '' // 验证码图片base64数据
      
    }
  },
  mounted() {
    this.refreshCaptcha();  // 添加mounted钩子，确保组件加载时获取验证码
  },
  methods: {
    // 从后端获取验证码
    async refreshCaptcha() {
      try {
        const response = await axios.get('http://localhost:9090/api/v1/captcha', {
          withCredentials: true
        });
        this.captchaImage = response.data.captchaImage;
        console.log('验证码获取成功');
      } catch (error) {
        console.error('获取验证码失败:', error);
        alert('获取验证码失败，请刷新页面重试');
      }
    },
    async handleLogin() {
      try {
        if (!this.loginForm.captcha) {
          alert('请输入验证码');
          return;
        }

        const formData = new URLSearchParams();
        formData.append('username', this.loginForm.username);
        formData.append('password', this.loginForm.password);
        formData.append('code', this.loginForm.captcha);  // 移除toLowerCase()
        
        console.log('正在提交登录请求...');
        const response = await axios.post('http://localhost:9090/api/v1/accounts/login', formData, {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          withCredentials: true
        });
        
        if (response.data.success) {
          localStorage.setItem('user', JSON.stringify(response.data.data));
          this.$router.push('/accountauth');
        } else {
          alert(response.data.message || '登录失败');
          this.refreshCaptcha();
        }
      } catch (error) {
        console.error('登录请求失败:', error.response?.data || error);
        if (error.response?.data?.message) {
          alert(error.response.data.message);
        } else {
          alert('登录失败，请检查用户名和密码');
        }
        this.refreshCaptcha();
      }
    },
    
    switchToRegister() {
      this.$emit('switch-to-register')
    }
  }
}
</script>

<style scoped>
/* 新增验证码样式 */
.captcha-container {
  display: flex;
  align-items: center;
}
.captcha-image {
  margin-left: 10px;
  height: 40px;
  cursor: pointer;
  border: 1px solid #ddd;
  padding: 5px;
}
.captcha-container::after {
  content: "Click to refresh";
  margin-left: 10px;
  font-size: 12px;
  color: #666;
}

.login-container {
  margin: 0 auto;
  max-width: 400px;
  padding: 30px;
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.3); /* 可选的遮罩层，增强文字可读性 */
  z-index: 0;
}


.auth-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 30px;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.9);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transform: translateY(0);
  transition: all 0.3s ease;
  position: relative;
  z-index: 1;
}

.auth-form:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

/* 新增动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

button {
  /* 修改按钮样式 */
  background-color: #4caf50;
  color: white;
  padding: 12px 24px;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s ease;
  width: 100%;
}

button:hover {
  background-color: #45a049;
  transform: scale(1.02);
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  font-size: 20px;
}

.form-group {
  margin-bottom: 20px;
}

input {
  width: 95%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
  background-color: #f5f9ff;
}

a {
  color: #007bff;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style>