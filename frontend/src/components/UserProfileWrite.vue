<template>
  <div class="card edit-field">
    <div class="card-body">
      <label for="edit-post" class="form-label">编辑帖子</label>
      <textarea v-model="content" class="form-control" id="edit-post" rows="3"></textarea>
      <button @click="post_a_post" type="button" class="btn btn-primary btn-sm">发帖</button>
    </div>
  </div>
</template>


<script>
// import { ref } from 'vue';
// import $ from 'jquery';
// import { useStore } from 'vuex';
// import config from '@/config';
// const API_BASE_URL = config.API_BASE_URL;

export default {
    name: "UserProfileWrite",
    // setup(props, context) {
    //   const store = useStore();
    //     let content = ref('');
    // const post_a_post = () => {
    //     if (content.value.trim() === '') {
    //         alert('内容不能为空');
    //         return;
    //     }
    //     $.ajax({
    //         url: `${API_BASE_URL}/api/article/put/`,
    //         type: 'PUT',
    //         data: {
    //             content: content.value,
    //         },  
    //         headers: {
    //           'Authorization': "Bearer " + store.state.user.access,
    //         },
    //         success(response) {
    //             if (response.status === 'success') {
    //                 context.emit('post_a_post', response.data);
    //                 content.value = ""; // 清空输入框
    //             } else {
    //                 alert('发帖失败，请稍后再试。');
    //             }
    //         },
    //       });


    // };
    //   return {
    //       content,
    //       post_a_post,
    //   }
    // }
}
</script>


<style scoped>
.form-label {
    font-weight: bold;
    margin-bottom: 10px;
}
.edit-field {
    margin-top: 20px;
}

button {
    margin-top: 10px;
}
</style>
