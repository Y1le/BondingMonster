<template>
  <div class="media-image">
    <div v-if="images.length > 0" class="image-preview-container">
      <div v-for="(image, index) in images" :key="index" class="image-preview-item">
        <img :src="image.preview" class="preview-image">
        <button @click="removeImage(index)" class="btn-close">
          <i class="bi bi-x"></i>
        </button>
      </div>
    </div>
    <input 
      type="file" 
      ref="fileInput" 
      accept="image/*" 
      multiple 
      @change="handleFileChange"
      style="display: none">
    <button @click="triggerFileInput" class="btn btn-outline-primary">
      <i class="bi bi-image"></i> 添加图片
    </button>
  </div>
</template>

<script>
export default {
  name: 'MediaImage',
  props: {
    value: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      images: []
    }
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleFileChange(e) {
      const files = Array.from(e.target.files);
      files.forEach(file => {
        if (!file.type.startsWith('image/')) return;
        
        const reader = new FileReader();
        reader.onload = (e) => {
          this.images.push({
            file: file,
            preview: e.target.result
          });
          this.$emit('input', this.images);
        };
        reader.readAsDataURL(file);
      });
    },
    removeImage(index) {
      this.images.splice(index, 1);
      this.$emit('input', this.images);
    }
  }
}
</script>

<style scoped>
.media-image {
  margin: 10px 0;
}
.image-preview-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 10px;
}
.image-preview-item {
  position: relative;
  width: 120px;
  height: 120px;
}
.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
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
