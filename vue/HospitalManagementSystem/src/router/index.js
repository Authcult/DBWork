import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import AdminView from '../views/AdminView.vue';
import DoctorView from '../views/DoctorView.vue';
import PatientView from '../views/PatientView.vue';
import RegisterView from '@/views/RegisterView.vue';

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/admin',
    name: 'Admin',
    component: AdminView,
    // TODO: Add route guard for admin role
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path: '/doctor',
    name: 'Doctor',
    component: DoctorView,
    // TODO: Add route guard for doctor role
    meta: { requiresAuth: true, role: 'doctor' }
  },
  {
    path: '/patient',
    name: 'Patient',
    component: PatientView,
    // TODO: Add route guard for patient role
    meta: { requiresAuth: true, role: 'patient' }
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterView
  },
  {
    path: '/admin/departments',
    name: 'DepartmentManagement',
    component: () => import('@/views/admin/DepartmentManagement.vue'),
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path: '/admin/doctors',
    name: 'DoctorManagement',
    component: () => import('@/views/admin/DoctorManagement.vue'),
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path: '/admin/schedules',
    name: 'ScheduleManagement',
    component: () => import('@/views/admin/ScheduleManagement.vue'),
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path: '/admin/drugs',
    name: 'DrugManagement',
    component: () => import('@/views/admin/DrugManagement.vue'),
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path:'/admin/wards',
    name:'WardsManagement',
    component:()=>import('@/views/admin/WardsManagement.vue'),
    meta: { requiresAuth: true, role: 'admin' }
  } 
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

// TODO: Implement navigation guards for authentication and role-based access
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true';
  const userRole = localStorage.getItem('userRole');

  if (to.meta.requiresAuth) {
    if (!isAuthenticated) {
      // 未登录将跳转到登录页
      next({ name: 'Login' });
    } else if (to.meta.role && to.meta.role !== userRole) {
      // 登录了但角色不匹配
      alert('无权限访问该页面');
      // 跳转到当前用户的角色首页，避免无限跳转
      switch (userRole) {
        case 'admin':
          next({ name: 'Admin' });
          break;
        case 'doctor':
          next({ name: 'Doctor' });
          break;
        case 'patient':
          next({ name: 'Patient' });
          break;
        default:
          next({ name: 'Login' });
      }
    } else {
      // 登录且角色匹配
      next();
    }
  } else {
    if (to.name === 'Login' && isAuthenticated) {
      // 已登录用户访问登录页，重定向到角色首页
      switch (userRole) {
        case 'admin':
          next({ name: 'Admin' });
          break;
        case 'doctor':
          next({ name: 'Doctor' });
          break;
        case 'patient':
          next({ name: 'Patient' });
          break;
        default:
          next(); // 角色未知，允许访问登录页
      }
    } else {
      // 其他无需验证的页面，直接放行
      next();
    }
  }
});


export default router;