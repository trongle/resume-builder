<script setup lang="ts">
import ACard from '../components/ACard.vue'
import ATable from '../components/ATable.vue'
import {ref} from "vue";
import {Cv, deleteCv, fetchCvs} from "../Cv";
import AButton from '../components/AButton.vue'

const cvs = ref<Cv[]>(await fetchCvs())

const onDelete = async (id: number) => {
  if (confirm("This action cannot undo. Please confirm")) {
    await deleteCv(id)
    cvs.value = await fetchCvs()
  }
}
const onEdit = (id: number) => {
  console.log("edit page")
}

</script>

<template>
  <a-card class="list-cv-section" title="List CV">
    <a-table>
      <template v-slot:thead>
        <tr>
          <th>
            <div class="thead-container">
              <span>ID</span>
            </div>
          </th>
          <th>
            <div class="thead-container">
              <span>Name</span>
            </div>
          </th>
          <th>
            <div class="thead-container">
              <span>Email</span>
            </div>
          </th>
          <th>
            <div class="thead-container">
              <span>Mobile</span>
            </div>
          </th>
          <th>
            <div class="thead-container">
            </div>
          </th>
        </tr>
      </template>
      <template v-slot:tbody>
        <template v-if="cvs.length > 0">
          <tr v-for="cv in cvs" :key="cv.id">
            <td>{{ cv.id }}</td>
            <td>{{ cv.name }}</td>
            <td>{{ cv.email }}</td>
            <td>{{ cv.mobile }}</td>
            <td>
              <div class="button-group">
                <a-button @click="onDelete(cv.id)">Delete</a-button>
                <a-button @click="onEdit(cv.id)">Edit</a-button>
              </div>

            </td>
          </tr>
        </template>

        <template v-else>
          <tr>
            <td colspan="4" class="empty-list">Empty list</td>
          </tr>
        </template>
      </template>
    </a-table>
  </a-card>
</template>

<style scoped>
.list-cv-section {
  width: 700px;
}

.button-group {
  display: flex;
  gap: 5px;
}
</style>