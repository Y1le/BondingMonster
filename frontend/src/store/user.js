import $ from 'jquery';
import config from '@/config';
import router from '@/router';

const API_BASE_URL = config.API_BASE_URL;
const ModuleUser = {
  state: {
    id: "",
    username: "",
    photo: "",
    randingCount: 0, // EnglistDanceGame 的排位分数
    followerCount: 0,
    access: "",
    refresh: "",
    is_login: false,
    pulling_info: false,  // 是否正在从云端拉取信息
  },
  getters: {
  },
  mutations: {
      updateUser(state, user) {
          state.id = user.id;
          state.username = user.username;
          state.photo = user.photo;
          state.randingCount = user.rankdingCount;
          state.followerCount = user.followerCount;
          state.access = user.access;
          state.refresh = user.refresh;
          state.is_login = user.is_login;
      },
      updateAccess(state, access) {
          state.access = access;
      },
      logout(state) {
          console.log("Logout");
          state.id = "";
          state.username = "";
          state.photo = "";
          state.randingCount = 0;
          state.followerCount = 0;
          state.access = "";
          state.refresh = "";
          state.is_login = false;

        router.push({ name: 'home' });
      },
        updatePullingInfo(state, pulling_info) {
            state.pulling_info = pulling_info;
        }

  },
  actions: {
      login(context, data) {
        $.ajax({
            url: `${API_BASE_URL}/api/token`,
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                username: data.username,
                password: data.password,
            }),
            success(response) {
                console.log("Login success");
                console.log(response);
                const {access, refresh} = response;
                localStorage.setItem("jwt_token", refresh);
                setInterval(() => {
                    $.ajax({
                        url: `${API_BASE_URL}/api/token/refresh`,
                        type: "POST",
                        contentType: "application/json", // <--- 明确指定 Content-Type
                        data: JSON.stringify({
                            refresh,
                        }),
                        success(response) {
                            context.commit('updateAccess', response.access);
                        }
                    });
                }, 4.5 * 60 * 1000);
                
                $.ajax({
                    url: `${API_BASE_URL}/myspace/getinfo`,
                    type: "GET",
                    data: {
                        user_id: response.id,
                    },
                    headers: {
                        'Authorization': "Bearer " + response.access,
                    },
                    success(response) {
                        context.commit("updateUser", {
                            ...response,
                            randingCount: response.rankdingCount|0, 
                            access: access,
                            refresh: refresh,
                            is_login: true,
                        });
                        data.success();
                    },
                })
            },
            error() {
                data.error();
                // console.error("Login failed");
                // context.commit("updateUser", {
                //     id:1,
                //     username: "mockUser",
                //     photo: "https://img.shetu66.com/2023/07/27/1690436791750269.png",
                //     followerCount: 0,
                //     access: "mock_access_token",
                //     refresh: "mock_refresh_token",
                //     is_login: true,
                // });
            }
        });
        data.success();
      },
    logout(context) {
        localStorage.removeItem("jwt_token");// 登出时清除本地存储的 token
        context.commit("logout");
    },
    refreshToken(context) {
        const refresh = localStorage.getItem("jwt_token");
        
        $.ajax({
            url: `${API_BASE_URL}/api/token/refresh`,
            type: "POST",
            contentType: "application/json", // <--- 明确指定 Content-Type
            data: JSON.stringify({
                refresh,
            }),
            success(response) {
                context.commit('updateAccess', response.access);
                console.log("refreshToken success:", response.access);
                $.ajax({
                    url: `${API_BASE_URL}/myspace/getinfo`,
                    type: "GET",
                    data: {
                        user_id: "0",
                    },
                    headers: {
                        'Authorization': "Bearer " + response.access,
                    },
                    success(response) {
                        console.log("user success:", response);
                        context.commit("updateUser", {
                            ...response,
                            randingCount: response.rankdingCount|0, 
                            access: response.access,
                            refresh: refresh,
                            is_login: true,
                        });
                    },
                })
            }
        });
    }
  },
  modules: {
  }
};

export default ModuleUser;
