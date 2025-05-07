# 需求表（ai生成有问题就改）

## 🩺 医生端（Doctor）

### 1. 医生登录

* **路径**：`/doctor/login`
* **字段**：工号、密码
* **校验**：非空；密码 MD5/BCrypt 校验

---

### 2. 医生首页（Dashboard）

* **路径**：`/doctor/dashboard`
* **功能板块**：

  1. **今日排班**

     * 展示时间段、地点、类型（门诊/巡诊）
     * 未签到时显示“签到”按钮
  2. **待接诊病人**

     * 挂号队列（按挂号时间升序）
     * 点击“开始诊疗”进入详情
  3. **住院病人列表**

     * 自己负责的住院档案列表
     * 显示：病人姓名、床位号、入院日期、最近一次记录日期
  4. **消息/通知**

     * 药房库存预警、系统公告

---

### 3. 门诊接诊流程

#### 3.1 挂号列表

* **路径**：`/doctor/outpatient/queue`
* **展示字段**：挂号单号、病人姓名、性别、年龄、挂号时间、科室
* **操作**：

  * “查看详情”→`/doctor/outpatient/{registrationId}`
  * “取消诊疗”→调用 `DELETE /api/registration/{id}`

#### 3.2 诊疗详情

* **路径**：`/doctor/outpatient/{registrationId}`
* **显示**：

  * 病人档案（基本信息、过敏史）
  * 本次主诉输入框（`/api/prescriptions` POST 参数 `symptomDescription`）
* **处方录入**：

  * 药品检索输入框（模糊匹配 `/api/drugs?keyword=`）
  * 选中药品后填写数量、用法
  * 动态计算药品小计 + 诊疗费（根据职称 `/api/doctor/{id}` 拿职称费率）
* **提交**：

  * “保存处方”→POST `/api/prescriptions` + `/api/prescriptionItems[]`
  * 返回处方单号、缴费链接

#### 3.3 缴费提醒

* 处方保存后自动推送给病人端消息，显示缴费二维码或链接

---

### 4. 住院巡诊流程

#### 4.1 病人列表

* **路径**：`/doctor/hospitalization/patients`
* **展示**：床号、姓名、入院时间、昨日记录简述
* **搜索/筛选**：按病房、姓名、入院时间范围

#### 4.2 每日病历录入

* **路径**：`/doctor/hospitalization/{recordId}`
* **显示**：

  * 病人档案（诊断、过敏等）
  * 历史每日记录表格
* **新增记录**：

  * 日期（默认当天，不可重复）
  * 病情描述富文本编辑器
  * 治疗方案（文字描述 + 关联处方下拉）
  * “提交”→POST `/api/hospitalization/record`

---

### 5. 排班管理

* **路径**：`/doctor/schedule`
* **展示**：月/周视图，按颜色区分门诊/巡诊
* **操作**：

  * “申请调班”→填写原时段、新时段、理由 → POST `/api/schedule/request`
  * 查看审批状态

---

### 6. 个人中心

* **路径**：`/doctor/profile`
* **功能**：

  * 修改密码、电话
  * 查看历史接诊量统计（柱状图，后端接口 `/api/statistics/doctor/{id}`）

---

## 🧑‍⚕️ 病人端（Patient）

### 1. 病人登录/注册

* **登录路径**：`/patient/login`（用户名+密码）
* **注册路径**：`/patient/register`（姓名、性别、电话、地址、用户名、密码）
* **校验**：手机号格式、密码长度 ≥ 6

---

### 2. 病人首页（Dashboard）

* **功能板块**：

  1. **预约/挂号入口**
  2. **待缴费处方**
  3. **历史就诊记录**
  4. **住院记录查询**
  5. **消息提醒**

---

### 3. 挂号预约

#### 3.1 选择科室与医生

* **路径**：`/patient/register`
* **步骤**：

  1. 选择科室（下拉 `/api/departments`）
  2. 查看该科医生列表（GET `/api/doctors?deptId=`）
  3. 选择医生 & 时间段（GET `/api/schedule?doctorId=`）

#### 3.2 确认挂号

* **显示**：科室、医生、时间、挂号费
* **操作**：确定→POST `/api/registration`

---

### 4. 处方缴费

#### 4.1 待缴费列表

* **路径**：`/patient/prescriptions?status=pending`
* **字段**：处方单号、开具时间、诊疗费、药费合计、总计

#### 4.2 缴费

* **操作**：

  * “去缴费”→调用 `/api/payments` POST `{ prescriptionId, amount }`
  * 支付成功后进入“取药码”页面

#### 4.3 历史处方

* **路径**：`/patient/prescriptions?status=paid`
* **功能**：查看详情、再次打印、在线评价（星级+文字）

---

### 5. 住院服务

#### 5.1 住院申请

* **路径**：`/patient/hospitalization/apply`
* **步骤**：

  1. 选择入院科室
  2. 选择或动态分配床位（GET `/api/beds?wardId=`）
  3. 预缴押金（POST `/api/payments`）

#### 5.2 住院记录查询

* **路径**：`/patient/hospitalizations`
* **展示**：每次住院档案列表（入院/出院时间、床位号、主治医师）
* **点击查看**：进入 `GET /api/hospitalization/{id}`

  * 查看每日记录列表（日期、病情、方案）
  * 可下载 PDF 病历

---

### 6. 个人中心

* **路径**：`/patient/profile`
* **功能**：

  * 修改个人信息  /api/users/me PUT
  * 修改密码
  * 查看消息通知（缴费提醒、排队提醒、系统公告）

---

> **数据交互约定**
>
> * 全部接口均需携带 `Authorization: Bearer <token>`
> * 请求/响应均为 JSON
> * 错误统一格式：`{ code, message }`

