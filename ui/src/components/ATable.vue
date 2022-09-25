<template>
  <table>
    <thead>
    <slot name="thead"></slot>
    </thead>
    <tbody>
    <slot name="tbody"></slot>
    </tbody>
  </table>
</template>

<style lang="scss">
table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 2rem;

  thead {
    th {
      border: 1px solid darkgray;
      text-align: left;
      padding: 10px;

      &:not(:last-child):not(:first-child) {
        border-right: none;
        border-left: none;
      }

      &:first-child {
        border-right: none;
      }

      &:last-child {
        border-left: none;
      }
    }
  }

  tbody {
    tr {
      border: 1px solid gray;

      &:hover {
        opacity: 70%;
      }

      td {
        border: none;
        padding: 10px;
      }
    }
  }
}
</style>