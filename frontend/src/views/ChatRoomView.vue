<template>
        <div class="chatroom-page">
            <h1>聊天室列表</h1>
            <div v-if="loading" class="loading-message">正在加载房间信息...</div>
            <div v-else-if="error" class="error-message">加载房间信息失败: {{ error }}</div>
            <div v-else class="room-grid">
                <div
                    v-for="room in rooms"
                    :key="room.id"
                    :class="['room-card', { 'full': room.currentUsers >= room.maxUsers, 'active-room': currentRoomId === room.id }]"
                    @click="handleRoomClick(room.id)"
                >
                    <h2>{{ room.id }}</h2>
                    <p>当前人数: {{ room.currentUsers }} / {{ room.maxUsers }}</p>
                    <div class="user-avatars">
                        <div v-for="user in room.users" :key="user.id" class="user-display">
                            <img :src="user.photo" :alt="user.name" class="room-user-avatar" />
                            <span>{{ user.name }}</span>
                        </div>
                        <!-- 如果房间未满，显示占位符 -->
                        <template v-if="room.currentUsers < room.maxUsers">
                            <div v-for="n in (room.maxUsers - room.currentUsers)" :key="`placeholder-${room.id}-${n}`" class="user-display placeholder">
                                <div class="room-user-avatar empty-slot"></div>
                                <span>空位</span>
                            </div>
                        </template>
                    </div>
                    <div v-if="room.currentUsers >= room.maxUsers" class="room-full-overlay">房间已满</div>
                </div>
            </div>
        </div>

</template>

<script setup>
import { ref } from 'vue';

// 示例房间数据
const rooms = ref([
    {
        id: 1,
        maxUsers: 2,
        currentUsers: 2,
        users: [
            { id: 1, name: 'user1', photo: 'https://img.shetu66.com/2023/07/27/1690436791750269.png' },
            { id: 2, name: 'user2', photo: 'https://pic2.zhimg.com/v2-ffdbbeea7a8063dd40a1e80a7c023b71_b.jpg' }
        ]
    },
    {
        id: 2,
        maxUsers: 2,
        currentUsers: 1,
        users: [
            { id: 1, name: '123', photo: 'https://img.shetu66.com/2023/07/27/1690436791750269.png' },
        ]
    },
    {
        id: 3,
        maxUsers: 2,
        currentUsers: 2,
        users: [
            { id: 1, name: 'user1', photo: 'https://img.shetu66.com/2023/07/27/1690436791750269.png' },
            { id: 2, name: 'user2', photo: 'https://pic2.zhimg.com/v2-ffdbbeea7a8063dd40a1e80a7c023b71_b.jpg' }
        ]
    },
    
]);

const handleRoomClick = (roomId) => {
    // 处理房间点击事件
    console.log('房间被点击，ID:', roomId);
};
</script>

<style scoped>
.chatroom-page {
    padding: 20px;
    flex-grow: 1;
}

.chatroom-page h1 {
    text-align: center;
    color: #333;
    margin-bottom: 30px;
}

.loading-message, .error-message {
    text-align: center;
    font-size: 1.2em;
    color: #666;
    margin-top: 50px;
}

.error-message {
    color: #dc3545;
}

.room-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); /* 响应式布局 */
    gap: 20px;
    max-width: 1200px;
    margin: 0 auto;
}

.room-card {
    background-color: white;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    padding: 20px;
    text-align: center;
    cursor: pointer;
    transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
    position: relative;
    overflow: hidden; /* 确保内容不溢出 */
}

.room-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.room-card.full {
    opacity: 0.7;
    cursor: not-allowed;
    background-color: #f8d7da; /* 红色背景表示已满 */
    border: 1px solid #dc3545;
}

.room-card.active-room {
    border: 2px solid #6a5acd; /* 活跃房间边框 */
    box-shadow: 0 0 15px rgba(106, 90, 205, 0.5);
}


.room-card h2 {
    color: #6a5acd;
    margin-top: 0;
    margin-bottom: 10px;
    font-size: 20px;
}

.room-card p {
    color: #555;
    font-size: 14px;
    margin-bottom: 15px;
}

.user-avatars {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 15px; /* 头像之间的间距 */
    margin-top: 15px;
    min-height: 50px; /* 确保有足够的空间显示头像和名称 */
}

.user-display {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 5px;
}

.room-user-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    object-fit: cover;
    border: 2px solid #6a5acd;
}

.user-display span {
    font-size: 12px;
    color: #333;
    white-space: nowrap; /* 防止名字换行 */
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 60px; /* 限制名字宽度 */
}

.empty-slot {
    background-color: #e0e0e0;
    border: 2px dashed #999;
}

.room-full-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(220, 53, 69, 0.8); /* 半透明红色 */
    color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 24px;
    font-weight: bold;
    border-radius: 10px;
    pointer-events: none; /* 允许点击穿透到下面的卡片 */
}
</style>



