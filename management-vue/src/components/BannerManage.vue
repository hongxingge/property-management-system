<template>
  <div>
    <div class="flex-x-right" style="margin-top: 15px">
      <el-button style="margin-left: 20px;width: 100px" type="primary" round
                 @click="isAdd =true;dialogVisible=true">添加
      </el-button>
    </div>
    <el-table
        :header-cell-style="{background:'#f5f5f5'}"
        :data="bannerData"
        style="margin-top: 20px"
        border>
      <el-table-column
          type="index"
          label="序号"
          width="60"
      />
      <el-table-column
          prop="title"
          label="标题"
      />
      <el-table-column
          prop="description"
          label="描述"
      />
      <el-table-column
          prop="deviceStatus"
          label="图片"
      >
        <template slot-scope="scope">
          <img height="100px" :src="scope.row.img">
        </template>
      </el-table-column>
      <el-table-column
          label="操作">
        <template slot-scope="scope">
          <el-button
              type="primary"
              @click="clickUpdateBanner(scope.row)"
              size="mini">修改
          </el-button>
          <el-button
              type="danger"
              @click="clickDeleteBanner(scope.row)"
              size="mini">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
        @close="currentBanner.title='';currentBanner.description=''; currentBanner.img='';  $refs.addImg.value=null"
        :close-on-click-modal="false"
        :title="isAdd?'添加':'修改'"
        :visible.sync="dialogVisible"
        width="40%">
      <el-row>
        <el-col :span="leftSpan">标题：</el-col>
        <el-col :span="rightSpan">
          <el-input v-model="currentBanner.title" placeholder="请输入标题"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="leftSpan">描述：</el-col>
        <el-col :span="rightSpan">
          <el-input v-model="currentBanner.description" placeholder="请输入描述"></el-input>
        </el-col>
      </el-row>

      <el-row class="flex-x-center" style="margin: 20px 0">
        <el-col :span="leftSpan">图片：</el-col>
        <el-col :span="rightSpan">
          <img style="margin-right: 30px" v-show="currentBanner.img" height="100px" :src="currentBanner.img">
          <input ref="addImg" accept="image/*" type="file" id="uploadIMG" @change="uploadImg"/>
        </el-col>
      </el-row>

      <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogOk">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import {apiAddBanner, apiDeleteBanner, apiGetBanner, apiUpdateBanner} from "@/utils/request";
import lrz from "lrz";
import fileToBase64 from "@/utils/base64";
export default {
  created() {
    this.getBanner()
  },
  mounted() {

  },
  data() {
    return {
      dialogVisible: false,
      leftSpan: 3,
      rightSpan: 21,
      isAdd: false,
      currentBanner: {
        id: -1,
        title: '',
        description: '',
        img: '',
      },
      bannerData: []
    }
  },
  methods: {
    uploadImg() {
      let file = this.$refs.addImg.files[0]
      lrz(file, {quality: 0.1}).then(res => {
        console.log("压缩success")
        this.currentBanner.img = res.base64
      }).catch(err => {
        console.log("压缩fail")
        fileToBase64(file, base64 => {
          this.currentBanner.img = base64
        })
      })
    },
    clickUpdateBanner(row) {
      this.currentBanner = JSON.parse(JSON.stringify(row))
      this.isAdd = false
      this.dialogVisible = true
    },
    clickDeleteBanner(row) {
      apiDeleteBanner({'id': row.id}).then(res => {
        this.$message.success('删除成功');
        this.getBanner()
      }).catch(err => {
        this.$message.error(err);
      })
    },
    getBanner() {
      apiGetBanner().then(res => {
        this.bannerData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    dialogOk() {
      if (this.isAdd) {
        apiAddBanner(this.currentBanner).then(res => {
          this.dialogVisible = false
          this.$message.success('添加成功');
          this.getBanner()
        }).catch(err => {
          this.$message.error(err);
        })
      } else {
        apiUpdateBanner(this.currentBanner).then(res => {
          this.$message.success('修改成功');
          this.dialogVisible = false
          this.getBanner()
        }).catch(err => {
          this.$message.error(err);
        })
      }
    }
  }
}
</script>

<style lang="less" scoped>

.el-row {
  display: flex;
  margin-top: 10px;
  justify-content: center;
  align-items: center;
}
</style>
