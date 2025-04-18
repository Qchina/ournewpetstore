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
      captchaImage: '',  // 验证码图片base64数据
      captchaText: ''    // 验证码文本
    }
  },
  mounted() {
    this.refreshCaptcha();  // 组件加载时获取验证码
  },
  methods: {
    generateCaptcha() {
    // 生成4位随机字母数字验证码
    const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789';
    let result = '';
    for (let i = 0; i < 4; i++) {
      result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    this.captchaText = result;
    
    // 创建验证码图片（简单实现）
    const canvas = document.createElement('canvas');
    canvas.width = 100;
    canvas.height = 40;
    const ctx = canvas.getContext('2d');
    
    // 背景色
    ctx.fillStyle = '#f5f5f5';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    
    // 绘制验证码文本
    ctx.font = '24px Arial';
    ctx.fillStyle = '#333';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.fillText(result, canvas.width/2, canvas.height/2);
    
    // 添加干扰线
    for (let i = 0; i < 5; i++) {
      ctx.strokeStyle = '#ccc';
      ctx.beginPath();
      ctx.moveTo(Math.random() * canvas.width, Math.random() * canvas.height);
      ctx.lineTo(Math.random() * canvas.width, Math.random() * canvas.height);
      ctx.stroke();
    }
    
    this.captchaImage = canvas.toDataURL();
  },
  
  refreshCaptcha() {
    this.generateCaptcha();
  },
  
  mounted() {
    this.generateCaptcha(); // 组件加载时生成验证码
  },
  
  async handleLogin() {
    // 验证码验证（不区分大小写）
    if (this.loginForm.captcha.toUpperCase() !== this.captchaText.toUpperCase()) {
      alert('验证码错误');
      this.refreshCaptcha();
      return;
    }
      try {
      const response = await axios.post('http://localhost:8080/api/user/login', {
        username: this.loginForm.username,
        password: this.loginForm.password
      });
      
      if (response.data.success) {
        // 登录成功处理
        localStorage.setItem('token', response.data.token);
        this.$router.push('/'); // 跳转到首页
      } else {
        alert(response.data.message || '登录失败');
        this.refreshCaptcha();
      }
    } catch (error) {
      console.error('登录请求失败:', error);
      alert('网络错误，请稍后重试');
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