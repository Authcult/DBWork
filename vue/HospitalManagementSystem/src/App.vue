<template>
  <div id="app">
    <div class="app-header">
      <span class="title">医院管理系统</span>
      <el-button v-if="isAuthenticated" type="danger" size="small" @click="handleLogout">
        退出登录
      </el-button>
    </div>
    <router-view></router-view>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { ref, onMounted } from 'vue';

const router = useRouter();
const isAuthenticated = ref(false);

onMounted(() => {
  isAuthenticated.value = !!localStorage.getItem('token');
});

const handleLogout = async () => {
  try {
    const token = localStorage.getItem('token');
    if (!token) return;

    await fetch('http://your-api-domain.com/auth/logout', {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    // 无论接口是否成功，都清除本地状态
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    localStorage.removeItem('isAuthenticated');

    ElMessage.success('已退出登录');
    router.push('/login');
  } catch (err) {
    console.error(err);
    ElMessage.error('退出失败');
  }
};
</script>

<style>
html, body, #app {
  height: 100vh;
  width: 100vw;
  margin: 0;
  padding: 0;
}

#app {
  background-image: url('/images/background.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  flex-direction: column;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  color: #2c3e50;
}

.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 24px;
  background-color: rgba(255, 255, 255, 0.9);
}

.title {
  font-size: 20px;
  font-weight: bold;
}
</style>
