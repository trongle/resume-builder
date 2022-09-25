<template>
  <form>
    <slot></slot>
  </form>
</template>

<style scoped>
form {
  display: grid;
  width: 100%;
  margin: 0 auto;
  gap: 1em;
  padding: 20px 10px;
}
</style>