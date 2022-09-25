<script setup lang="ts">
import {ref} from "vue"
import {fetchUsers, getUserFromLocalStorage, User} from "../User"
import {logout} from "../Auth"
import ACard from '../components/ACard.vue'
import AnInput from '../components/AnInput.vue'
import ATable from '../components/ATable.vue'
import AChevronUpIcon from '../components/AChevronUpIcon.vue'
import AChevronDownIcon from '../components/AChevronDownIcon.vue'
import APagination from '../components/APagination.vue'
import AButton from '../components/AButton.vue'
import ALink from '../components/ALink.vue'

const ordering = ref({
  column: '',
  by: ''
})
const searchTerm = ref('')
const submitting = ref(false)
const users = ref<User[]>([])
const me = ref<User>()
const currentPage = ref(1)
const lastPage = ref(0)

const getUsers = async () => {
  try {
    submitting.value = true
    const data = await fetchUsers(searchTerm.value, currentPage.value, ordering.value)
    users.value = data.users
    lastPage.value = data.lastPage
  } catch (e) {
    window.location.hash = 'login'
  } finally {
    submitting.value = false
  }
}

getUsers()

try {
  me.value = getUserFromLocalStorage()
} catch (e) {
  window.location.hash = 'login'
}

const sort = async (column: string, by: 'asc' | 'desc') => {
  ordering.value = {column, by}
  currentPage.value = 1
  await getUsers()
}

const onSearch = async (_: Event) => {
  currentPage.value = 1
  await getUsers()
}

const onPaginationChange = async (page: number) => {
  currentPage.value = page
  await getUsers()
}

const onLogout = () => {
  logout()
  window.location.hash = 'login'
}
</script>

<template>
  <a-card title="Account Information">
    <p><b>User name:</b> {{ me?.userName }}</p>
    <p><b>Full name:</b> {{ me?.fullName }}</p>
    <p><b>Email:</b> {{ me?.email }}</p>
    <a-link href="#" @click.prevent="onLogout">logout</a-link>
  </a-card>

  <a-card class="list-user-section" title="List User">
    <an-input class="search-input"
              placeholder="search..."
              @keyup.enter="onSearch"
              v-model="searchTerm"
              :disabled="submitting"/>

    <a-table>
      <template v-slot:thead>
        <tr>
          <th>
            <div class="thead-container">
              <span>User name</span>
              <div class="sort">
                <a-button>
                  <a-chevron-up-icon
                      v-if="ordering.column === 'userName' && ordering.by === 'desc' || ordering.column !== 'userName'"
                      @click="sort('userName', 'asc')"/>
                </a-button>
                <a-button>
                  <a-chevron-down-icon
                      v-if="ordering.column === 'userName' && ordering.by === 'asc' || ordering.column !== 'userName'"
                      @click="sort('userName', 'desc')"/>
                </a-button>
              </div>
            </div>
          </th>
          <th>
            <div class="thead-container">
              <span>Full name</span>
              <div class="sort">
                <a-button>
                  <a-chevron-up-icon
                      v-if="ordering.column === 'fullName' && ordering.by === 'desc' || ordering.column !== 'fullName'"
                      @click="sort('fullName', 'asc')"/>
                </a-button>
                <a-button>
                  <a-chevron-down-icon
                      v-if="ordering.column === 'fullName' && ordering.by === 'asc' || ordering.column !== 'fullName'"
                      @click="sort('fullName', 'desc')"/>
                </a-button>
              </div>
            </div>
          </th>
          <th>
            <div class="thead-container">
              <span>Email</span>
              <div class="sort">
                <a-button>
                  <a-chevron-up-icon
                      v-if="ordering.column === 'email' && ordering.by === 'desc' || ordering.column !== 'email'"
                      @click="sort('email', 'asc')"/>
                </a-button>
                <a-button>
                  <a-chevron-down-icon
                      v-if="ordering.column === 'email' && ordering.by === 'asc' || ordering.column !== 'email'"
                      @click="sort('email', 'desc')"/>
                </a-button>
              </div>
            </div>
          </th>
        </tr>
      </template>
      <template v-slot:tbody>
        <template v-if="users.length > 0">
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.userName }}</td>
            <td>{{ user.fullName }}</td>
            <td>{{ user.email }}</td>
          </tr>
        </template>

        <template v-else>
          <tr>
            <td colspan="3" class="empty-list">Empty list</td>
          </tr>
        </template>

      </template>
    </a-table>

    <a-pagination @change="onPaginationChange($event)" :page="currentPage" :last-page="lastPage"
                  :disabled="submitting"/>
  </a-card>
</template>

<style scoped lang="scss">
.list-user-section {
  width: 700px;

  .search-input {
    margin-bottom: 2rem;
  }

  .thead-container {
    display: flex;
    justify-content: left;
    align-items: center;
    gap: 5px;

    .sort {
      display: grid;
      gap: 1px;

      button {
        background: none;
        height: fit-content;
        color: white;
        display: flex;
        place-items: center;

        svg {
          width: 1rem;
          stroke-width: 0.1rem;
        }
      }
    }
  }

  .empty-list {
    text-align: center;
  }
}
</style>