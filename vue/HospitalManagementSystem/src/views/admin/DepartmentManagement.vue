<template>
    <div class="department-page">
      <!-- 顶部操作区 -->
      <div class="toolbar">
        <el-form :model="form" inline>
          <el-form-item label="科室名称">
            <el-input v-model="form.name" placeholder="请输入科室名称" />
          </el-form-item>
          <el-button type="primary" @click="handleAdd">新增科室</el-button>
          <el-button @click="goBack" type="default">返回主页</el-button>
        </el-form>
      </div>
  
      <!-- 科室表格 -->
      <el-table :data="departments" style="width: 100%; flex: 1; overflow-y: auto;">
        <el-table-column prop="departmentId" label="ID" width="80" />
        <el-table-column prop="name" label="科室名称" />
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.departmentId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 编辑弹窗 -->
      <el-dialog title="编辑科室" v-model="editDialogVisible">
        <el-form :model="editForm">
          <el-form-item label="科室名称">
            <el-input v-model="editForm.name" />
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
    getDepartmentList,
    addDepartment,
    updateDepartment,
    deleteDepartment
  } from '@/api/admin/department'
  import { ElMessage, ElMessageBox } from 'element-plus'
  
  const router = useRouter()
  
  const form = ref({ name: '' })
  const departments = ref([])
  
  const editDialogVisible = ref(false)
  const editForm = ref({ departmentId: null, name: '' })
  
  const fetchDepartments = async () => {
    const res = await getDepartmentList()
    departments.value = res.data?.data || res.data?.items || []
  }
  
  const handleAdd = async () => {
    if (!form.value.name) {
        return ElMessage.warning('请输入科室名称')
    }
    try {
        const res = await addDepartment(form.value)
        console.log('新增科室接口返回:', res)
        if (res.success) { // 返回有效数据就成功
            ElMessage.success(res.message || '添加成功')
            form.value.name = ''
            fetchDepartments()
        } else {
            ElMessage.error(res.message || '添加失败')
        }
    } catch (error) {
        console.error('新增科室出错:', error)
        ElMessage.error('添加失败，请重试')
    }
  }
  
  const handleEdit = (row) => {
    editForm.value = { ...row }
    editDialogVisible.value = true
  }
  
  const submitEdit = async () => {
    const { departmentId, name } = editForm.value
    await updateDepartment(departmentId, { name })
    ElMessage.success('更新成功')
    editDialogVisible.value = false
    fetchDepartments()
  }
  
  const handleDelete = (id) => {
    ElMessageBox.confirm('确认删除该科室？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      center: true
    }).then(async () => {
      await deleteDepartment(id)
      ElMessage.success('删除成功')
      fetchDepartments()
    })
  }
  
  const goBack = () => {
    router.push('/admin')
  }
  
  onMounted(() => {
    fetchDepartments()
  })
  </script>
  
  <style scoped>
  .department-page {
    display: flex;
    flex-direction: column;
    height: 100vh; /* 占满全屏视图 */
    padding: 20px;
    box-sizing: border-box;
    background-color: #f5f7fa;
  }
  
  .toolbar {
    margin-bottom: 16px;
  }
  </style>
  