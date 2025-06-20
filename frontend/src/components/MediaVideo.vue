<template>
  <div class="media-video">
    <div v-if="videos.length > 0" class="video-preview-container">
      <div v-for="(video, index) in videos" :key="index" class="video-preview-item">
        <video controls class="preview-video">
          <source :src="video.preview" :type="video.type">
        </video>
        <button @click="removeVideo(index)" class="btn-close">
          <i class="bi bi-x"></i>
        </button>
      </div>
    </div>
    <input 
      type="file" 
      ref="fileInput" 
      accept="video/*" 
      multiple 
      @change="handleFileChange"
      style="display: none">
    <button @click="triggerFileInput" class="btn btn-outline-primary">
      <i class="bi bi-film"></i> 添加视频
    </button>
  </div>
</template>

<script>
export default {
  name: 'MediaVideo',
  props: {
    value: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      videos: []
    }
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleFileChange(e) {
      const files = Array.from(e.target.files);
      files.forEach(file => {
        if (!file.type.startsWith('video/')) return;
        
        const reader = new FileReader();
        reader.onload = (e) => {
          this.videos.push({
            file: file,
            preview: e.target.result,
            type: file.type
          });
          this.$emit('input', this.videos);
        };
        reader.readAsDataURL(file);
      });
    },
    removeVideo(index) {
      this.videos.splice(index, 1);
      this.$emit('input', this.videos);
    }
  }
}
</script>

<style scoped>
.media-video {
  margin: 10px 0;
}
.video-preview-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 10px;
}
.video-preview-item {
  position: relative;
  width: 200px;
}
.preview-video {
  width: 100%;
  max-height: 200px;
  border-radius: 4px;
  background-color: #000;
}
.btn-close {
  position: absolute;
  top: 5px;
  right: 5px;
  background: rgba(0,0,0,0.5);
  border: none;
  color: white;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
