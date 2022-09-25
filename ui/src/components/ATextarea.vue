<script setup lang="ts">
defineProps(['modelValue'])
defineEmits(['update:modelValue'])
</script>

<template>
  <textarea rows="5" :value="modelValue" @input="$emit('update:modelValue', $event.target.value)"></textarea>
</template>

<style scoped>
  textarea {
    width: 100%;
  }
</style>