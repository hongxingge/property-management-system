<template>
  <div>
    <div class="flex-x-center" style="font-size: 18px;margin-bottom: 20px">
      投诉建议
    </div>

    <div style="width: 600px;margin: 0 auto">
      <p>描述:</p>
      <el-input
          style="width: 100%"
          type="textarea"
          :rows="8"
          placeholder="请输入您的建议"
          v-model="advice">
      </el-input>
      <p>图片:</p>
      <el-upload
          action="http://localhost:8899/management/fileUpload"
          list-type="picture-card"
          name="file"
          :on-success="uploadFileSuccess"
          :on-exceed="exceedImg"
          :limit="3"
          :on-remove="handleRemove">
        <i class="el-icon-plus"></i>
      </el-upload>
      <div class="flex-x-center">
        <el-button style="margin-top: 20px;width: 100%" class="flex-x-center" size="medium" type="primary"
                   @click="commit">提交
        </el-button>
      </div>

    </div>

  </div>
</template>

<script>
import {apiAddAdvice} from "@/utils/request";

export default {
  data() {
    return {
      advice: '',
      dialogImageUrl: '',
      uploadLists: []
    };
  }
  ,
  methods: {
    commit() {
      let strs = ''
      for (const item of this.uploadLists) {
        strs += item.newFileName + ','
      }
      let commitData = {
        uid: window.sessionStorage.getItem('userid'),
        advice: this.advice,
        fileNames: strs
      }
      apiAddAdvice(commitData).then(res => {
        this.$message.success('提交成功');
        this.dialogShow = false
      }).catch(err => {
        this.$message.error(err);
      })
    }, handleRemove(file, fileList) {
      let index = 0
      for (let i = 0; i < this.uploadLists.length; i++) {
        if (this.uploadLists[i].fileName === file.name) {
          index = i
        }
      }
      this.uploadLists.splice(index, 1)
    }, uploadFileSuccess(response, file, fileList) {
      console.log(file.name)
      this.uploadLists.push({
        fileName: file.name,
        newFileName: response.data
      })
    }
    ,
    exceedImg() {
      this.$message.info('最多上传3张图片');
    }
  }
}
;
</script>

<style scoped>
p {
  font-size: 14px;
  color: #8492a6;
}
</style>
