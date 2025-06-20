import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import EnglistDanceGameView from '../views/EnglistDanceGameView.vue'
import ChatRoomView from '../views/ChatRoomView.vue'
import LoginView from '../views/LoginView.vue'
import NotFoundView from '../views/NotFoundView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/EnglistDanceGame',
    name: 'englistdancegame',
    component: EnglistDanceGameView
  },
  {
    path: '/chatroom',
    name: 'chatroom',
    component: ChatRoomView
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/register',
    name:'register',
    component: () => import('../views/RegisterView.vue')
  },
  {
    path: '/404',
    name: '404',
    component: NotFoundView
  },  
  {
    path: '/:catchAll(.*)',
    redirect: "/404"
  }


]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
