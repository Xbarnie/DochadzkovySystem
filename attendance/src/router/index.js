import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Administration from '../views/Administration.vue'
import AttendenceOverview from '../views/administration/AttendenceOverview.vue'
import OverviewOfApproaches from '../views/administration/OverviewOfApproaches.vue'
import DayLog from '../views/administration/DayLog.vue'
import UserAttendance from '../views/administration/UserAttendance.vue'
import Employee from '../views/Employee.vue'
import EditingHistory from '../views/administration/EditingHistory.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/administration',
    name: 'Administration',
    component: Administration,
    children: [
      {
        path: '/administration/day-log',
        name: 'DayLog',
        component: DayLog
      },
      {
        path: '/administration/attendence-overview',
        name: 'AttendenceOverview',
        component: AttendenceOverview
      },
      {
        path: '/administration/overview-approaches',
        name: 'OverviewOfApproaches',
        component: OverviewOfApproaches
      },
      {
        path: '/administration/user/:id',
        name: 'UserAttendance',
        component: UserAttendance
      },
      {
        path: '/administration/editing-history',
        name: 'EditingHistory',
        component: EditingHistory
      }
    ]
  },
  {
    path: '/employee',
    name: 'Employee',
    component: Employee
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
