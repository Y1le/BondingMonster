<template>
  <div class="posts-container">
    <div 
      v-for="post in posts.posts" 
      :key="post.id" 
      class="post-card"
    >
      <div class="post-header">
        <div class="user-avatar">
          <img 
            :src="post.userimage" 
            :alt="'用户头像'" 
            class="avatar-img"
            @error="handleImageError"
          >
        </div>
        <div class="post-meta">
          <div class="username">用户名</div>
          <div class="timestamp">{{ post.timestamp || '刚刚' }}</div>
        </div>
        <div class="post-actions" v-if="is_me">
          <button 
            @click="confirmDelete(post.id)" 
            class="delete-btn"
            title="删除帖子"
          >
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path d="M3 6h18M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2m3 0v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6h14zM10 11v6M14 11v6"/>
            </svg>
          </button>
        </div>
      </div>
      
      <div class="post-content">
        {{ post.content }}
      </div>
      
      <div class="post-footer">
        <div class="post-stats">
          <button class="stat-btn">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
            </svg>
            <span>0</span>
          </button>
          <button class="stat-btn">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
            </svg>
            <span>0</span>
          </button>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-if="posts.posts.length === 0" class="empty-state">
      <div class="empty-icon">📝</div>
      <div class="empty-text">还没有帖子，快来发表第一条吧！</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserProfilePosts",
  props: {
    posts: {
      type: Object,
      required: true,
    },
    user: {
      type: Object,
      required: true,
    }
  },
  data() {
    return {
      is_me: true // 示例数据，实际应该从props或store获取
    }
  },
  methods: {
    handleImageError(event) {
      // 头像加载失败时显示默认头像
      event.target.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiIGZpbGw9IiNGM0Y0RjYiLz4KPHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4PSI4IiB5PSI4Ij4KPHA+cGF0aCBkPSJNMjAgMjF2LTJhNCA0IDAgMCAwLTQtNEg4YTQgNCAwIDAgMC00IDR2MiIgc3Ryb2tlPSIjOUI5QkEwIiBzdHJva2Utd2lkdGg9IjIiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIvPgo8Y2lyY2xlIGN4PSIxMiIgY3k9IjciIHI9IjQiIHN0cm9rZT0iIzlCOUJBMCIgc3Ryb2tlLXdpZHRoPSIyIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiLz4KPC9zdmc+Cjwvc3ZnPgo='
    },
    
    confirmDelete(postId) {
      if (confirm('确定要删除这条帖子吗？此操作无法撤销。')) {
        this.delete_a_post(postId)
      }
    },
    
    delete_a_post(postId) {
      this.$emit('delete_a_post', postId)
    }
  }
}
</script>

<style scoped>
.posts-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.post-card {
  background: #ffffff;
  border-radius: 12px;
  border: 1px solid #e1e5e9;
  padding: 20px;
  transition: all 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.post-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-1px);
}

.post-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 16px;
}

.user-avatar {
  margin-right: 12px;
}

.avatar-img {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #f0f0f0;
}

.post-meta {
  flex: 1;
  min-width: 0;
}

.username {
  font-weight: 600;
  color: #1a1a1a;
  font-size: 15px;
  margin-bottom: 2px;
}

.timestamp {
  color: #6b7280;
  font-size: 13px;
}

.post-actions {
  margin-left: auto;
}

.delete-btn {
  background: none;
  border: none;
  color: #dc2626;
  padding: 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.delete-btn:hover {
  background-color: #fee2e2;
  transform: scale(1.05);
}

.post-content {
  color: #374151;
  font-size: 15px;
  line-height: 1.6;
  margin-bottom: 16px;
  word-wrap: break-word;
}

.post-footer {
  border-top: 1px solid #f3f4f6;
  padding-top: 12px;
}

.post-stats {
  display: flex;
  gap: 16px;
}

.stat-btn {
  background: none;
  border: none;
  color: #6b7280;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
}

.stat-btn:hover {
  background-color: #f3f4f6;
  color: #374151;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #6b7280;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 16px;
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .post-card {
    background: #1f2937;
    border-color: #374151;
    color: #f9fafb;
  }
  
  .username {
    color: #f9fafb;
  }
  
  .post-content {
    color: #d1d5db;
  }
  
  .post-footer {
    border-color: #374151;
  }
  
  .stat-btn:hover {
    background-color: #374151;
    color: #f9fafb;
  }
}
</style>