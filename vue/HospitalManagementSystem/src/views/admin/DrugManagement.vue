<template>
    <div class="drug-page">
      <div class="toolbar">
        <el-form :model="form" inline>
          <el-form-item label="药品名称">
            <el-input v-model="form.name" placeholder="请输入药品名称" />
          </el-form-item>
          <el-form-item label="价格">
            <el-input v-model="form.price" placeholder="请输入价格" type="number" />
          </el-form-item>
          <el-form-item label="库存">
            <el-input v-model="form.stock" placeholder="请输入库存数量" type="number" />
          </el-form-item>
          <el-button type="primary" @click="handleAdd">新增药品</el-button>
          <el-button @click="goBack">返回主页</el-button>
        </el-form>
      </div>
  
      <el-table :data="drugs" style="width: 100%" stripe height="calc(100vh - 180px)">
        <el-table-column prop="drugId" label="ID" width="80" />
        <el-table-column prop="name" label="药品名称" />
        <el-table-column prop="price" label="价格" />
        <el-table-column prop="stock" label="库存" />
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.drugId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-dialog title="编辑药品" v-model="editDialogVisible">
        <el-form :model="editForm" label-width="80px">
          <el-form-item label="药品名称">
            <el-input v-model="editForm.name" />
          </el-form-item>
          <el-form-item label="价格">
            <el-input v-model="editForm.price" type="number" />
          </el-form-item>
          <el-form-item label="库存">
            <el-input v-model="editForm.stock" type="number" />
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
  import { getDrugList, addDrug, updateDrug, deleteDrug } from '@/api/admin/drug'
  import { ElMessage, ElMessageBox } from 'element-plus'
  
  const router = useRouter()
  
  const form = ref({
    name: '',
    price: null,
    stock: null
  })
  
  const drugs = ref([])
  
  const editDialogVisible = ref(false)
  const editForm = ref({
    drugId: null,
    name: '',
    price: null,
    stock: null
  })
  
  const fetchDrugs = async () => {
    try {
      const res = await getDrugList()
      drugs.value = res.data?.data || res.data?.items || []
    } catch (err) {
      console.error('加载药品失败', err)
      ElMessage.error('加载药品列表失败')
    }
  }
  
  const handleAdd = async () => {
    const { name, price, stock } = form.value
    if (!name) return ElMessage.warning('请输入药品名称')
    if (price == null) return ElMessage.warning('请输入价格')
    if (stock == null) return ElMessage.warning('请输入库存')
  
    const res = await addDrug(form.value)
    ElMessage.success(res.data?.message || '添加成功')
    form.value = { name: '', price: null, stock: null }
    fetchDrugs()
  }
  
  const handleEdit = (row) => {
    editForm.value = { ...row }
    editDialogVisible.value = true
  }
  
  const submitEdit = async () => {
    const { drugId, name, price, stock } = editForm.value
    if (!name) return ElMessage.warning('请输入药品名称')
    if (price == null) return ElMessage.warning('请输入价格')
    if (stock == null) return ElMessage.warning('请输入库存')
  
    await updateDrug(drugId, { name, price, stock })
    ElMessage.success('更新成功')
    editDialogVisible.value = false
    fetchDrugs()
  }
  
  const handleDelete = (id) => {
    ElMessageBox.confirm('确认删除该药品？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      center: true
    }).then(async () => {
      await deleteDrug(id)
      ElMessage.success('删除成功')
      fetchDrugs()
    })
  }
  
  const goBack = () => {
    router.push('/admin')
  }
  
  onMounted(() => {
    fetchDrugs()
  })
  </script>
  
  <style scoped>
  .drug-page {
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
  </style>