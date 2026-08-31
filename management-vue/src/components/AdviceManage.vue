<template>
  <div>

    <el-table
        :header-cell-style="{background:'#f5f5f5'}"
        :data="advices"
        border>
      <el-table-column
          type="index"
          label="序号"
          width="60"
      />
      <el-table-column
          prop="name"
          label="业主姓名"
      />
      <el-table-column
          prop="phone"
          label="联系方式"
      />
      <el-table-column
          prop="advice"
          label="建议"
      />
      <el-table-column
          prop="files"
          label="图片"
      >
        <template #default="scope">
          <template v-for="(item,index) in getFileList(scope.row.fileNames)">
            <img @click="imgClick(baseUrl+'/user_images?fileName='+item)"
                 style="width: 60px;height: 60px;margin-left: 10px"
                 :src="baseUrl+'/user_images?fileName='+item">
          </template>
        </template>
      </el-table-column>

      <el-table-column
          prop="time"
          :formatter="formatTime"
          label="时间"
      />
    </el-table>


    <el-dialog
        title="图片"
        v-model="imgShow"
        width="50%"
        :close-on-click-modal="false"
    >
      <div class="flex-x-center">
        <img  width="80%" :src="curImg">
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {apiGetAdvice, getBaseUrl} from "@/utils/request";

export default {
  created() {
    this.getAdvice()
    this.baseUrl = getBaseUrl()
  },
  data() {
    return {
      advices: [], dialogShow: false,
      baseUrl: '',
      curImg: '',
      imgShow: false,
    }
  },
  methods: {
    getAdvice() {
      apiGetAdvice().then(res => {
        this.advices = res
      }).catch(err => {
        this.$message.error(err)
      })
    },
    formatTime(row) {
      let date = new Date(row.time);
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();
      month = month < 10 ? "0" + month : month;
      day = day < 10 ? "0" + day : day;
      return year + '-' + month + '-' + day;
    }, getFileList(files) {
      console.log(files)
      if (files) {
        let fileList = files.substring(0, files.lastIndexOf(',')).split(',')
        return fileList
      } else {
        return []
      }
    },
    imgClick(img) {
      this.imgShow = true
      this.curImg = img
    }
  }
}
</script>

<style scoped>

</style>
