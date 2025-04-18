<template>
    <div class="profile-container">
  <div class="auth-form">
    <h2>User Profile</h2>
    <form @submit.prevent="handleUpdate">
      <div class="form-container">
        <div class="form-column">
          <div class="form-group">
            <label>Username</label>
            <input v-model="userProfile.username" type="text" required>
          </div>
          <div class="form-group">
            <label>Email</label>
            <input v-model="userProfile.email" type="email" required>
          </div>
          <div class="form-group">
            <label>First Name</label>
            <input v-model="userProfile.firstname" type="text" required>
          </div>
          <div class="form-group">
            <label>Last Name</label>
            <input v-model="userProfile.lastname" type="text" required>
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
        firstname: '',
        lastname: '',
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
        const response = await axios.get('/api/user/profile');
        this.userProfile = response.data;
      } catch (error) {
        console.error('Failed to fetch user profile:', error);
      }
    },
    async handleUpdate() {
      try {
        await axios.put('/api/user/profile', this.userProfile);
        alert('Profile updated successfully');
      } catch (error) {
        console.error('Failed to update profile:', error);
        alert('Failed to update profile');
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