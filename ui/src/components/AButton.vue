<template>
  <button type="button">
    <slot></slot>
  </button>
</template>

<style scoped lang="scss">
button {
  width: 100%;
  border-radius: 5px;
  border: none;
  height: 2rem;

  &:hover {
    cursor: pointer;
    opacity: 80%;
  }

  &:disabled {
    cursor: not-allowed;
    opacity: 80%;
  }

  &.primary {
    background: dodgerblue;
    color: white;
    font-size: 1.2rem;
  }
}
</style>