<script setup lang="ts">
import {ref} from "vue";
import {getUserFromLocalStorage} from "../User";
import {login} from "../Auth";
import AForm from '../components/AForm.vue'
import AnInput from '../components/AnInput.vue'
import AButton from '../components/AButton.vue'
import ACard from '../components/ACard.vue'
import ALink from '../components/ALink.vue'
import AnAlert from '../components/AnAlert.vue'

// Redirect to ListUserPage if user logged in.
try {
  const user = getUserFromLocalStorage()
  if (user.isAdmin) {
    window.location.hash = 'listUser'
  } else {
    window.location.hash = 'createCv'
  }
} catch (e) {
}

const submitting = ref(false)
const errors = ref<string[]>([])
const form = ref({
  userName: '',
  password: ''
})

const onSubmit = async () => {
  if (submitting.value) {
    return
  }

  errors.value = []

  try {
    submitting.value = true
    await login(form.value.userName, form.value.password)

    form.value = {
      userName: '',
      password: '',
    }

    window.location.hash = 'createCv'
  } catch (e) {
    const message = JSON.parse((e as Error).message)
    errors.value = message.errors
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <a-card class="login-section" title="Login">
    <a-form @submit.prevent="onSubmit" autocomplete="off">
      <label>
        User Name
        <an-input type="text" v-model="form.userName" required></an-input>
      </label>

      <label>
        Password
        <an-input type="password" v-model="form.password" required></an-input>
      </label>

      <an-alert :messages="errors" class="warning"></an-alert>

      <a-button type="submit" class="primary login-btn" :disabled="submitting">Login</a-button>
      <div class="register-link">
        <b>Or</b>
        <a-link href="#register">register</a-link>
      </div>
    </a-form>
  </a-card>
</template>

<style scoped lang="scss">
.login-section {
  .login-btn {
    margin-top: 1.5rem;
  }

  .register-link {
    display: flex;
    justify-content: center;
    gap: 5px;
  }
}
</style>