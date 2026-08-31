<template>
  <div>
    <div class="flex-x-right">
      <el-button
          style="margin-bottom: 20px"
          type="success"
          size="medium"
          @click="addRoomUser"
      >添加业主
      </el-button>
    </div>

    <el-table
        :header-cell-style="{background:'#f5f5f5'}"
        :data="roomUsers"
        border>
      <el-table-column
          type="index"
          label="序号"
          width="60"
      />
      <el-table-column
          prop="name"
          width="80"
          label="姓名"
      />
      <el-table-column
          prop="sex"
          width="60"
          :formatter="sexFormat"
          label="性别"
      />
      <el-table-column
          prop="age"
          width="60"
          label="年龄"
      />
      <el-table-column
          prop="idCard"
          label="身份证号"
      />
      <el-table-column
          prop="phone"
          label="手机号"
      />
      <el-table-column
          prop="roomNumber"
          label="房屋信息"
      />
      <el-table-column
          label="操作">
        <template #default="scope">
          <el-button
              type="primary"
              @click="updateClick(scope.row)"
              size="small">修改
          </el-button>
          <el-button
              type="danger"
              @click="deleteClick(scope.row)"
              size="small">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
        :close-on-click-modal="false"
        :title="isAdd?'添加业主':'修改业主'"
        v-model="dialogVisible"
        @close="currRoomUser ={}"
        width="40%">
      <el-row>
        <el-col :span="leftSpan">姓名：</el-col>
        <el-col :span="rightSpan">
          <el-input v-model="currRoomUser.name" placeholder="请输入姓名"></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="leftSpan">性别：</el-col>
        <el-col :span="rightSpan">
          <el-select v-model.number="currRoomUser.sex" placeholder="请选择">
            <el-option
                v-for="item in sexOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="leftSpan">年龄：</el-col>
        <el-col :span="rightSpan">
          <el-input v-model="currRoomUser.age" maxlength="3" placeholder="请输入年龄"></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="leftSpan">身份证号：</el-col>
        <el-col :span="rightSpan">
          <el-input v-model="currRoomUser.idCard" maxlength="18" placeholder="请输入身份证号"></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="leftSpan">手机号：</el-col>
        <el-col :span="rightSpan">
          <el-input v-model="currRoomUser.phone" maxlength="11" placeholder="请输入手机号"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="leftSpan">房屋信息：</el-col>
        <el-col :span="rightSpan">
          <el-input v-model="currRoomUser.roomNumber" maxlength="30" placeholder="请输入房屋信息（例如：5号楼-6单元-501）"></el-input>
        </el-col>
      </el-row>

      <template #footer>
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogOk">确 定</el-button>
            </template>
    </el-dialog>
  </div>
</template>

<script>
import {
  apiAddRoomUser, apiDeleteRoomUserById,
  apiDeleteUserByUid,
  apiQueryRoomUser, apiUpdateRoomUserById,
  apiUpdateUserByUid
} from "@/utils/request";

export default {
  created() {
    this.getRoomUser()
  },
  data() {
    return {
      dialogVisible: false,
      leftSpan: 4,
      rightSpan: 20,
      isAdd: false,
      currRoomUser: {
        id: '',
        name: '',
        sex: 0,
        age: 0,
        idCard: '',
        phone: '',
        roomNumber: ''
      },
      sexOptions: [
        {
          value: 0,
          label: '男'
        }, {
          value: 1,
          label: '女'
        }
      ],
      roomUsers: []
    }
  },
  methods: {
    addRoomUser() {
      this.dialogVisible = true
      this.isAdd = true
    },
    updateClick(row) {
      this.currRoomUser = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.isAdd = false
    },
    deleteClick(row) {
      apiDeleteRoomUserById({id: row.id}).then(res => {
        this.$message.success('删除成功')
        this.getRoomUser()
      }).catch(err => {
        this.$message.error(err)
      })
    },
    getRoomUser() {
      apiQueryRoomUser().then(res => {
        this.roomUsers = res
      }).catch(err => {
        this.$message.error(err)
      })
    },
    dialogOk() {
      if (this.isAdd) {
        apiAddRoomUser(this.currRoomUser).then(res => {
          this.dialogVisible = false
          this.$message.success('添加成功')
          this.getRoomUser()
        }).catch(err => {
          this.$message.error(err)
        })
      } else {
        apiUpdateRoomUserById(this.currRoomUser).then(res => {
          this.dialogVisible = false
          this.$message.success('修改成功')
          this.getRoomUser()
        }).catch(err => {
          this.$message.error(err)
        })
      }
    },
    sexFormat(row) {
      switch (row.sex) {
        case 0:
          return '男'
        case 1:
          return '女'
        default :
          return '未知'
      }
    }
  }
}
</script>

<style scoped>

</style>
