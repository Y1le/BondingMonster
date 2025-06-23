<template>
  <div class="user-board">
    <div class="board-card">
      <div class="board-header">
        <h3 class="board-title">
          <span class="title-icon">🏆</span>
          用户榜单
        </h3>
        <div class="board-subtitle">活跃用户排行</div>
      </div>
      
      <div class="board-content">
        <div 
          v-for="(user, index) in topUsers" 
          :key="user.id" 
          class="user-item"
          :class="{ 'top-three': index < 3 }"
        >
          <div class="rank-badge" :class="getRankClass(index)">
            <span v-if="index < 3" class="rank-icon">
              {{ index === 0 ? '🥇' : index === 1 ? '🥈' : '🥉' }}
            </span>
            <span v-else class="rank-number">{{ index + 1 }}</span>
          </div>
          
          <div class="user-info">
            <div class="user-avatar">
              <img 
                :src="user.avatar || getDefaultAvatar()" 
                :alt="user.username"
                class="avatar-small"
                @error="handleAvatarError"
              >
            </div>
            <div class="user-details">
              <div class="user-name">{{ user.username }}</div>
              <div class="user-id">ID: {{ user.id }}</div>
            </div>
          </div>
          
          <div class="user-score">
            <div class="score-value">{{ user.score || 100 - index * 10 }}</div>
            <div class="score-label">积分</div>
          </div>
        </div>
        
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-state">
          <div class="loading-spinner"></div>
          <div class="loading-text">加载中...</div>
        </div>
        
        <!-- 空状态 -->
        <div v-if="!loading && topUsers.length === 0" class="empty-board">
          <div class="empty-icon">👥</div>
          <div class="empty-text">暂无用户数据</div>
        </div>
      </div>
      
      <div class="board-footer">
        <button class="view-all-btn" @click="viewAllUsers">
          查看完整榜单
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor">
            <path d="M5 12h14M12 5l7 7-7 7"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserBoard',
  data() {
    return {
      loading: false,
      topUsers: [
        { 
          id: 1001, 
          username: '编程达人', 
          score: 2580,
          avatar: 'https://img.shetu66.com/2023/07/27/1690436791750269.png'
        },
        { 
          id: 1002, 
          username: '技术专家', 
          score: 2340,
          avatar: 'https://img.shetu66.com/2023/07/27/1690436791750269.png'
        },
        { 
          id: 1003, 
          username: '代码忍者', 
          score: 2100,
          avatar: 'https://img.shetu66.com/2023/07/27/1690436791750269.png'
        },
        { 
          id: 1004, 
          username: '算法大师', 
          score: 1890,
          avatar: 'https://img.shetu66.com/2023/07/27/1690436791750269.png'
        },
        { 
          id: 1005, 
          username: '全栈开发', 
          score: 1750,
          avatar: 'https://img.shetu66.com/2023/07/27/1690436791750269.png'
        },
      ]
    }
  },
  methods: {
    getRankClass(index) {
      return {
        'rank-gold': index === 0,
        'rank-silver': index === 1,
        'rank-bronze': index === 2,
        'rank-normal': index >= 3
      }
    },
    
    getDefaultAvatar() {
      return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzIiIGhlaWdodD0iMzIiIHZpZXdCb3g9IjAgMCAzMiAzMiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMTYiIGN5PSIxNiIgcj0iMTYiIGZpbGw9IiNGM0Y0RjYiLz4KPHN2ZyB3aWR0aD0iMjAiIGhlaWdodD0iMjAiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4PSI2IiB5PSI2Ij4KPHA+cGF0aCBkPSJNMjAgMjF2LTJhNCA0IDAgMCAwLTQtNEg4YTQgNCAwIDAgMC00IDR2MiIgc3Ryb2tlPSIjOUI5QkEwIiBzdHJva2Utd2lkdGg9IjIiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIvPgo8Y2lyY2xlIGN4PSIxMiIgY3k9IjciIHI9IjQiIHN0cm9rZT0iIzlCOUJBMCIgc3Ryb2tlLXdpZHRoPSIyIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiLz4KPC9zdmc+Cjwvc3ZnPgo='
    },
    
    handleAvatarError(event) {
      event.target.src = this.getDefaultAvatar()
    },
    
    viewAllUsers() {
      // 跳转到完整榜单页面
      this.$router?.push('/leaderboard') || console.log('查看完整榜单')
    },
    
    async fetchTopUsers() {
      this.loading = true
      try {
        // 这里应该是实际的API调用
        // const response = await this.$http.get('/api/user/topUsers/')
        // this.topUsers = response.data
        
        // 模拟异步加载
        await new Promise(resolve => setTimeout(resolve, 1000))
      } catch (error) {
        console.error('获取用户榜单失败:', error)
        // 可以显示错误提示
      } finally {
        this.loading = false
      }
    }
  },
  
  mounted() {
    // 组件挂载时可以获取数据
    // this.fetchTopUsers()
  }
}
</script>

<style scoped>
.user-board {
  width: 100%;
}

.board-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.board-header {
  padding: 24px 20px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.board-title {
  color: white;
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 4px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.title-icon {
  font-size: 20px;
}

.board-subtitle {
  color: rgba(255, 255, 255, 0.8);
  font-size: 13px;
  margin: 0;
}

.board-content {
  padding: 16px 0;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.user-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  transition: all 0.2s ease;
  position: relative;
}

.user-item:hover {
  background: rgba(102, 126, 234, 0.1);
  transform: translateX(4px);
}

.user-item.top-three {
  background: linear-gradient(90deg, rgba(255, 215, 0, 0.1) 0%, transparent 100%);
}

.rank-badge {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  margin-right: 12px;
  font-weight: 700;
  font-size: 14px;
}

.rank-gold {
  background: linear-gradient(135deg, #ffd700, #ffed4e);
  color: #92400e;
  box-shadow: 0 2px 8px rgba(255, 215, 0, 0.3);
}

.rank-silver {
  background: linear-gradient(135deg, #c0c0c0, #e5e7eb);
  color: #374151;
  box-shadow: 0 2px 8px rgba(192, 192, 192, 0.3);
}

.rank-bronze {
  background: linear-gradient(135deg, #cd7f32, #d97706);
  color: white;
  box-shadow: 0 2px 8px rgba(205, 127, 50, 0.3);
}

.rank-normal {
  background: #f3f4f6;
  color: #6b7280;
}

.rank-icon {
  font-size: 18px;
}

.rank-number {
  font-size: 16px;
  font-weight: 600;
}

.user-info {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.user-avatar {
  margin-right: 10px;
}

.avatar-small {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(255, 255, 255, 0.5);
}

.user-details {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-weight: 600;
  color: #1f2937;
  font-size: 14px;
  margin-bottom: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-id {
  color: #6b7280;
  font-size: 12px;
}

.user-score {
  text-align: right;
  margin-left: 12px;
}

.score-value {
  font-weight: 700;
  color: #1f2937;
  font-size: 14px;
}

.score-label {
  color: #9ca3af;
  font-size: 11px;
}

.board-footer {
  padding: 16px 20px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.view-all-btn {
  width: 100%;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 10px 16px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.view-all-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-1px);
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
  color: #6b7280;
}

.loading-spinner {
  width: 24px;
  height: 24px;
  border: 2px solid #e5e7eb;
  border-top: 2px solid #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 12px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  font-size: 14px;
}

.empty-board {
  text-align: center;
  padding: 40px 20px;
  color: #9ca3af;
}

.empty-icon {
  font-size: 32px;
  margin-bottom: 12px;
}

.empty-text {
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .board-card {
    border-radius: 12px;
  }
  
  .board-header {
    padding: 20px 16px;
  }
  
  .user-item {
    padding: 10px 16px;
  }
  
  .rank-badge {
    width: 32px;
    height: 32px;
  }
  
  .user-name {
    font-size: 13px;
  }
  
  .board-footer {
    padding: 12px 16px;
  }
}

/* 暗色模式适配 */
@media (prefers-color-scheme: dark) {
  .board-content {
    background: rgba(31, 41, 55, 0.95);
  }
  
  .user-name {
    color: #f9fafb;
  }
  
  .score-value {
    color: #f9fafb;
  }
  
  .user-item:hover {
    background: rgba(59, 130, 246, 0.1);
  }
}
</style>