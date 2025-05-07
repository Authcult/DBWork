import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import AdminView from '../views/AdminView.vue';
import DoctorView from '../views/DoctorView.vue';
import PatientView from '../views/PatientView.vue';

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
    // This is a protected route
    // TODO: Replace with actual authentication check
    if (!isAuthenticated) {
      // User is not authenticated, redirect to login
      next({ name: 'Login' });
    } else {
      // User is authenticated, check role if specified
      if (to.meta.role && to.meta.role !== userRole) {
        // User does not have the required role, redirect to an appropriate page or show an error
        // For now, let's redirect to login, but a dedicated 'Unauthorized' page would be better.
        alert('Access Denied!'); // Simple alert for now
        next({ name: 'Login' }); // Or redirect to a relevant dashboard if already logged in with a different role
      } else {
        next(); // Proceed to route
      }
    }
  } else {
    // Route does not require auth
    if (to.name === 'Login' && isAuthenticated) {
      // If user is authenticated and tries to go to Login page,
      // redirect them to their dashboard if their role is known.
      if (userRole === 'admin') {
        next({ path: '/admin' });
      } else if (userRole === 'doctor') {
        next({ path: '/doctor' });
      } else if (userRole === 'patient') {
        next({ path: '/patient' });
      } else {
        // Authenticated, but role is unknown or not set.
        // Allow proceeding to the login page (or current non-auth page).
        next();
      }
    } else {
      next(); // Proceed for other non-auth routes, or if not (on Login and authenticated)
    }
  }
});

export default router;