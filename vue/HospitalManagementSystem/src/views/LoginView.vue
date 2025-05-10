<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>医院管理系统 - 登录</span>
        </div>
      </template>
      <el-form @submit.prevent="handleLogin" :model="loginForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="loginForm.role" placeholder="请选择角色">
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="医生" value="doctor"></el-option>
            <el-option label="患者" value="patient"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" style="width: 100%;">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const router = useRouter();

const loginForm = ref({
  username: '',
  password: '',
  role: ''
});

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password || !loginForm.value.role) {
    ElMessage.error('请输入用户名、密码并选择角色');
    return;
  }

  try {
    const response = await fetch('http://loc/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(loginForm.value)
    });

    const res = await response.json();

    if (res.success) {
      // 保存 token 和用户信息
      localStorage.setItem('token', res.data.token);
      localStorage.setItem('userRole', res.data.user.role);
      localStorage.setItem('userName', res.data.user.name);
      ElMessage.success('登录成功');

      // 跳转到不同角色页面
      switch (res.data.user.role) {
        case 'admin':
          router.push('/admin');
          break;
        case 'doctor':
          router.push('/doctor');
          break;
        case 'patient':
          router.push('/patient');
          break;
        default:
          router.push('/');
      }
    } else {
      ElMessage.error(res.error?.message || '登录失败');
    }
  } catch (err) {
    console.error(err);
    ElMessage.error('无法连接服务器，请稍后重试');
  }
};
</script>


<style scoped>
.login-container {
  height: 100vh;
  width: 100vw;
  position: relative;
}

.login-card {
  width: 400px;
  padding: 20px;
  position: absolute;
  top: 50%;
  left: 200px;
  transform: translateY(-50%);
  background-color: white;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
}
</style>