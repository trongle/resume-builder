<script setup lang="ts">
const {messages} = defineProps<{ messages: string[] }>()
</script>

<template>
  <div class="alert" v-if="messages.length > 0">
    <p><b>Errors:</b></p>
    <ul>
      <li v-for="message in messages">{{ message }}</li>
    </ul>
  </div>
</template>

<style scoped lang="scss">
.alert {
  border-radius: 5px;
  color: #242424;
  padding: 5px;

  &.warning {
    border: 1px solid yellow;
    background: #DCDD5D;
  }

  &.success {
    border: 1px solid greenyellow;
    background: lightgreen;
  }
}
</style>