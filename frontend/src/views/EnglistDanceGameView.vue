<template>
    <MatchGround v-if="$store.state.englistdancegame.status === 'matching'" />
    <PlayGround v-if="$store.state.englistdancegame.status === 'playing'"/> 

</template>

<script>
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
        const $store = useStore();
        return {
            $store,
        };
    },
};
</script>



