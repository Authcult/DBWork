<template>
    <div class="patient-profile-page">
      <!-- 返回主页按钮 -->
      <div class="toolbar">
        <el-button @click="goBack">返回主页</el-button>
      </div>
      <el-card>
        <template #header>
          <div class="card-header">
            <span>个人信息与密码修改</span>
          </div>
        </template>
        <div>
          <el-card>
            <template #header>
              <div class="card-header">
                <span>个人信息</span>
                <el-button type="primary" @click="editing = true" v-if="!editing">编辑</el-button>
                <el-button type="success" @click="submitProfile" v-if="editing">保存</el-button>
              </div>
            </template>
            <el-form :model="profile" label-width="100px">
              <el-form-item label="用户名">
                <el-input v-model="profile.username" disabled />
              </el-form-item>
              <el-form-item label="姓名">
                <el-input v-model="profile.name" :disabled="!editing" />
              </el-form-item>
              <el-form-item label="性别">
                <el-input v-model="profile.gender" disabled />
              </el-form-item>
              <el-form-item label="地址">
                <el-input v-model="profile.address" :disabled="!editing" />
              </el-form-item>
              <el-form-item label="手机号">
                <el-input v-model="profile.phone" :disabled="!editing" />
              </el-form-item>
            </el-form>
          </el-card>
  
          <el-card class="password-card" style="margin-top: 20px">
            <template #header>
              修改密码
            </template>
            <el-form :model="passwordForm" label-width="100px">
              <el-form-item label="旧密码">
                <el-input v-model="passwordForm.currentPassword" type="password" />
              </el-form-item>
              <el-form-item label="新密码">
                <el-input v-model="passwordForm.newPassword" type="password" />
              </el-form-item>
              <el-form-item label="确认新密码">
                <el-input v-model="passwordForm.confirmPassword" type="password" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitPassword">修改密码</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-card>
      <!-- 提示框 -->
      <el-dialog v-model="dialogVisible" title="提示">
        <p>{{ dialogMessage }}</p>
        <template #footer>
          <el-button @click="dialogVisible = false">关闭</el-button>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { ElMessage } from 'element-plus'
  import { getPatientProfile, updatePatientProfile, changePatientPassword } from '@/api/patient/profile'
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  const profile = ref({})
  const passwordForm = ref({ currentPassword: '', newPassword: '', confirmPassword: '' })
  const editing = ref(false)
  
  onMounted(() => {
    getPatientProfile().then(res => {
      if (res.success) {
        profile.value = res.data
      }
    })
  })
  
  const submitProfile = () => {
    updatePatientProfile(profile.value).then(res => {
      if (res.success) {
        ElMessage.success('信息更新成功')
        editing.value = false
      } else {
        ElMessage.error('信息更新失败')
      }
    })
  }

  const submitPassword = () => {
    if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
      ElMessage.error('两次输入的新密码不一致')
      return
    }

    // 修改为使用query参数方式调用API
    changePatientPassword({}, {
      params: {
        currentPassword: passwordForm.value.currentPassword,
        newPassword: passwordForm.value.newPassword
      }
    }).then(res => {
      if (res.success) {
        ElMessage.success('密码修改成功')
        passwordForm.value = { currentPassword: '', newPassword: '', confirmPassword: '' }
      } else {
        ElMessage.error('密码修改失败')
      }
    }).catch((error) => {
      console.error('修改密码请求失败:', error)
      ElMessage.error('请求失败，请稍后重试')
    })
  }
  
  // 返回主页方法
  const goBack = () => {
    router.push('/patient')
  }
  
  // 提示框相关
  const dialogVisible = ref(false)
  const dialogMessage = ref('')
  const showDialog = (message) => {
    dialogMessage.value = message
    dialogVisible.value = true
  }
  </script>
  
  <style scoped>
  .patient-profile-page {
    min-height: 100vh;
    background-color: #f5f7fa; /* 参考 DrugManagement.vue 常见背景色 */
    padding: 20px;
    display: flex;
    flex-direction: column;
  }
  
  .toolbar {
    margin-bottom: 20px;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 10px;
  }
  
  .el-card {
    flex: 1;
    overflow: auto;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
  
  .password-card {
    margin-top: 20px;
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
  }
  </style>