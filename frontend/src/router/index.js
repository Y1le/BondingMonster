import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import EnglistDanceGameView from '../views/EnglistDanceGameView.vue'
import ChatRoomView from '../views/ChatRoomView.vue'
import LoginView from '../views/LoginView.vue'
import NotFoundView from '../views/NotFoundView.vue'
import store from '../store'
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta:{
      requestAuth: false,
    }
  },
  {
    path: '/EnglistDanceGame',
    name: 'englistdancegame',
    component: EnglistDanceGameView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path: '/chatroom',
    name: 'chatroom',
    component: ChatRoomView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta:{
      requestAuth: false,
    }
  },
  {
    path: '/register',
    name:'register',
    component: () => import('../views/RegisterView.vue'),
    meta:{
      requestAuth: false,
    }
  },
  {
    path: '/404',
    name: '404',
    component: NotFoundView,
    meta:{
      requestAuth: false,
    }
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

router.beforeEach((to, from, next) => {
  if (to.meta.requestAuth && !store.state.user.is_login) {
    next({ name: "login"})
  } else {
    next()
  }
});

export default router
