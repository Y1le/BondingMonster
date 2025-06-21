<template>
    <div class="card">
        <div class="card-body">
            <div v-for="post in posts.posts" :key="post.id">
                <div class="card single-post">
                    <div class="card-body">
                        <div class="col-1">
                            <img class="img-fluid float-start rounded" :src="post.userimage" alt="加载失败">
                        </div>
                        <div class="col-11 content-field">{{ post.content }}</div>
                        <button @click="delete_a_post(post.id)" v-if="is_me" type="button" class="btn btn-danger btn-sm">删除</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { userStore } from 'vuex';
import { computed } from 'vue';
import $ from 'jquery';
import config from '@/config';
const API_BASE_URL = config.API_BASE_URL;
export default {
    name: "UserProfilePosts",
    props: {
        posts: {
            type: Object,
            required: true,
        },
        user: {
            type: Object,
            required: true,
        }
    },
    setup(props, context){
        const store = userStore();
        let is_me = computed(() => store.state.user.id === props.user.id);

        const delete_a_post = (post_id) => {
            $.ajax({
                url: `${API_BASE_URL}/api/myspace/post/`,
                type: 'DELETE',
                data:{
                    post_id,
                },
                headers: {
                    'Authorization': "Bearer " + store.state.user.access,
                },

                success(response) {
                    if (response.status === 'success') {
                        context.emit('delete_a_post', post_id);
                    } else {
                        alert('删除失败，请稍后再试。');
                    }
                },
                error() {
                    alert('网络错误，请稍后再试。');
                }
            });
        };
        return {
            is_me,
            delete_a_post,
        }
    }
    
    
}
</script>


<style scoped>
.single-post {
    margin-bottom: 10px;
}

button {
    float: right;
}
</style>
