<template>
  <div class="logs-container">
    <div class="auth-form">
      <div class="header-container">
        <h2>{{ username }}_log</h2>
        <button type="button" @click="returnToMyAccount" class="return-button">Return to My Account</button>
      </div>
      <!-- 修改表格容器结构 -->
      <div class="form-container">
        <div class="header-group">
          <div class="col">Time</div>
          <div class="col">History</div>
        </div>
        <div class="logs-scroll-container">
          <div v-for="log in logs" :key="log.time + log.behavior" class="log-item">
            <div class="col">{{ log.time }}</div>
            <div class="col">{{ log.behavior }}</div>
          </div>
        </div>
      </div>
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
        // 从localStorage获取用户信息
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        this.username = user.username || '';
      } catch (error) {
        console.error('获取用户名失败:', error);
      }
    },
    async fetchLogs() {
      try {
        const response = await axios.get('http://localhost:9090/api/v1/logs', {
          withCredentials: true
        });
        console.log('日志响应数据:', response); // 添加调试日志
        
        // 直接使用响应数据，因为后端直接返回日志列表
        if (Array.isArray(response.data)) {
          this.logs = response.data.map(log => ({
            time: log.time,
            behavior: log.behavior
          }));
        } else {
          console.error('获取到的日志数据格式不正确');
          this.logs = [];
        }
      } catch (error) {
        if (error.response?.status === 401) {
          console.error('用户未登录');
          this.$router.push('/userauth');
        } else {
          console.error('获取日志失败:', error);
        }
        this.logs = [];
      }
    },
    returnToMyAccount() {
      this.$emit('switch-to-userProfile');
    }
  }
}
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

.form-container {
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
  max-height: 500px; /* 设置最大高度 */
  display: flex;
  flex-direction: column;
}

.header-group {
  background-color: #f5f5f5;
  font-weight: bold;
  display: flex;
  position: sticky;
  top: 0;
  z-index: 1;
  border-bottom: 2px solid #e0e0e0;
}

.logs-scroll-container {
  overflow-y: auto;
  flex-grow: 1;
}

.log-item {
  display: flex;
  border-bottom: 1px solid #e0e0e0;
}

/* 添加滚动条样式 */
.logs-scroll-container::-webkit-scrollbar {
  width: 8px;
}

.logs-scroll-container::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.logs-scroll-container::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.logs-scroll-container::-webkit-scrollbar-thumb:hover {
  background: #555;
}

</style>