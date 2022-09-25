<script setup lang="ts">
const {title} = defineProps<{ title: string }>()
</script>

<template>
  <section>
    <h1>{{ title }}</h1>
    <div class="body">
      <slot></slot>
    </div>

  </section>
</template>

<style scoped lang="scss">
section {
  padding: 1rem;

  .body {
    border: 1px solid dodgerblue;
    border-radius: 5px;
    margin-top: 2rem;
    padding: 1rem;
  }
}
</style>