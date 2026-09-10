<template>
  <div>
    <div class="flex-x-right" style="margin-top: 15px">
      <el-button style="margin-left: 20px;width: 100px" type="primary" round @click="clickAdd">添加知识</el-button>
    </div>

    <el-table :header-cell-style="{background:'#f5f5f5'}" style="margin-top: 20px" :data="docs" border>
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="status" width="80" :formatter="statusFormat" label="状态" />
      <el-table-column prop="createdAt" :formatter="formatTime" label="创建时间" />
      <el-table-column label="操作" width="100">
        <template #default="scope">
          <el-button type="danger" @click="clickDelete(scope.row)" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="添加知识" v-model="dialogShow" :close-on-click-modal="false" width="50%">
      <el-form label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="如：停车费标准"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="12"
                    placeholder="知识内容。段落之间空一行会自动切分成多个片段"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogShow = false">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { apiAddKnowledge, apiDeleteKnowledge, apiListKnowledge } from "@/utils/request";

export default {
  created() {
    this.getDocs()
  },
  data() {
    return {
      dialogShow: false,
      docs: [],
      form: { title: '', content: '' }
    }
  },
  methods: {
    getDocs() {
      apiListKnowledge().then(res => {
        this.docs = res
      }).catch(err => {
        this.$message.error(err)
      })
    },
    clickAdd() {
      this.form = { title: '', content: '' }
      this.dialogShow = true
    },
    submit() {
      if (!this.form.title || !this.form.content) {
        this.$message.warning('请填写标题和内容')
        return
      }
      apiAddKnowledge(this.form).then(res => {
        this.$message.success('知识添加成功')
        this.dialogShow = false
        this.getDocs()
      }).catch(err => {
        this.$message.error(err)
      })
    },
    clickDelete(row) {
      apiDeleteKnowledge({ id: row.id }).then(res => {
        this.$message.success('删除成功')
        this.getDocs()
      }).catch(err => {
        this.$message.error(err)
      })
    },
    statusFormat(row) {
      return row.status == 1 ? '启用' : '停用'
    },
    formatTime(row) {
      let date = new Date(row.createdAt)
      let y = date.getFullYear()
      let m = (date.getMonth() + 1 < 10 ? '0' : '') + (date.getMonth() + 1)
      let d = (date.getDate() < 10 ? '0' : '') + date.getDate()
      return y + '-' + m + '-' + d
    }
  }
}
</script>

<style scoped>
</style>