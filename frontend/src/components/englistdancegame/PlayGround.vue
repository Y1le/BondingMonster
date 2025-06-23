<template>
  <div class="play-ground">
        <div class="row h-100">
            <div class="col-2 user-panel">
                <div class="user-info">
                    <div class="user-photo">
                        <img src="https://th.bing.com/th/id/OIP.H3ofruWZi8OgQskOyFmP9gAAAA?r=0&rs=1&pid=ImgDetMain&cb=idpwebp2&o=7&rm=3" alt="我的头像-加载失败">    
                    </div>
                    <div class="user-name">{{ $store.state.user.name }}</div>
                    <div class="score">分数: {{ userScore }}</div>
                </div>
            </div>
            <div class="col-8 game-area">
                <div class="word-display">
                    <h1>{{ word }}</h1>
                </div>  
                <div class="option-container">
                    <div v-for="(item, index) in option" :key="index" class="option-item">{{ item }}</div>
                </div>
                <div class="match-button-container">  
                    <button  type="button" class="btn btn-primary btn-lg start-match-btn">开始匹配</button>
                </div>
            </div>
            <div class="col-2 user-panel">
                <div class="user-info">
                    <div class="user-photo">
                        <img src="https://th.bing.com/th/id/OIP.H3ofruWZi8OgQskOyFmP9gAAAA?r=0&rs=1&pid=ImgDetMain&cb=idpwebp2&o=7&rm=3" alt="我的头像-加载失败">    
                    </div>
                    <div class="user-name">{{ $store.state.englistdancegame.oppoent_name }}</div>
                    <div class="score">分数: {{ oppoentScore }}</div>
                </div>
            </div>
        </div>  
  </div>
</template>

<script>
import { useStore } from 'vuex';
import { ref } from 'vue';
export default {
    name: 'PlayGround',
    setup() {
        const $store = useStore();
        const userScore = ref('0');
        const word = ref('ygl');
        const option = ref(['opt1', 'opt2', 'opt3', 'opt4']);
        const oppoentScore = ref('0');
        return {
            $store,
            userScore,
            oppoentScore,
            word,
            option,
        };
    },
};  
</script>

<style scoped>
/* 引入 Google Fonts (可选，可以根据喜好选择其他字体) */
@import url('https://fonts.googleapis.com/css2?family=Press+Start+2P&family=Roboto:wght@400;700&display=swap');

div.play-ground {
    width: 100vw;
    height: 100vh;
    /* 更柔和的渐变背景 */
    background: linear-gradient(to bottom right, #a8dadc, #457b9d);
    display: flex; /* 使用 flexbox 布局 */
    align-items: center; /* 垂直居中 */
    justify-content: center; /* 水平居中 */
}

.row.h-100 {
    height: 100%; /* 确保 row 占据整个 play-ground 的高度 */
    width: 100%;
}

div.user-panel {
    background-color: rgba(29, 53, 87, 0.7); /* 深色半透明背景 */
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    color: #f1faee; /* 浅色文字 */
    font-family: 'Roboto', sans-serif;
}

div.user-info {
    text-align: center;
}

div.user-photo {
    margin-bottom: 15px;
}

div.user-photo img {
    width: 12vh; /* 稍微大一点 */
    height: 12vh;
    border-radius: 50%; /* 圆形头像 */
    border: 3px solid #a8dadc; /* 边框 */
    object-fit: cover; /* 确保图片填充 */
}

div.user-name {
    font-size: 1.8rem;
    font-weight: bold;
    margin-bottom: 10px;
}

div.score {
    font-size: 1.5rem;
    color: #f1faee;
}

div.game-area {
    background-color: rgba(241, 250, 238, 0.9); /* 浅色半透明背景 */
    display: flex;
    flex-direction: column;
    justify-content: space-around; /* 均匀分布内容 */
    align-items: center;
    padding: 20px;
    border-radius: 15px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}

div.word-display {
    text-align: center;
    margin-bottom: 30px; /* 增加与选项的间距 */
}

div.word-display h1 {
    font-size: 15vh; /* 更大的字体 */
    color: #e63946; /* 醒目的红色 */
    font-family: 'Press Start 2P', cursive; /* 游戏风格字体 */
    text-shadow: 4px 4px 0px rgba(0, 0, 0, 0.2); /* 字体阴影 */
    letter-spacing: 5px; /* 字母间距 */
}

div.option-container {
    display: flex;
    flex-wrap: wrap; /* 允许换行 */
    justify-content: center;
    align-items: center;
    margin-bottom: 40px; /* 增加与按钮的间距 */
}   

div.option-item {
    width: 22vh; /* 稍微大一点 */
    height: 15vh; /* 调整高度 */
    margin: 15px; /* 增加间距 */
    background-color: #457b9d; /* 蓝色背景 */
    color: #f1faee; /* 浅色文字 */
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 2.5rem; /* 更大的字体 */
    border-radius: 15px; /* 更圆润的边角 */
    cursor: pointer; /* 鼠标悬停时显示手型 */
    transition: all 0.3s ease; /* 平滑过渡效果 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    font-family: 'Roboto', sans-serif;
    font-weight: bold;
}

div.option-item:hover {
    background-color: #e63946; /* 悬停时变色 */
    transform: translateY(-5px) scale(1.05); /* 向上移动并稍微放大 */
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

div.match-button-container {
    text-align: center;
    padding-top: 5vh; /* 调整按钮位置 */
}

.start-match-btn {
    background-color: #1d3557; /* 深蓝色按钮 */
    border-color: #1d3557;
    color: #f1faee;
    font-size: 2rem;
    padding: 15px 40px;
    border-radius: 50px; /* 圆角按钮 */
    transition: all 0.3s ease;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.start-match-btn:hover {
    background-color: #a8dadc; /* 悬停时变色 */
    border-color: #a8dadc;
    color: #1d3557;
    transform: translateY(-3px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

/* 响应式调整 */
@media (max-width: 768px) {
    div.user-panel {
        padding: 10px;
    }
    div.user-photo img {
        width: 8vh;
        height: 8vh;
    }
    div.user-name {
        font-size: 1.2rem;
    }
    div.score {
        font-size: 1rem;
    }
    div.word-display h1 {
        font-size: 10vh;
    }
    div.option-item {
        width: 18vh;
        height: 12vh;
        font-size: 2rem;
        margin: 10px;
    }
    .start-match-btn {
        font-size: 1.5rem;
        padding: 10px 30px;
    }
}

@media (max-width: 576px) {
    div.play-ground {
        flex-direction: column; /* 小屏幕下垂直排列 */
    }
    .row.h-100 {
        flex-direction: column;
    }
    div.user-panel {
        width: 100%;
        height: auto;
        flex-direction: row; /* 用户信息横向排列 */
        justify-content: space-around;
        padding: 10px 0;
    }
    div.user-info {
        display: flex;
        align-items: center;
        gap: 10px;
    }
    div.user-photo {
        margin-bottom: 0;
    }
    div.user-photo img {
        width: 6vh;
        height: 6vh;
    }
    div.user-name, div.score {
        font-size: 1rem;
    }
    div.game-area {
        width: 100%;
        height: auto;
        border-radius: 0;
    }
    div.word-display h1 {
        font-size: 8vh;
    }
    div.option-item {
        width: 15vh;
        height: 10vh;
        font-size: 1.8rem;
        margin: 8px;
    }
    .start-match-btn {
        font-size: 1.2rem;
        padding: 8px 20px;
    }
}

</style>