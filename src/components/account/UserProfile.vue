<template>
    <div class="profile-container">
  <div class="auth-form">
    <h2>User Profile</h2>
    <form @submit.prevent="handleUpdate">
      <div class="form-container">
        <div class="form-column">
          <div class="form-group">
            <label>Username</label>
            <input v-model="userProfile.username" type="text" required disabled>
          </div>
          <div class="form-group">
            <label>Email</label>
            <input v-model="userProfile.email" type="email" required>
          </div>
          <div class="form-group">
            <label>First Name</label>
            <input v-model="userProfile.firstName" type="text" required>
          </div>
          <div class="form-group">
            <label>Last Name</label>
            <input v-model="userProfile.lastName" type="text" required>
          </div>
          <div class="form-group">
            <label>Telephone</label>
            <input v-model="userProfile.phone" type="tel" required>
          </div>
        </div>
        <div class="form-column">
          <div class="form-group">
            <label>Address1</label>
            <input v-model="userProfile.address1" type="text" required>
          </div>
          <div class="form-group">
            <label>Address2</label>
            <input v-model="userProfile.address2" type="text">
          </div>
          <div class="form-group">
            <label>City</label>
            <input v-model="userProfile.city" type="text" required>
          </div>
          <div class="form-group">
            <label>Status</label>
            <input v-model="userProfile.status" type="text" required>
          </div>
          <div class="form-group">
            <label>Zip Code</label>
            <input v-model="userProfile.zip" type="text" required>
          </div>
        </div>
      </div>
      <div class="form-footer">
        <button type="submit">Update Profile</button>
        <button type="button" @click="switchToUserLogs">View Logs</button>
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
      userProfile: {
        username: '',
        email: '',
        firstName: '',
        lastName: '',
        phone: '',
        address1: '',
        address2: '',
        city: '',
        zip: '',
        status: ''
      }
    }
  },
  async created() {
    await this.fetchUserProfile();
  },
  methods: {
    async fetchUserProfile() {
      try {
        // 确保使用withCredentials发送请求
        const response = await axios.get('http://localhost:9090/api/v1/accounts/profile', {
          withCredentials: true
        });
        
        console.log('获取用户信息响应:', response.data); // 添加日志查看响应内容
        
        if (response.data.success) {
          this.userProfile = response.data.data;
        } else {
          console.error('获取用户信息失败:', response.data.message);
          // 如果是未登录状态，可以尝试重定向到登录页面
          if (response.data.message === '未登录') {

            // 可以添加重定向到登录页面的逻辑
            this.$router.push('/userauth');
          } else {
            alert(response.data.message || '获取用户信息失败');
          }
        }
      } catch (error) {
        console.error('获取用户信息失败:', error);
        alert('网络错误，请稍后再试');
      }
    },
        async handleUpdate() {
      try {
        console.log('准备更新的用户信息:', this.userProfile); // 添加日志
        
        const response = await axios.put('http://localhost:9090/api/v1/accounts/profile', this.userProfile, {
          headers: {
            'Content-Type': 'application/json' // 确保设置正确的内容类型
          },
          withCredentials: true
        });
        
        console.log('更新响应:', response.data); // 添加日志
        
        if (response.data.success) {
          alert('个人信息更新成功');
          // 重新获取用户信息以确认更新
          await this.fetchUserProfile();
        } else {
          alert(response.data.message || '更新失败');
        }
      } catch (error) {
        console.error('更新个人信息失败:', error);
        if (error.response) {
          console.error('错误响应:', error.response.data);
        }
        alert('网络错误，请稍后再试');
      }
    },
    switchToUserLogs() {
      this.$emit('switch-to-userLogs')
    }
  }
}
</script>

<style scoped>
.profile-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 30px;
}

.profile-container::before {
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
input:disabled {
  background-color: #f0f0f0;
  color: #666;
  cursor: not-allowed;
  border: 1px solid #ddd;
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