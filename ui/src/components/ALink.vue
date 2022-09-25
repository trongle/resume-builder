<template>
  <a>
    <slot></slot>
  </a>
</template>

<style scoped lang="scss">
a {
  text-decoration: none;
  color: #4974FF;

  &:hover {
    opacity: 80%;
  }
}
</style>