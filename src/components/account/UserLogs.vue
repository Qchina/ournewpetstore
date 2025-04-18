<template>
  <div class="logs-container">
    <div class="auth-form">
      <!-- 将标题和按钮放在一个容器里 -->
      <div class="header-container">
        <h2>{{ username }}_log</h2>
        <button type="button" @click="returnToMyAccount" class="return-button">Return to My Account</button>
      </div>
      <form class="form-container">
        <!-- 添加表头 -->
        <div class="form-group header-group">
          <div class="col">Time</div>
          <div class="col">History</div>
        </div>
        <div v-for="log in logs" :key="log.time + log.behavior" class="form-group log-item">
          <div class="col">{{ log.time }}</div>
          <div class="col">{{ log.behavior }}</div>
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
      username: '',
      logs: []
    };
  },
  async created() {
    await Promise.all([this.fetchUsername(), this.fetchLogs()]);
  },
  methods: {
    async fetchUsername() {
      try {
        const response = await axios.get('/api/user/info');
        this.username = response.data.username;
      } catch (error) {
        console.error('Failed to fetch username:', error);
      }
    },
    async fetchLogs() {
      try {
        const response = await axios.get('/api/user/logs');
        this.logs = response.data;
      } catch (error) {
        console.error('Failed to fetch logs:', error);
      }
    },
    returnToMyAccount() {
      this.$emit('switch-to-userProfile');
    }
  }
};
</script>

<style scoped>
.logs-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 30px;
}

.logs-container::before {
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
  max-width: 900px;
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

/* 新增标题和按钮容器样式 */
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  gap: 20px; /* 添加间距，可以根据需求调整数值 */
}

h2 {
  text-align: left; /* 修改标题对齐方式 */
  margin-bottom: 0; /* 移除底部外边距 */
  font-size: 20px;
}

/* 调整返回按钮样式 */
.return-button {
  width: auto;
  padding: 8px 16px;
  font-size: 14px;
}

.form-container {
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
}
button {
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
}

button:hover {
  background-color: #0b7dda;
  transform: scale(1.02);
}

.form-group {
  display: flex;
  border-bottom: 1px solid #e0e0e0;
}

.form-group:last-child {
  border-bottom: none;
}

.header-group {
  background-color: #f5f5f5;
  font-weight: bold;
}

.col {
  flex: 1;
  padding: 12px;
  word-break: break-word;
  border-right: 1px solid #e0e0e0; /* 添加右侧边框作为分割线 */
}

.col:last-child {
  border-right: none; /* 移除最后一列的右侧边框 */
}

.log-item:nth-child(even) {
  background-color: #fafafa;
}

.log-item:hover {
  background-color: #f0f0f0;
  transition: background-color 0.3s ease;
}
</style>