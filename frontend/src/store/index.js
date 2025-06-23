import { createStore } from 'vuex'
import ModuleUser from './user';
import ModuleEnglistDanceGame from './englistdancegame';

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    englistdancegame: ModuleEnglistDanceGame,
  }
});
