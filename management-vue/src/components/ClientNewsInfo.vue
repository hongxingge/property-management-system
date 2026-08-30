<template>
  <div style="padding: 20px;width: 100%">
    <h2 style="text-align: center">{{ currentNews.title }}</h2>
    <div style="width: 80%;margin:20px auto;" v-html="currentNews.content">

    </div>
  </div>
</template>

<script>

import {apiGetNewsById} from "@/utils/request";

export default {
  mounted() {
    apiGetNewsById({//通过主界面传过来的id获取具体的公告信息
      'id': this.$route.query.id
    }).then(res => {
      this.currentNews = res
      this.showNewsDialog = true
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
  methods:{
  }
}
</script>

<style scoped>

</style>
