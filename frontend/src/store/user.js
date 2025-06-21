import $ from 'jquery';
import jwt_decode from 'jwt-decode';
import config from '@/config';
const API_BASE_URL = config.API_BASE_URL;
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
        $.ajax({
            url: `${API_BASE_URL}/api/token/`,
            type: "POST",
            data: {
                username: data.username,
                password: data.password,
            },
            success(resp) {
                const {access, refresh} = resp;
                const access_obj = jwt_decode(access);

                setInterval(() => {
                    $.ajax({
                        url: `${API_BASE_URL}/api/token/refresh/`,
                        type: "POST",
                        data: {
                            refresh,
                        },
                        success(resp) {
                            context.commit('updateAccess', resp.access);
                        }
                    });
                }, 4.5 * 60 * 1000);
                $.ajax({
                    url: `${API_BASE_URL}/myspace/getinfo/`,
                    type: "GET",
                    data: {
                        user_id: access_obj.user_id,
                    },
                    headers: {
                        'Authorization': "Bearer " + access,
                    },
                    success(response) {
                        context.commit("updateUser", {
                            ...response,
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
                context.commit("updateUser", {
                    id:1,
                    username: "mockUser",
                    photo: "https://img.shetu66.com/2023/07/27/1690436791750269.png",
                    followerCount: 0,
                    access: "mock_access_token",
                    refresh: "mock_refresh_token",
                    is_login: true,
                });
            }
        });



        data.success();

        
      },
  },
  modules: {
  }
};

export default ModuleUser;
