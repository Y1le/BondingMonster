<template>
    <ContentBase>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">username</label>
                        <input v-model="username" type="text"  id="username" class="form-control" >
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">password</label>
                        <input v-model="password" type="password"  id="password" class="form-control" >
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">submit</button>
                </form>
            </div>
        </div>
    </ContentBase>
</template>

<script>
import ContentBase from '../components/ContentBase.vue';
import { ref } from 'vue';
import { useStore } from 'vuex';    
import router from '../router';

export default {
    name: 'LoginView',
    components: {
        ContentBase
    },
    setup() {
        const store = useStore();
        const username = ref('');
        const password = ref('');
        const error_message = ref('');

        const login = () => {
            error_message.value = '';
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success: () => {
                    router.push({ name: 'home' });
                },
                error(){
                    error_message.value = '登录失败，请检查用户名和密码';
                }
            });
        };
        return {
            username,
            password,
            error_message,
            login
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
