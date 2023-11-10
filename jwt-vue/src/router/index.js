import { createRouter, createWebHistory } from 'vue-router'
import MainView from '@/views/MainView.vue'

import UserView from '@/views/UserView.vue'
import LoginComponent from '@/components/users/LoginComponent.vue'
import MyPageComponent from '@/components/users/MyPageComponent.vue'
import SignUpComponent from '@/components/users/SignUpComponent.vue'

import BoardView from '@/views/BoardView.vue'
import BoardListComponent from '@/components/boards/BoardListComponent.vue'
import BoardWriteComponent from '@/components/boards/BoardWriteComponent.vue'
import BoardDetailComponent from '@/components/boards/BoardDetailComponent.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: MainView
    },
    {
      path: '/user',
      name: 'user',
      component: UserView,
      children: [
        {
          path: 'login',
          name: 'login',
          component: LoginComponent,
        },
        {
          path: 'signUp',
          name: 'signUp',
          component: SignUpComponent,
        },
        {
          path: 'myPage',
          name: 'myPage',
          component: MyPageComponent,
        },
      ]
    },
    {
      path: '/board',
      name: 'board',
      component: BoardView,
      children: [
        {
          path: 'list',
          name: 'list',
          component: BoardListComponent,
        },
        {
          path: 'write',
          name: 'write',
          component: BoardWriteComponent,
        },
        {
          path: 'detail',
          name: 'detail',
          component: BoardDetailComponent,
        },
      ]
    },
  ]
})

export default router
