<template>
  <div class="user-board">
    <div class="card">
      <div class="card-header">
        <h5>用户榜单</h5>
      </div>
      <ul class="list-group list-group-flush">
        <li v-for="user in topUsers" :key="user.id" class="list-group-item">
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <span class="badge bg-primary me-2">{{ user.rank }}</span>
              <span>{{ user.username }}</span>
            </div>
            <span class="text-muted">ID: {{ user.id }}</span>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
// import { ref } from 'vue';

export default {
  name: 'UserBoard',
  setup() {
    // const topUsers = ref([]);
    const topUsers = [
        { id: 1, username: '用户A', rank: 1, image: 'https://img.shetu66.com/2023/07/27/1690436791750269.png' },
        { id: 2, username: '用户B', rank: 2, image: 'https://img.shetu66.com/2023/07/27/1690436791750269.png' },
        { id: 3, username: '用户C', rank: 3, image: 'https://img.shetu66.com/2023/07/27/1690436791750269.png' },
        { id: 4, username: '用户D', rank: 4, image: 'https://img.shetu66.com/2023/07/27/1690436791750269.png' },
        { id: 5, username: '用户E', rank: 5, image: 'https://img.shetu66.com/2023/07/27/1690436791750269.png' },
        ];

    return {
        topUsers,
    }
  }
}
</script>

<style scoped>
.user-leaderboard {
  position: sticky;
  top: 20px;
}
.card-header {
  font-weight: bold;
  background-color: #f8f9fa;
}
.list-group-item {
  padding: 0.75rem 1.25rem;
}
.badge {
  min-width: 30px;
  text-align: center;
}
</style>
