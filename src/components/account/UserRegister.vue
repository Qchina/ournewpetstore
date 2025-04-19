<template>
  <div class="register-container">
    <div class="auth-form">
      <h2>Register A New Account</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>Username</label>
          <input v-model="registerForm.username" type="text" required>
        </div>
        <div class="form-group">
          <label>Password</label>
          <input v-model="registerForm.password" type="password" required>
        </div>
        <div class="form-group">
          <label>Password Again</label>
          <input v-model="registerForm.confirmPassword" type="password" required>
          <p v-if="passwordMismatch" class="error-message">密码不匹配</p>  
        </div>
        <div class="form-group">
        <label>Captcha</label>
        <div class="captcha-container">
          <input v-model="registerForm.captcha" type="text" required>
          <img :src="captchaImage" @click="refreshCaptcha" alt="验证码" class="captcha-image">
        </div>
      </div>
        <div class="form-footer">
          <button type="submit">注册</button>
        </div>
      </form>
      <p>Already have an account？<a href="#" @click="switchToLogin">Go Login !</a></p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        captcha: ''
      },
       captchaImage: '' // 验证码图片base64数据
    }
  },
  mounted() {
    this.refreshCaptcha();  // 添加mounted钩子，确保组件加载时获取验证码
  },
  computed: {
    passwordMismatch() {
      return this.registerForm.password && 
             this.registerForm.confirmPassword && 
             this.registerForm.password !== this.registerForm.confirmPassword
    }
  },
  methods: {
    async refreshCaptcha() {
      try {
        const response = await axios.get('http://localhost:9090/api/v1/captcha', {
          withCredentials: true
        });
        this.captchaImage = response.data.captchaImage;
      } catch (error) {
        console.error('获取验证码失败:', error);
        alert('获取验证码失败，请刷新页面重试');
      }
    },
    async handleRegister() {
      if (this.passwordMismatch) {
        alert('请确保两次输入的密码一致');
        return;
      }
      try {
        const registerData = { ...this.registerForm };
        delete registerData.confirmPassword; // 删除确认密码字段
        
        const response = await axios.post('http://localhost:9090/api/v1/accounts/register', registerData, {
          withCredentials: true
        });
        
        if (response.data.success) {
          alert('注册成功');
          this.registerForm = {
            username: '',
            password: '',
            confirmPassword: '',
            captcha: ''
          };
          this.$emit('switch-to-login');
        } else {
          alert(response.data.message || '注册失败');
          this.refreshCaptcha();
        }
      } catch (error) {
        if (error.response) {
          alert(error.response.data.message || '注册失败');
        } else {
          alert('网络错误，请稍后再试');
        }
        this.refreshCaptcha();
      }
    },
    switchToLogin() {
      this.$emit('switch-to-login')
    }
  }
}
</script>

<style scoped>
/* 添加验证码相关样式 */
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

.register-container {
  margin: 0 auto;
  max-width: 400px;
  padding: 30px;
}

.register-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.3);
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

button {
  background-color: #2196F3;
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
  background-color: #0b7dda;
  transform: scale(1.02);
}

a {
  color: #007bff;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

.error-message {
  color: red;
  font-size: 12px;
  margin-top: 5px;
}

.form-footer {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-top: 20px;
}

.form-footer p {
  margin: 0;
}
</style>