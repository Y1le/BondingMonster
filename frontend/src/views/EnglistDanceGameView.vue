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
        const socketUrl = `ws://localhost:8080/websocket/${store.state.user.id}`;
        store.commit("updateOppoent", {
            name: "等待对手",
            photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
        })
        let socket = null;
        onMounted(() => {

            
            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                console.log("connected to websocket server");
                store.commit("updateSoket", socket);
                // console.log( socket);
            };

            socket.onmessage = msg => {
                const data = JSON.parse(msg.date);
                if (data.event === "matched") {
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




