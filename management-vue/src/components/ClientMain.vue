<template>
  <div>
    <!--轮播图-->
    <el-carousel indicator-position="outside" type="card">
      <el-carousel-item v-for="(v,i) in bannerData" :key="i">
        <img style="object-fit: cover" width="100%" height="100%" :src="v.img">
        <div style="position:absolute;color: white;bottom: 0;text-align: center;width: 100%">
          <p style="font-size: 25px">{{ v.title }}</p>
          <p>{{ v.description }}</p>
        </div>
      </el-carousel-item>
    </el-carousel>
    <div class="notice-news">
      <!--公告列表-->
      <el-table
          :header-cell-style="{background:'#f5f5f5'}"
          :data="tableData"
          @row-click="rowClick"
          style="width: 100%">
        <el-table-column
            prop="title"
            label="公告"
        >
        </el-table-column>
        <el-table-column
            label=""

            width="140px"
        >
          <template #default="scope">
            <span>{{ formatTime(scope.row) }}</span>
          </template>

        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>

import {apiGetBanner, apiGetNews} from "@/utils/request";

export default {
  created() {
    this.getBanner()
    this.getNews()
  },
  data() {
    return {
      bannerData: [],
      tableData: []
    }
  }, methods: {
    getBanner() {//从后台获取轮播图信息
      apiGetBanner().then(res => {
        this.bannerData = res
      }).catch(err => {
        this.$message.error(err);
      })
    }, formatTime(item) {//时间格式
      let date = new Date(item.time)
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();
      month = month < 10 ? "0" + month : month;
      day = day < 10 ? "0" + day : day;
      return year + '-' + month + '-' + day
    },
    getNews() {//从后台获取公告信息
      apiGetNews().then(res => {
        this.tableData = res;
      }).catch(err => {
        this.$message.error(err);
      })
    },
    rowClick(row) {//跳转公告具体信息界面(路由跳转并且把id传过去)
      this.$router.push({
        path: '/news_info',
        query: {
          id: row.id
        }
      })
    }
  }
}
</script>

<style scoped>
.notice-news {
  border: 1px solid #DDDDDD;
}
</style>
