<script setup lang="ts">
import {ref} from "vue";
import AnInput from '../components/AnInput.vue'
import AButton from '../components/AButton.vue'
import AForm from '../components/AForm.vue'
import AnAlert from '../components/AnAlert.vue';
import ALink from '../components/ALink.vue'
import ACard from '../components/ACard.vue'
import {addUser, getUserFromLocalStorage} from "../User";

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
const success = ref(false)
const form = ref({
  userName: '',
  fullName: '',
  email: '',
  password: '',
  confirmPassword: '',
})

const onSubmit = async () => {
  if (submitting.value) {
    return
  }

  errors.value = []
  success.value = false

  if (form.value.confirmPassword !== form.value.password) {
    errors.value.push('Password and Confirm Password do not match.')
  }
  if (errors.value.length > 0) {
    return
  }

  submitting.value = true
  try {
    await addUser({
      id: (new Date()).getMilliseconds(),
      userName: form.value.userName,
      fullName: form.value.fullName,
      email: form.value.email,
      password: form.value.password,
      isAdmin: false
    })

    form.value = {
      userName: '',
      fullName: '',
      email: '',
      password: '',
      confirmPassword: '',
    }
    success.value = true
  } catch (e) {
    const message = JSON.parse((e as Error).message)
    errors.value = message.errors
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <a-card class="register-section" title="Register">
    <a-form @submit.prevent="onSubmit" autocomplete="off">
      <label>
        User Name
        <an-input type="text" v-model="form.userName" required></an-input>
      </label>
      <label>
        Full Name
        <an-input type="text" v-model="form.fullName"></an-input>
      </label>
      <label>
        Email
        <an-input type="email" v-model="form.email"></an-input>
      </label>
      <label>
        Password
        <an-input type="password" v-model="form.password" required></an-input>
      </label>
      <label>
        Confirm Password
        <an-input type="password" v-model="form.confirmPassword" required></an-input>
      </label>

      <an-alert :messages="errors" class="warning"/>
      <an-alert v-if="success" :messages="['Successfully registered!']" class="success"/>

      <a-button type="submit" class="primary save-btn" :disabled="submitting">register</a-button>
      <div class="login-link">
        <b>Or</b>
        <a-link href="#login">login</a-link>
      </div>
    </a-form>
  </a-card>
</template>

<style scoped lang="scss">
.register-section {
  display: grid;
  max-width: 400px;
  margin: 0 auto;

  h1 {
    font-size: 2rem;
  }

  .save-btn {
    margin-top: 1.5rem;
  }

  .login-link {
    display: flex;
    justify-content: center;
    gap: 5px;
  }
}
</style>