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
