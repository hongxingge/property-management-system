<template>
  <div>
    <div class="flex-x-right" style="margin-top: 15px">
      <el-button style="margin-left: 20px;width: 100px" type="primary" round
                 @click="clickAdd">添加
      </el-button>
    </div>

    <el-table
        :header-cell-style="{background:'#f5f5f5'}"
        style="margin-top: 20px"
        :data="pays"
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
          prop="phone"
          label="手机号"
      />
      <el-table-column
          prop="roomNumber"
          label="房屋信息"
      />
      <el-table-column
          prop="payType"
          width="100"
          :formatter="payTypeFormat"
          label="账单类别"
      />
      <el-table-column
          prop="payMoney"
          label="缴费金额"
      />
      <el-table-column
          :formatter="payStateFormat"
          width="100"
          label="支付状态"
      />
      <el-table-column
          width="100"
          :formatter="isCheckFormat"
          label="是否审核"
      />
      <el-table-column
          prop="time"
          :formatter="formatTime"
          label="添加时间"
      />
      <el-table-column
          label="操作">
        <template slot-scope="scope">
          <el-button
              type="primary"
              @click="clickOperation(scope.row,false)"
              size="mini">修改
          </el-button>
          <el-button
              type="danger"
              @click="clickOperation(scope.row,true)"
              size="mini">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
        :title="isAdd?'添加':'修改'"
        :visible.sync="dialogShow"
        :close-on-click-modal="false"
        width="40%">

      <el-row>
        <el-col :span="LS">业主：</el-col>
        <el-col :span="RS">
          <el-select style="width: 100%" filterable v-model="curData.uid" placeholder="请选择业主">
            <el-option
                v-for="item in roomUsers"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.idCard }}</span>
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="LS">账单类型：</el-col>
        <el-col :span="RS">
          <el-select style="width: 100%" v-model="curData.payType" placeholder="请选择账单类型">
            <el-option
                v-for="item in payTypeOption"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row v-show="!isAdd">
        <el-col :span="LS">支付状态：</el-col>
        <el-col :span="RS">

          <el-select style="width: 100%" v-model="curData.payState" placeholder="请选择支付状态">
            <el-option
                v-for="item in payStateOption"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row v-show="!isAdd && curData.payState==2">
        <el-col :span="LS">是否审核：</el-col>
        <el-col :span="RS">
          <el-select style="width: 100%" v-model="curData.isCheck" placeholder="请选择是否审核">
            <el-option
                v-for="item in isCheckOption"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="LS">金额：</el-col>
        <el-col :span="RS">
          <el-input style="width: 100%" type="number" v-model="curData.payMoney"  maxlength="8" placeholder="请输入金额"></el-input>
        </el-col>
      </el-row>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogShow = false">取 消</el-button>
        <el-button type="primary" @click="operationOk">确 定</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>

import {
  apiAddCost,
  apiAddPark, apiDeleteCost,
  apiDeletePark, apiGetCarport, apiGetCost,
  apiGetPark, apiGetParkByNumber, apiQueryRoomUser, apiUpdateCost,
  apiUpdatePark
} from "@/utils/request";

export default {
  created() {
    this.getPays()
    this.getRoomUser()
  },
  data() {
    return {
      isAdd: true,
      dialogShow: false,
      LS: 4,
      RS: 20,
      curData: {},
      payTypeOption: [{
        value: 1,
        label: '物业费'
      }, {
        value: 2,
        label: '停车费'
      }],
      payStateOption: [{
        value: 1,
        label: '未支付'
      }, {
        value: 2,
        label: '已支付'
      }],
      isCheckOption: [{
        value: 1,
        label: '未审核'
      }, {
        value: 2,
        label: '已审核'
      }],
      roomUsers: [],
      pays: []
    }
  },
  methods: {
    getPays() {
      apiGetCost().then(res => {
        this.pays = res
      }).catch(err => {
        this.$message.error(err)
      })
    }, getRoomUser() {
      apiQueryRoomUser().then(res => {
        this.roomUsers = res
      }).catch(err => {
        this.$message.error(err)
      })
    },
    clickAdd() {
      this.curData = {};
      this.curData.payState = 2
      this.curData.isCheck = 2
      this.isAdd = true
      this.dialogShow = true
    },
    clickOperation(row, isDelete) {
      if (isDelete) {
        apiDeleteCost({id: row.id}).then(res => {
          this.$message.success('删除成功');
          this.getPays()
        }).catch(err => {
          this.$message.error(err);
        })
      } else {
        this.isAdd = false
        this.curData = JSON.parse(JSON.stringify(row))
        this.dialogShow = true;
      }
    },
    operationOk() {
      if (!this.curData.uid || !this.curData.payMoney ||
          !this.curData.payState || !this.curData.payType ||
          !this.curData.isCheck) {
        this.$message.warning('请输入相关信息');
        return
      }
      if (this.isAdd) {
        apiAddCost(this.curData).then(res => {
          this.$message.success('添加成功');
          this.dialogShow = false
          this.getPays()
        }).catch(err => {
          this.$message.error(err);
        })
      } else {
        apiUpdateCost(this.curData).then(res => {
          this.$message.success('修改成功');
          this.dialogShow = false
          this.getPays()
        }).catch(err => {
          this.$message.error(err);
        })
      }
    },
    payTypeFormat(row) {
      switch (row.payType) {
        case 1:
          return '物业费'
        case 2:
          return '停车费'
      }
    },
    payStateFormat(row) {
      switch (row.payState) {
        case 2:
          return '已支付'
        case 1:
          return '未支付'
      }
    },
    isCheckFormat(row) {
      switch (row.isCheck) {
        case 2:
          return '已审核'
        case 1:
          return '未审核'
      }
    },
    formatTime(row) {
      let date = new Date(row.time);
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();
      month = month < 10 ? "0" + month : month;
      day = day < 10 ? "0" + day : day;
      return year + '-' + month + '-' + day;
    }
  }
}
</script>

<style scoped>
</style>
