<script setup lang="ts">
import {getUserFromLocalStorage, User} from "../User"
import {ref} from "vue";
import ACard from '../components/ACard.vue'
import AForm from '../components/AForm.vue'
import AnInput from '../components/AnInput.vue'
import ATextarea from '../components/ATextarea.vue'
import AButton from '../components/AButton.vue'
import AnAlert from '../components/AnAlert.vue'
import {addCv} from "../Cv";
import ALink from '../components/ALink.vue'

const submitting = ref(false)
const errors = ref<string[]>([])
const success = ref(false)
const user = ref<User>()
try {
  user.value = getUserFromLocalStorage()
} catch (e) {
  window.location.hash = 'login'
}

const form = ref({
  name: '',
  email: '',
  mobile: '',
  github: '',
  linkedln: '',
  description: '',
  jobTitle: '',
  company: '',
  period: '',
  jobDescription: '',
  university: '',
  faculty: '',
  gpa: '',
  skills: ''
})

const onSubmit = async () => {
  if (submitting.value) {
    return
  }

  errors.value = []
  success.value = false

  if (errors.value.length > 0) {
    return
  }

  submitting.value = true
  try {
    await addCv(form.value)

    form.value = {
      name: '',
      email: '',
      mobile: '',
      github: '',
      linkedln: '',
      description: '',
      jobTitle: '',
      company: '',
      period: '',
      jobDescription: '',
      university: '',
      faculty: '',
      gpa: '',
      skills: ''
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
  <a-link href="#listCv">View CVs</a-link>

  <a-form class="create-cv-section" @submit.prevent="onSubmit" autocomplete="off">
    <ACard class="section" title="Information">
      <label>
        Name
        <an-input type="text" required v-model="form.name"></an-input>
      </label>
      <label>
        Email
        <an-input type="text" required v-model="form.email"></an-input>
      </label>
      <label>
        Mobile
        <an-input type="text" required v-model="form.mobile"></an-input>
      </label>
      <label>
        Github
        <an-input type="text" v-model="form.github"></an-input>
      </label>
      <label>
        Linkedln
        <an-input type="text" v-model="form.linkedln"></an-input>
      </label>
    </ACard>

    <ACard class="section" title="Summary">
      <label>
        Short description about yourself
        <a-textarea rows="3" v-model="form.description"></a-textarea>
      </label>
    </ACard>

    <ACard class="section" title="Experiences">
      <label>
        Job title
        <an-input v-model="form.jobTitle"></an-input>
      </label>
      <label>
        Company
        <an-input v-model="form.company"></an-input>
      </label>
      <label>
        Period
        <an-input v-model="form.period"></an-input>
      </label>
      <label>
        Job description
        <an-input v-model="form.jobDescription"></an-input>
      </label>
    </ACard>

    <ACard class="section" title="Education">
      <label>
        University/College
        <an-input v-model="form.university"></an-input>
      </label>
      <label>
        Faculty
        <an-input v-model="form.faculty"></an-input>
      </label>
      <label>
        GPA
        <an-input v-model="form.gpa"></an-input>
      </label>
    </ACard>

    <ACard class="section" title="Skills">
      <label>
        Skills
        <a-textarea rows="5" v-model="form.skills"></a-textarea>
      </label>
    </ACard>

    <an-alert :messages="errors" class="warning"/>
    <an-alert v-if="success" :messages="['Successfully created new CV!']" class="success"/>

    <a-button type="submit" class="primary save-btn" :disabled="submitting">create</a-button>
  </a-form>
</template>

<style lang="scss">
.create-cv-section {
  width: 700px;

  .section {
    .body {
      display: grid;
      gap: 1rem;
    }
  }

  .save-btn {
    width: 50%;
    margin: 0.75rem auto;
  }
}

</style>