<template>
  <ContentBase>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="register">
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input v-model="username" type="text" class="form-control" id="username">
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input v-model="password" type="password" class="form-control" id="password">
          </div>
          <div class="mb-3">
            <label for="password_confirm" class="form-label">确认密码</label>
            <input v-model="password_confirm" type="password" class="form-control" id="password_confirm">
          </div>
          <div class="error-message">{{ error_message }}</div>
          <button type="submit" class="btn btn-primary">登录</button>
        </form>
      </div>
    </div>
  </ContentBase>
</template>

<script>
import ContentBase from '../components/ContentBase'
import { ref } from 'vue';
import { useStore } from 'vuex';
import router from '../router/index';
import $ from 'jquery';
import config from '@/config';
const API_BASE_URL = config.API_BASE_URL;
export default {
  name: 'RegisterView',
  components: {
      ContentBase,
  },
  setup() {
    const store = useStore();
    let username = ref('');
    let password = ref('');
    let password_confirm = ref('');
    let error_message = ref('');
    
    const register = () => {
      error_message.value = "";
      if (password.value!== password_confirm.value) {
        error_message.value = "两次密码输入不一致";
        return;
      }
      

      $.ajax({
        url: `${API_BASE_URL}/api/register`,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
                username: username.value,
                password: password.value,
        }),
        success(resp) {
          if (resp.error_message === "success") {
            store.dispatch("login", {
              username: username.value,
              password: password.value,
              success() {
                router.push({name: 'home'});
              },
              error() {
                error_message.value = "系统异常，请稍后重试";
              }
            });
          } else {
            error_message.value = resp.error_message;
          }
        }
      })
    };

    return {
      username,
      password,
      password_confirm,
      error_message,
      register,
    }
  }
}
</script>

<style scoped>
button {
  width: 100%;
}

.error-message {
  color: red;
}
</style>
