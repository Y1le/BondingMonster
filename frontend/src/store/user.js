// import $ from 'jquery';
// import jwt_decode from 'jwt-decode';

const ModuleUser = {
  state: {
    id: "",
    username: "",
    photo: "",
    followerCount: 0,
    access: "",
    refresh: "",
    is_login: false,
  },
  getters: {
  },
  mutations: {
      updateUser(state, user) {
          state.id = user.id;
          state.username = user.username;
          state.photo = user.photo;
          state.followerCount = user.followerCount;
          state.access = user.access;
          state.refresh = user.refresh;
          state.is_login = user.is_login;
      },
      updateAccess(state, access) {
          state.access = access;
      },
      logout(state) {
          state.id = "";
          state.username = "";
          state.photo = "";
          state.followerCount = 0;
          state.access = "";
          state.refresh = "";
          state.is_login = false;
      }
  },
  actions: {
      login(context, data) {
        const mockUser = {
        id: 1, // 使用时间戳生成更真实的mock id
        username: data.username || "mockUser", // 使用传入的用户名，或默认值
        photo: "https://img.shetu66.com/2023/07/27/1690436791750269.png",
        followerCount: 0,
        access: "mock_access_token",
        refresh: "mock_refresh_token",
        is_login: true,
        };
        context.commit("updateUser", mockUser);

        data.success();

        
      },
  },
  modules: {
  }
};

export default ModuleUser;
