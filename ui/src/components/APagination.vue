<script setup lang="ts">
import AButton from '../components/AButton.vue'
import AChevronDoubleLeftIcon from '../components/AChevronDoubleLeftIcon.vue'
import AChevronDoubleRightIcon from '../components/AChevronDoubleRightIcon.vue'
import AChevronLeftIcon from '../components/AChevronLeftIcon.vue'
import AChevronRightIcon from '../components/AChevronRightIcon.vue'

defineEmits(['change'])
defineProps({
  page: Number,
  disabled: Boolean,
  lastPage: Number
})
</script>

<template>
  <div class="pagination">
    <a-button class="first" @click="$emit('change', 1)" :disabled="page === 1 || disabled">
      <a-chevron-double-left-icon/>
    </a-button>
    <a-button class="prev" @click="$emit('change', page - 1)" :disabled="page === 1 || disabled">
      <a-chevron-left-icon/>
    </a-button>
    <a-button class="next" @click="$emit('change', page + 1)" :disabled="page === lastPage  || disabled">
      <a-chevron-right-icon/>
    </a-button>
    <a-button class="last" @click="$emit('change', lastPage)" :disabled="page === lastPage || disabled">
      <a-chevron-double-right-icon/>
    </a-button>
  </div>
</template>

<style scoped lang="scss">
.pagination {
  display: flex;
  justify-content: end;
  gap: 5px;

  button {
    width: 3rem;
    display: flex;
    justify-content: center;
    place-items: center;

    svg {
      width: 1rem;
      stroke-width: 0.1rem;
    }
  }
}
</style>