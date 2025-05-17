<template>
    <div class="schedule-page">
      <div class="toolbar">
        <el-form :model="form" inline>
          <el-form-item label="医生">
            <el-select v-model="form.doctorId" placeholder="选择医生" style="width: 200px" @change="handleDoctorChange">
                <el-option
                    v-for="doc in doctors"
                    :key="doc.doctorId"
                    :label="doc.name"
                    :value="doc.doctorId"
                />
            </el-select>
          </el-form-item>
          <el-form-item label="类型">
            <el-select v-model="form.workType" placeholder="选择类型" style="width: 120px">
              <el-option label="门诊" value="门诊" />
              <el-option label="住院巡诊" value="住院巡诊" />
            </el-select>
          </el-form-item>
          <el-form-item label="开始时间">
            <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" />
          </el-form-item>
          <el-form-item label="结束时间">
            <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" />
          </el-form-item>
          <el-button type="primary" @click="handleAdd">新增排班</el-button>
          <el-button @click="goBack" type="default">返回主页</el-button>
        </el-form>
      </div>
  
      <el-table :data="schedules" style="width: 100%" stripe height="calc(100vh - 180px)">
        <el-table-column prop="scheduleId" label="排班ID" width="100" />
        <el-table-column prop="doctorName" label="医生" />
        <el-table-column prop="departmentName" label="科室" />
        <el-table-column prop="workType" label="类型" width="100" />
        <el-table-column prop="startTime" label="开始时间" />
        <el-table-column prop="endTime" label="结束时间" />
        <el-table-column label="操作" width="180">
            <template #default="scope">
            <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.scheduleId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-dialog title="编辑排班" v-model="editDialogVisible">
        <el-form :model="editForm" label-width="80px">
          <el-form-item label="类型">
            <el-select v-model="editForm.workType">
              <el-option label="门诊" value="门诊" />
              <el-option label="住院巡诊" value="住院巡诊" />
            </el-select>
          </el-form-item>
          <el-form-item label="开始时间">
            <el-date-picker v-model="editForm.startTime" type="datetime" />
          </el-form-item>
          <el-form-item label="结束时间">
            <el-date-picker v-model="editForm.endTime" type="datetime" />
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
  import dayjs from 'dayjs'
  import { useRouter } from 'vue-router'
  import {
    getDoctorList
  } from '@/api/admin/doctor'
  import {
    getSchedule,
    getScheduleList,
    createSchedule,
    updateSchedule,
    deleteSchedule
  } from '@/api/admin/schedule'
  import { ElMessage, ElMessageBox } from 'element-plus'
  
  const router = useRouter()
  
  const form = ref({
    doctorId: null,
    workType: '',
    startTime: '',
    endTime: ''
  })
  
  const doctors = ref([])
  const schedules = ref([])
  
  const editDialogVisible = ref(false)
  const editForm = ref({
    scheduleId: null,
    workType: '',
    startTime: '',
    endTime: ''
  })

  
  
  const fetchDoctors = async () => {
    try {
        const res = await getDoctorList()
        console.log('fetchDoctors 响应:', res)
        doctors.value = res.data?.items.map(doc => ({
            doctorId: doc.doctorID, 
            name: doc.name
        })) || []
    } catch (error) {
        console.error('fetchDoctors 报错:', error)
    }
}
  
  const fetchSchedules = async () => {
    try {
        let res
        if (form.value.doctorId) {
            res = await getSchedule(form.value.doctorId)
            console.log('fetchSchedules doctorId 响应:', res)
            schedules.value = (res.data?.schedules || []).map(item => ({
                scheduleId: item.scheduleID,
                doctorName: item.doctor.name,
                departmentName: item.doctor.department.name,
                workType: item.workType,
                startTime: dayjs(item.startTime).format('YYYY-MM-DD HH:mm'),
                endTime: dayjs(item.endTime).format('YYYY-MM-DD HH:mm')
            }))
        } else {
            res = await getScheduleList()
            console.log('fetchSchedules all 响应:', res)
            const scheduleList = res.data?.schedules || [] 
            console.log('scheduleList:', scheduleList)
            schedules.value = scheduleList.map(item => ({
                scheduleId: item.scheduleID,
                doctorName: item.doctor.name,
                departmentName: item.doctor.department.name,
                workType: item.workType,
                startTime: dayjs(item.startTime).format('YYYY-MM-DD HH:mm'),
                endTime: dayjs(item.endTime).format('YYYY-MM-DD HH:mm') 
            }))
        }
    } catch (error) {
        console.error('fetchSchedules error:', error)
    }
  }
  
  const handleAdd = async () => {
    const { doctorId, workType, startTime, endTime } = form.value
    if (!doctorId || !workType || !startTime || !endTime) {
      return ElMessage.warning('请填写完整排班信息')
    }
    await createSchedule(doctorId, {
      workType,
      startTime,
      endTime
    })
    ElMessage.success('排班添加成功')
    fetchSchedules()
  }
  
  const handleEdit = (row) => {
    editForm.value = {
        scheduleId: row.scheduleId,
        workType: row.workType,
        startTime: new Date(row.startTime), 
        endTime: new Date(row.endTime)
    }
    editDialogVisible.value = true
   }

   const handleDoctorChange = (doctorId) => {
    console.log('选择了医生ID:', doctorId, '类型:', typeof doctorId)
    fetchSchedules()
   }
  
  const submitEdit = async () => {
    const { scheduleId, workType, startTime, endTime } = editForm.value
    await updateSchedule(scheduleId, { workType, startTime, endTime })
    ElMessage.success('排班更新成功')
    editDialogVisible.value = false
    fetchSchedules()
  }
  
  const handleDelete = (id) => {
    ElMessageBox.confirm('确定删除该排班记录吗？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    }).then(async () => {
      await deleteSchedule(id)
      ElMessage.success('删除成功')
      fetchSchedules()
    })
  }
  
  const goBack = () => {
    router.push('/admin')
  }
  
  onMounted(() => {
    fetchDoctors()
    fetchSchedules()
  })
  </script>
  
  <style scoped>
  .schedule-page {
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
    align-items: center;
    gap: 20px;
    flex-wrap: wrap;
  }
  </style>
  