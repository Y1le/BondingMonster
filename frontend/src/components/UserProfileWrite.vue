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
import { ref } from 'vue';
// import $ from 'jquery';
// import { useStore } from 'vuex';

export default {
    name: "UserProfileWrite",
    setup() {
        let content = ref('');
        return {
            content,
        }
    }
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
