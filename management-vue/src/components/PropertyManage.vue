<template>
  <div>
    <div class="flex-x-right">
      <el-button
          style="margin-bottom: 20px"
          type="success"
          size="medium"
          @click="addAdministrator"
      >添加管理员
      </el-button>
    </div>

    <el-table
        :header-cell-style="{background:'#f5f5f5'}"
        :data="administrators"
        border>
      <el-table-column
          type="index"
          label="序号"
          width="60"
      />
      <el-table-column
          prop="name"
          label="姓名"
      />
      <el-table-column
          prop="phone"
          label="手机号"
      />
      <el-table-column
          label="密码"
      >
        <template ><p>* * * * * *</p></template>
      </el-table-column>
      <el-table-column
          label="操作">
        <template slot-scope="scope">
          <el-button
              type="primary"
              @click="updateAdminClick(scope.row)"
              size="mini">修改
          </el-button>
          <el-button
              type="danger"
              @click="deleteAdminClick(scope.row)"
              size="mini">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
        :close-on-click-modal="false"
        :title="isAdd?'添加管理员':'修改管理员'"
        :visible.sync="dialogVisible"
        @close="currAdministrator ={}"
        width="40%">
      <el-row>
        <el-col :span="leftSpan">姓名：</el-col>
        <el-col :span="rightSpan">
          <el-input v-model="currAdministrator.name"   maxlength="5" placeholder="请输入姓名"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="leftSpan">手机号：</el-col>
        <el-col :span="rightSpan">
          <el-input v-model="currAdministrator.phone"  maxlength="11" placeholder="请输入手机号"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="leftSpan">密码：</el-col>
        <el-col :span="rightSpan">
          <el-input type="password" v-model="currAdministrator.pwd"   maxlength="12" placeholder="请输入密码"></el-input>
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
import {apiAddUser, apiDeleteUserByUid, apiUpdateUserByUid, queryUser} from "@/utils/request";

export default {
  created() {
    this.getUser()
  },
  data() {
    return {
      dialogVisible: false,
      leftSpan: 4,
      rightSpan: 20,
      isAdd: false,
      currAdministrator: {
        uid: '',
        name: '',
        phone: '',
        pwd: '',
        type: 1
      },
      administrators: []
    }
  },
  methods: {
    addAdministrator() {
      this.dialogVisible = true
      this.isAdd = true
    },
    updateAdminClick(row) {
      this.currAdministrator = row
      this.dialogVisible = true
      this.isAdd = false
    },
    deleteAdminClick(row) {
      apiDeleteUserByUid({uid: row.uid}).then(res => {
        this.$message.success('删除成功')
        this.getUser()
      }).catch(err => {
        this.$message.error(err)
      })
    },
    typeFormat(row) {
      switch (row.type) {
        case 0:
          return '保洁'
        case 1:
          return '保安'
        case 2:
          return '物业'
        default :
          return '未知'
      }
    },
    getUser() {
      queryUser().then(res => {
        this.administrators = res
      }).catch(err => {
        this.$message.error(err)
      })
    },
    dialogOk() {
      if (this.isAdd) {
        apiAddUser(this.currAdministrator).then(res => {
          this.dialogVisible = false
          this.$message.success('添加成功')
          this.getUser()
        }).catch(err => {
          this.$message.error(err)
        })
      } else {
        apiUpdateUserByUid(this.currAdministrator).then(res => {
          this.dialogVisible = false
          this.$message.success('修改成功')
          this.getUser()
        }).catch(err => {
          this.$message.error(err)
        })
      }
    }
  }
}
</script>

<style scoped>

</style>
