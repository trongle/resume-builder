<script setup lang="ts">
// This starter template is using Vue 3 <script setup> SFCs
// Check out https://vuejs.org/api/sfc-script-setup.html#script-setup
import {ref} from "vue"
import {getUrlHash} from "./utils"
import {getUserFromLocalStorage, User} from "./User"
import RegisterPage from './pages/RegisterPage.vue'
import LoginPage from './pages/LoginPage.vue'
import ListUserPage from './pages/ListUserPage.vue'
import CreateCvPage from './pages/CreateCvPage.vue'
import ListCvPage from './pages/ListCvPage.vue'

const user = ref<User>()
const routes = ['login', 'register', 'listUser', 'createCv', 'listCv']
const hash = ref(getUrlHash())

try {
  user.value = getUserFromLocalStorage()
} catch (e) {
}

window.addEventListener('hashchange', (_: HashChangeEvent) => {
  hash.value = routes.includes(getUrlHash()) ? getUrlHash() : 'login'
})
</script>

<template>
  <register-page v-if="hash === '' || hash === 'register'"></register-page>
  <login-page v-else-if="hash === 'login'"></login-page>
  <Suspense v-else-if="hash === 'listUser' && user?.isAdmin">
    <list-user-page></list-user-page>
  </Suspense>
  <create-cv-page v-else-if="hash === 'createCv'"></create-cv-page>
  <Suspense v-else-if="hash === 'listCv'">
    <list-cv-page></list-cv-page>
  </Suspense>
</template>