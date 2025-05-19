<template>
    <div class="hospitalization-detail-page">
        <!-- 返回主页按钮 -->
        <div class="toolbar">
            <el-button type="danger" @click="handleDischarge">办理出院</el-button>
            <el-button @click="goBack">返回主页</el-button>
        </div>
        <el-card>
            <template #header>
                <div class="flex justify-between items-center">
                    <span>住院详情 - {{ detail?.patientName }}</span>

                </div>
            </template>

            <div>病人姓名：{{ detail?.patientName }}</div>
            <div>病房位置：{{ detail?.wardLocation }}</div>
            <div>病床号：{{ detail?.bedNumber }}</div>
            <div>入院时间：{{ detail?.admissionDate }}</div>

            <el-divider>每日诊疗记录</el-divider>
            <el-timeline>
                <el-timeline-item
                    v-for="record in detail?.dailyRecords"
                    :key="record.dailyRecordId"
                    :timestamp="record.date"
                    placement="top"
                >
                    {{ record.treatmentPlan }}
                </el-timeline-item>
            </el-timeline>

            <el-form :model="form" class="mt-4">
                <el-form-item label="今日诊疗方案">
                    <el-input v-model="form.treatmentPlan" type="textarea" rows="3" />
                </el-form-item>
                <el-button type="primary" @click="addRecord">添加记录</el-button>
            </el-form>
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
import { useRoute, useRouter } from 'vue-router'
import { getHospitalizationDetail, addDailyRecord, dischargePatient } from '@/api/doctor/hospitalization'
import { ElMessageBox, ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const detail = ref(null)
const form = ref({ treatmentPlan: '' })

const fetchDetail = async () => {
    const res = await getHospitalizationDetail(route.params.recordId)
    if (res.success) {
        detail.value = res.data
    }
}

const addRecord = async () => {
    if (!form.value.treatmentPlan) return ElMessage.warning('请填写治疗方案')
    const res = await addDailyRecord(route.params.recordId, form.value)
    console.log("添加记录回应：",res)
    if (res.success) {
        ElMessage.success('记录添加成功')
        form.value.treatmentPlan = ''
        fetchDetail()
    }
}

const handleDischarge = () => {
    ElMessageBox.confirm('确认办理出院？', '提示').then(async () => {
        const today = new Date().toISOString().split('T')[0]
        const res = await dischargePatient(route.params.recordId, { dischargeDate: today })
        console.log("出院回应：",res)
        if (res.success) {
            ElMessage.success('已办理出院')
            router.push('/doctor/inpatients')
        }
    })
}

// 返回主页方法
const goBack = () => {
    router.push('/doctor/inpatients') // 可根据实际路由配置修改
}

// 提示框相关
const dialogVisible = ref(false)
const dialogMessage = ref('')
const showDialog = (message) => {
    dialogMessage.value = message
    dialogVisible.value = true
}

onMounted(fetchDetail)
</script>

<style scoped>
.hospitalization-detail-page {
    height: 100vh;
    display: flex;
    flex-direction: column;
    padding: 20px;
    box-sizing: border-box;
    background-color: #f5f7fa; /* 参考 DrugManagement.vue 常见背景色 */
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
}
</style>