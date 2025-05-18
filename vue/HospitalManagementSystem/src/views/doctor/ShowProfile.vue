<template>
    <div class="doctor-page">
      <div class="toolbar">
        <el-button type="primary" @click="editMode = !editMode">
          {{ editMode ? '取消编辑' : '编辑信息' }}
        </el-button>
        <el-button @click="goBack">返回主页</el-button>
      </div>
  
      <el-card>
        <template #header>
          <span>医生信息</span>
        </template>
  
        <el-form :model="profile" label-width="80px">
          <el-form-item label="姓名">{{ profile.name }}</el-form-item>
          <el-form-item label="性别">{{ profile.gender }}</el-form-item>
          <el-form-item label="职称">
            {{ profile.title }}
          </el-form-item>
          <el-form-item label="手机号">
            <div v-if="!editMode">{{ profile.phone }}</div>
            <el-input v-else v-model="profile.phone" />
          </el-form-item>
          <el-form-item label="科室">{{ profile.departmentName }}</el-form-item>
        </el-form>
  
        <div v-if="editMode">
          <el-button type="primary" @click="saveProfile">保存</el-button>
        </div>
      </el-card>
  
      <el-card class="mt-4">
        <template #header><span>修改密码</span></template>
        <el-form :model="passwordForm" label-width="100px" :rules="rules" ref="passwordFormRef">
          <el-form-item label="当前密码" prop="currentPassword">
            <el-input v-model="passwordForm.currentPassword" type="password" />
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="passwordForm.newPassword" type="password" />
          </el-form-item>
          <el-button type="primary" @click="submitPassword">修改密码</el-button>
        </el-form>
      </el-card>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { getDoctorProfile, updateDoctorProfile, changeDoctorPassword } from '@/api/doctor/profile'
  import { ElMessage } from 'element-plus'
  
  const router = useRouter()
  
  const profile = ref({})
  const editMode = ref(false)
  
  const passwordForm = ref({
    currentPassword: '',
    newPassword: ''
  })
  
  const rules = {
    currentPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
    newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }]
  }
  
  const passwordFormRef = ref()
  
  const fetchProfile = async () => {
    try {
      const res = await getDoctorProfile()
      if (res.success) {
        profile.value = res.data
      } else {
        ElMessage.error('获取信息失败')
      }
    } catch (e) {
      ElMessage.error('加载失败，请稍后重试')
    }
  }
  
  const saveProfile = async () => {
    try {
      const res = await updateDoctorProfile({
        phone: profile.value.phone,
        title: profile.value.title
      })
      if (res.success) {
        ElMessage.success('信息更新成功')
        editMode.value = false
        fetchProfile()
      } else {
        ElMessage.error('更新失败')
      }
    } catch (e) {
      ElMessage.error('更新出错')
    }
  }
  
  const submitPassword = () => {
  passwordFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      const res = await changeDoctorPassword({
        oldPassword: passwordForm.value.currentPassword,
        newPassword: passwordForm.value.newPassword
      })

      if (res.success) {
        ElMessage.success(res.message || '密码修改成功')
        passwordForm.value.currentPassword = ''
        passwordForm.value.newPassword = ''
      } else {
        // 处理返回错误信息
        ElMessage.error(res.error?.message || '密码修改失败')
      }
    } catch (e) {
      ElMessage.error('修改密码出错')
        }
      })
    }
  
  const goBack = () => {
    router.push('/doctor')
  }
  
  onMounted(fetchProfile)
  </script>
  
  <style scoped>
  .doctor-page {
    height: 100vh;
    display: flex;
    flex-direction: column;
    padding: 20px;
    box-sizing: border-box;
    background-color: #f5f7fa;
  }
  
  .toolbar {
    margin-bottom: 20px;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 10px;
  }
  
  .mt-4 {
    margin-top: 20px;
  }
  </style>
  