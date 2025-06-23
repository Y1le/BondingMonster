// import $ from 'jquery';
// import config from '@/config';
// const API_BASE_URL = config.API_BASE_URL;
const ModuleEnglistDanceGame = {
  state: {
    status: "matching", // matching, playing, finished
    socket: null, // WebSocket 实例
    oppoent_name: "", // 对手的名字
    oppoent_photo: "", // 对手的头像
    currentWord: null, // 当前正在匹配的单词
  },
  getters: {
  },
  mutations: {
    updateStatus(state, status) {
      state.status = status;
    },
    updateSocket(state, socket) {
      state.socket = socket;
    },
    updateOppoentName(state, oppoent) {
      state.oppoent_name = oppoent.name;
      state.oppoent_photo = oppoent.photo;
    },
    updateCurrentWord(state, word) {
      state.currentWord = word;
    },
  },
  actions: {
    
  },
  modules: {
  }
};

export default ModuleEnglistDanceGame;
