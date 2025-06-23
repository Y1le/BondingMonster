<template>
  <div class="match-ground">
    <div class="row h-100 justify-content-center align-items-center">
        <div class="col-md-5 col-12 user-match-card">
            <div class="user-photo">
                <img :src="$store.state.user.photo" alt="我的头像-加载失败">    
            </div>
            <div class="user-name">{{$store.state.user.username}}</div>
        </div>
        <div class="col-md-5 col-12 user-match-card">
            <div class="user-photo opponent-photo">
                <img :src="$store.state.englistdancegame.oppoent_photo" alt="对手图片-加载失败">    
            </div>
            <div class="user-name opponent-name">{{ $store.state.englistdancegame.oppoent_name }}</div>
        </div>
        <div class="col-12 text-center match-button-container">
            <button  @click="click_match_btn" type="button" class="btn match-action-btn">{{ match_btn_info }}</button>
        </div>
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex';
import { ref } from 'vue';
export default {
    name: 'MatchGround',
    setup() {
        const store = useStore(); 
        let match_btn_info = ref("开始匹配"); 

        const click_match_btn = () => {
            if (match_btn_info.value === "开始匹配") {
                store.state.englistdancegame.soket.send(JSON.stringify({
                    event:"start-matching",
                }));
                match_btn_info.value = "取消匹配"; // 点击后变为“取消匹配”
            } else {
                match_btn_info.value = "开始匹配"; // 点击后变为“开始匹配”
                store.state.englistdancegame.soket.send(JSON.stringify({
                    event:"stop-matching",
                }));
            }
        }
        return {
            match_btn_info,
            click_match_btn,
        }
    }
}
</script>

<style scoped>
/* 引入 Google Fonts (保持一致性，尽管图片中字体可能不同) */
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');

div.match-ground {
    width: 60vw; /* 调整宽度 */
    height: 70vh; /* 调整高度 */
    margin: 40px auto;
    background-color: #F0F0F0; /* 浅灰色背景，更接近图片 */
    border-radius: 15px; /* 圆角边框 */
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1); /* 柔和的阴影 */
    display: flex;
    align-items: center; /* 垂直居中 */
    justify-content: center; /* 水平居中 */
    padding: 20px; /* 内部填充 */
}

.row.h-100 {
    height: 100%;
    width: 100%;
}

/* 移除标题样式，因为图片中没有标题 */
/* .match-title h1 { ... } */

div.user-match-card {
    background-color: transparent; /* 背景透明，因为图片中卡片没有独立背景色 */
    border-radius: 0; /* 移除圆角 */
    padding: 0; /* 移除内边距 */
    box-shadow: none; /* 移除阴影 */
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin: 0; /* 移除左右间距 */
    transition: none; /* 移除过渡效果 */
}

div.user-photo {
    text-align: center;
    margin-bottom: 15px; /* 调整与名字的间距 */
    padding-top: 5vh; /* 调整头像距离顶部的距离 */
}

div.user-photo img {
    width: 18vh; /* 调整头像大小 */
    height: 18vh;
    border-radius: 50%;
    border: 4px solid #E63946; /* 红色边框 */
    object-fit: cover;
    box-shadow: none; /* 移除头像阴影 */
}

div.user-name {
    text-align: center;
    font-size: 1.6rem; /* 调整名字字体大小 */
    color: #333; /* 深色文字 */
    font-family: 'Roboto', sans-serif;
    font-weight: normal; /* 正常字重 */
    text-shadow: none; /* 移除文字阴影 */
}

/* 移除 VS 文本样式，因为图片中没有 VS */
/* .vs-text { ... } */

div.match-button-container {
    text-align: center;
    padding-top: 10vh; /* 调整按钮距离上方内容的距离 */
    width: 100%; /* 确保按钮容器占据整行 */
}

.match-action-btn {
    background-color: #FFC107; /* 黄色背景，与图片中的按钮颜色一致 */
    border-color: #FFC107;
    color: #333; /* 深色文字 */
    font-size: 1.8rem; /* 调整字体大小 */
    padding: 12px 35px; /* 调整内边距 */
    border-radius: 8px; /* 稍微圆润的边角 */
    transition: all 0.2s ease;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); /* 柔和的阴影 */
    font-family: 'Roboto', sans-serif;
    font-weight: bold;
    letter-spacing: 1px;
}

.match-action-btn:hover {
    background-color: #e0a800; /* 悬停时颜色变深 */
    border-color: #e0a800;
    color: #fff; /* 悬停时文字变白 */
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

/* 响应式调整 */
@media (max-width: 768px) {
    div.match-ground {
        width: 90vw;
        height: auto;
        padding: 15px;
    }
    .row.h-100 {
        flex-direction: column; /* 在小屏幕上垂直排列 */
    }
    div.user-match-card {
        margin-bottom: 20px; /* 增加卡片间距 */
    }
    div.user-photo img {
        width: 15vh;
        height: 15vh;
    }
    div.user-name {
        font-size: 1.4rem;
    }
    div.match-button-container {
        padding-top: 5vh;
    }
    .match-action-btn {
        font-size: 1.5rem;
        padding: 10px 30px;
    }
}

@media (max-width: 576px) {
    div.user-photo img {
        width: 12vh;
        height: 12vh;
    }
    div.user-name {
        font-size: 1.2rem;
    }
    .match-action-btn {
        font-size: 1.2rem;
        padding: 8px 25px;
    }
}
</style>