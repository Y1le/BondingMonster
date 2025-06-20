<template>
    <ConTentBase>
        <div class="row">
            <div class="col-9">
                <div class="row mb-3">
                    <UserProfileWrite v-if="$store.state.user.is_login"  />
                </div>
                <div class="row">
                    <UserProfilePosts :user="user" :posts="posts" @delete_a_post="delete_a_post" />
                </div>
            </div>
            <div class="col-3">
                <UserBoard />
            </div>
        </div>
    </ConTentBase>
</template>

<script>
import { reactive } from 'vue';
import UserProfilePosts from '@/components/UserProfilePosts.vue';
import ConTentBase from '../components/ContentBase.vue';
import UserProfileWrite from '../components/UserProfileWrite.vue';
import UserBoard from '@/components/UserBoard.vue';

export default {
    name: 'HomeView',
    components: {
        ConTentBase,
        UserProfileWrite,
        UserBoard,
        UserProfilePosts
    },
    setup() {
        const posts = reactive({
            posts: [
                { id: 1, content: '这是第一条帖子' , userimage: 'https://img.shetu66.com/2023/07/27/1690436791750269.png' },
                { id: 2, content: '这是第二条帖子' , userimage: 'https://img.shetu66.com/2023/07/27/1690436791750269.png'},
                { id: 3, content: '这是第三条帖子' , userimage: 'https://img.shetu66.com/2023/07/27/1690436791750269.png' },
                { id: 4, content: '这是第四条帖子' , userimage: 'https://img.shetu66.com/2023/07/27/1690436791750269.png' },
                { id: 5, content: '这是第五条帖子' , userimage: 'https://img.shetu66.com/2023/07/27/1690436791750269.png'},
            ],
        });


        return {
        posts,
        }
    }

}
</script>

<style scoped>
.row {
    margin-left: 0;
    margin-right: 0;
}
</style>
