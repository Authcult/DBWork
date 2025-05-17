<template>
    <div class="doctor-page">
      <div class="toolbar">
        <el-form :model="form" inline>
          <el-form-item label="医生姓名">
            <el-input v-model="form.name" placeholder="请输入医生姓名" />
          </el-form-item>
          <el-form-item label="所属科室">
            <el-select v-model="form.departmentId" placeholder="选择科室" style="width: 200px">
              <el-option
                v-for="dept in departments"
                :key="dept.departmentId"
                :label="dept.name"
                :value="dept.departmentId"
              />
            </el-select>
          </el-form-item>
          <el-button type="primary" @click="handleAdd">新增医生</el-button>
          <el-button @click="goBack" type="default">返回主页</el-button>
        </el-form>
      </div>
  
      <el-table :data="doctors" style="width: 100%" stripe height="calc(100vh - 180px)">
        <el-table-column prop="doctorId" label="ID" width="80" />
        <el-table-column prop="name" label="医生姓名" />
        <el-table-column prop="departmentName" label="所属科室" />
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.doctorId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 编辑弹窗 -->
      <el-dialog title="编辑医生" v-model="editDialogVisible">
        <el-form :model="editForm" label-width="80px">
          <el-form-item label="医生姓名">
            <el-input v-model="editForm.name" />
          </el-form-item>
          <el-form-item label="所属科室">
            <el-select v-model="editForm.departmentId" placeholder="选择科室">
              <el-option
                v-for="dept in departments"
                :key="dept.departmentId"
                :label="dept.name"
                :value="dept.departmentId"
              />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">保存</el-button>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import {
    getDoctorList,
    addDoctor,
    updateDoctor,
    deleteDoctor
  } from '@/api/admin/doctor'
  import { getDepartmentList } from '@/api/admin/department'
  import { ElMessage, ElMessageBox } from 'element-plus'
  
  const router = useRouter()
  
  const form = ref({
    name: '',
    departmentId: null
  })
  
  const doctors = ref([])
  const departments = ref([])
  
  const editDialogVisible = ref(false)
  const editForm = ref({
    doctorId: null,
    name: '',
    departmentId: null
  })
  
  const fetchDepartments = async () => {
    const res = await getDepartmentList()
    departments.value = res.data?.data || res.data?.items || []
  }
  
  const fetchDoctors = async () => {
  try {
    const res = await getDoctorList()
    console.log('医生接口返回：', res.data)
    const data = res.data?.data || res.data?.items || res.data || []
    if (!Array.isArray(data)) {
      ElMessage.error('医生数据格式错误！')
      return
    }
    doctors.value = data
  } catch (err) {
    console.error('获取医生数据失败:', err)
    ElMessage.error('加载医生列表失败')
  }
}
  
  const handleAdd = async () => {
    if (!form.value.name) return ElMessage.warning('请输入医生姓名')
    if (!form.value.departmentId) return ElMessage.warning('请选择所属科室')
  
    const res = await addDoctor(form.value)
    ElMessage.success(res.data?.message || '添加成功')
    form.value.name = ''
    form.value.departmentId = null
    fetchDoctors()
  }
  
  const handleEdit = (row) => {
    editForm.value = { ...row }
    editDialogVisible.value = true
  }
  
  const submitEdit = async () => {
    const { doctorId, name, departmentId } = editForm.value
    if (!name) return ElMessage.warning('请输入医生姓名')
    if (!departmentId) return ElMessage.warning('请选择所属科室')
  
    await updateDoctor(doctorId, { name, departmentId })
    ElMessage.success('更新成功')
    editDialogVisible.value = false
    fetchDoctors()
  }
  
  const handleDelete = (id) => {
    ElMessageBox.confirm('确认删除该医生？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      center: true
    }).then(async () => {
      await deleteDoctor(id)
      ElMessage.success('删除成功')
      fetchDoctors()
    })
  }
  
  const goBack = () => {
    router.push('/admin')
  }
  
  onMounted(() => {
    fetchDepartments()
    fetchDoctors()
  })
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
  .header-bar {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    gap: 20px;
    flex-wrap: wrap;
  }
  </style>
  