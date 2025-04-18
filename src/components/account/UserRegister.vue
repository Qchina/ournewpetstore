<template>
  <div class="register-container">
  <div class="auth-form">
    <h2>Register A New Account</h2>
    <form @submit.prevent="handleRegister">
      <div class = "form-container">
      <div class="form-column">
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
          <label>Email</label>
          <input v-model="registerForm.email" type="email" required>
        </div>
        <div class="form-group">
          <label>First Name</label>
          <input v-model="registerForm.firstname" type="text" required>
        </div>
        <div class="form-group">
          <label>Last Name</label>
          <input v-model="registerForm.lastname" type="text" required>
        </div>
      <div class="form-group">
          <label>Telephone</label>
          <input v-model="registerForm.phone" type="tel" required>
      </div>
      <div class="form-group">
          <label>Address1</label>
          <input v-model="registerForm.address1" type="text" required>
        </div>
        </div>
      <div class="form-column">
      <!-- 这里可以添加更多的表单组 -->
      
      <div class="form-group">
          <label>Address2</label>
          <input v-model="registerForm.address2" type="text">
        </div>
      <div class="form-group">
          <label>City</label>
          <input v-model="registerForm.city" type="text" required>
        </div>
        <div class="form-group">
          <label>Status</label>
          <input v-model="registerForm.status" type="text" required>
        </div>
        <div class="form-group">
          <label>Zip Code</label>
          <input v-model="registerForm.zip" type="text" required>
        </div>
        <div class="form-group">
          <label>Country</label>
          <input v-model="registerForm.country" type="text" required>
        </div>
        <div class="form-group">
          <label>Language Preference</label>
          <input v-model="registerForm.language" type="text" required>
        </div>
        <div class="form-group">
          <label>Favourite Category</label>
          <input v-model="registerForm.favoriteCategory" type="text" required>
        </div>
        <div class="form-group checkbox-group">
        <label>
          <input type="checkbox" v-model="registerForm.enableMyList"> Enable MyList
        </label>
        </div>
        <div class="form-group checkbox-group">
        <label>
          <input type="checkbox" v-model="registerForm.enableMyBanner"> Enable MyBanner
        </label>
        </div>
      </div>
      </div>
        <div class="form-footer">
        <button type="submit">注册</button>
        <p>Already have an accouunt？<a href="#" @click="switchToLogin">Go Login !</a></p>
      </div>
    </form>
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
        email: '',
        firstname: '',
        lastname: '',
        phone: '',
        address1: '',
        address2: '',
        city: '',
        zip: '',
        status: '',
        country: '',
        language: 'English', // 新增语言偏好，默认英语
        favoriteCategory: 'FISH', // 新增喜好分类，默认鱼类
        enableMyList: true,
        enableMyBanner: true
      }
    }
  },
  computed: {
    passwordMismatch() {
      return this.registerForm.password && 
             this.registerForm.confirmPassword && 
             this.registerForm.password !== this.registerForm.confirmPassword
    }
  },
  methods: {
    async handleRegister() {
      if (this.passwordMismatch) {
        alert('请确保两次输入的密码一致');
        return;
      }
      // 这里添加注册逻辑
      try {
        // Remove the unused response variable
        await axios.post('http://localhost:8080/api/user/register', this.registerForm);
        alert('注册成功');
        this.$router.push('/login');
      } catch (error) {
        if (error.response) {
          alert(error.response.data.message || '注册失败');
        } else {
          alert('网络错误，请稍后再试');
        }
      }
    },
    switchToLogin() {
      this.$emit('switch-to-login')
    }
  }
}
</script>

<style scoped>
.register-container {
  max-width: 900px;
  margin: 0 auto;
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
  max-width: 900px; /* 适当增加表单容器的宽度 */
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

.form-container {
  display: flex;
  gap: 20px;
}

.form-column {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-group {
/* flex: 1 0 calc(50% - 10px); 每个表单组占50%宽度，减去一定的间距 */
  margin-bottom: 0;
}

input {
  width: 95%;;
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

/* 新增勾选项样式 */
.checkbox-group {
  display: flex;
  align-items: center;
}

.checkbox-group label {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.checkbox-group input[type="checkbox"] {
  width: auto;
  margin-right: 8px;
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