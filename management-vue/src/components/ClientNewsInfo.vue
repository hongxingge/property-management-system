<template>
  <div class="news-detail">
    <!-- 返回按钮 -->
    <div class="back-bar">
      <el-button
          type="text"
          icon="el-icon-arrow-left"
          @click="goBack"
      >
        返回公告列表
      </el-button>
    </div>

    <!-- 公告内容 -->
    <div class="news-content">
      <h2>{{ currentNews.title }}</h2>

      <div
          class="content"
          v-html="currentNews.content">
      </div>
    </div>
  </div>
</template>

<script>

import {apiGetNewsById} from "@/utils/request";

export default {
  mounted() {
    apiGetNewsById({
      // 通过主界面传过来的 id 获取具体的公告信息
      id: this.$route.query.id
    }).then(res => {
      this.currentNews = res
    }).catch(err => {
      this.$message.error(err);
    })
  },

  data() {
    return {
      currentNews: {
        title: '',
        source: '',
        content: ''
      }
    }
  },

  methods: {
    // 返回公告列表
    goBack() {
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.news-detail {
  width: 100%;
  min-height: 100%;
  padding: 20px;
  box-sizing: border-box;
}

.back-bar {
  width: 80%;
  margin: 0 auto;
  padding-bottom: 10px;
  border-bottom: 1px solid #eeeeee;
}

.news-content {
  width: 80%;
  margin: 20px auto;
}

.news-content h2 {
  text-align: center;
  margin-bottom: 30px;
}

.content {
  line-height: 1.8;
  font-size: 16px;
}
</style>