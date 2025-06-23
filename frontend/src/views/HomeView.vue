<template>
  <ContentBase>
    <div class="home-container">
      <div class="main-content">
        <!-- 发帖区域 -->
        <div class="write-section" v-if="is_me">
          <UserProfileWrite @post_a_post="post_a_post" />
        </div>
        
        <!-- 帖子列表 -->
        <div class="posts-section">
          <UserProfilePosts 
            :user="user" 
            :posts="posts" 
            @delete_a_post="delete_a_post" 
          />
        </div>
      </div>
      
      <!-- 侧边栏 -->
      <div class="sidebar">
        <UserBoard />
      </div>
    </div>
  </ContentBase>
</template>

<script>
import { reactive, ref } from 'vue'
import UserProfilePosts from '@/components/UserProfilePosts.vue'
import ContentBase from '../components/ContentBase.vue'
import UserProfileWrite from '../components/UserProfileWrite.vue'
import UserBoard from '@/components/UserBoard.vue'

export default {
  name: 'HomeView',
  components: {
    ContentBase,
    UserProfileWrite,
    UserBoard,
    UserProfilePosts
  },
  setup() {
    const is_me = ref(true) // 示例数据
    const user = reactive({ id: 1, username: '当前用户' }) // 示例数据
    
    const posts = reactive({
      posts: [
        { 
          id: 1, 
          content: '这是第一条帖子，内容可能会比较长，用来测试布局效果。', 
          userimage: 'https://img.shetu66.com/2023/07/27/1690436791750269.png',
          timestamp: '2小时前'
        },
        { 
          id: 2, 
          content: '这是第二条帖子，分享一些有趣的内容。', 
          userimage: 'https://img.shetu66.com/2023/07/27/1690436791750269.png',
          timestamp: '4小时前'
        },
        { 
          id: 3, 
          content: '这是第三条帖子，希望大家喜欢。', 
          userimage: 'https://img.shetu66.com/2023/07/27/1690436791750269.png',
          timestamp: '6小时前'
        },
        { 
          id: 4, 
          content: '这是第四条帖子，内容丰富多彩。', 
          userimage: 'https://img.shetu66.com/2023/07/27/1690436791750269.png',
          timestamp: '8小时前'
        },
        { 
          id: 5, 
          content: '这是第五条帖子，感谢大家的支持。', 
          userimage: 'https://img.shetu66.com/2023/07/27/1690436791750269.png',
          timestamp: '1天前'
        },
      ],
    })

    const post_a_post = (newPost) => {
      posts.posts.unshift({
        id: Date.now(),
        content: newPost.content,
        userimage: 'https://img.shetu66.com/2023/07/27/1690436791750269.png',
        timestamp: '刚刚'
      })
    }

    const delete_a_post = (postId) => {
      const index = posts.posts.findIndex(post => post.id === postId)
      if (index > -1) {
        posts.posts.splice(index, 1)
      }
    }

    return {
      posts,
      user,
      is_me,
      post_a_post,
      delete_a_post
    }
  }
}
</script>

<style scoped>
.home-container {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 24px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.main-content {
  min-width: 0; /* 防止grid溢出 */
}

.write-section {
  margin-bottom: 24px;
}

.posts-section {
  /* 帖子区域样式 */
}

.sidebar {
  position: sticky;
  top: 20px;
  height: fit-content;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .home-container {
    grid-template-columns: 1fr;
    padding: 16px;
    gap: 16px;
  }
  
  .sidebar {
    position: static;
    order: -1; /* 移动端将侧边栏放到顶部 */
  }
}
</style>