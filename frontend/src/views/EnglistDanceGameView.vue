<template>
    <MatchGround v-if="$store.state.englistdancegame.status === 'matching'" />
    <PlayGround v-if="$store.state.englistdancegame.status === 'playing'"/> 

</template>

<script>
import { onMounted, onUnmounted } from 'vue';
import MatchGround from '../components/englistdancegame/MatchGround.vue';
import PlayGround from '../components/englistdancegame/PlayGround.vue';
import { useStore } from 'vuex';
export default {
    name: 'EnglistDanceGameView',
    components: {
        PlayGround,
        MatchGround,
    },
    setup() {
        const store = useStore();
        const access = store.state.user.access;
        console.log("access  ", access);
        const socketUrl = `ws://localhost:8080/websocket/${access}`;
        store.commit("updateOppoent", {
            name: "等待对手",
            photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
        })
        let socket = null;
        onMounted(() => {

            
            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                console.log("connected to websocket server");
                store.commit("updateSocket", socket);
            };

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if (data.event === "start-game") {
                    store.commit("updateOppoent", {
                        name: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 1000);
                }
            };

            socket.onclose = () => {
                console.log("WebSocket connection closed");
            };
        });

        onUnmounted(() => {
            socket.close();
            store.commit("updateStatus", "matching");
        })

    },
};
</script>




