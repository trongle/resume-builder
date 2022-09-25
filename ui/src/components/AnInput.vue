<script setup lang="ts">
defineProps(['modelValue'])
defineEmits(['update:modelValue'])
</script>

<template>
  <input :value="modelValue" @input="$emit('update:modelValue', $event.target.value)">
</template>

<style scoped>
input {
  width: 100%;
  padding: 5px;
  height: 2rem;
  border-radius: 5px;
  border: none;
}
</style>