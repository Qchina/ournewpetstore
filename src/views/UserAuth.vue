<template>
  <Header />
  <div class="auth-container">
    <div class="auth-background"></div>
    <UserLogin v-if="showLogin" @switch-to-register="showLogin = false" class="auth-content" />
    <UserRegister v-else @switch-to-login="showLogin = true" class="auth-content" />
  </div>
</template>

<script>
import Header from '@/components/CommonTop.vue'
import UserLogin from '@/components/account/UserLogin.vue'
import UserRegister from '@/components/account/UserRegister.vue'

export default {
  components: {
    Header,
    UserLogin,
    UserRegister
  },
  data() {
    return {
      showLogin: true
    }
  }
}
</script>

<style scoped>
.auth-container {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
  display: flex; /* 新增 */
  justify-content: center; /* 新增 */
  align-items: center; /* 新增 */
}

.auth-background {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url('@/assets/login-bg.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  z-index: 0;
}

.auth-background::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.3);
}
.auth-content {
  position: relative;
  z-index: 1;
  max-width: 750px; /* 新增 */
  margin: 0 auto; /* 新增 */
}
</style>