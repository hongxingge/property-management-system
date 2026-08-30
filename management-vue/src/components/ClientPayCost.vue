<template>
  <div>

    <div class="flex-x-center" style="font-size: 18px;margin-bottom: 20px">
      费用缴纳
    </div>
<!--    <el-empty v-show="pays.length<1" description="暂无数据"></el-empty>-->
    <!--缴费表格-->
    <el-table
        :header-cell-style="{background:'#f5f5f5'}"
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
          width="100"
          :formatter="payStateFormat"
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
              :disabled="scope.row.payState==2"
              @click="pay(scope.row)"
              size="mini">支付
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import {
  apiAddRoomUser, apiDeleteRoomUserById,
  apiDeleteUserByUid, apiGetCost, apiGetCostByUid, apiPay,
  apiQueryRoomUser, apiUpdateRoomUserById, apiUpdateUserById,
  apiUpdateUserByUid
} from "@/utils/request";

export default {
  created() {
    this.getPays()
  },
  data() {
    return {
      pays: []
    }
  },
  methods: {
    getPays() {//根据登录之后保存到本地sessionStorage的userid获取费用信息
      apiGetCostByUid({uid: window.sessionStorage.getItem('userid')}).then(res => {
        this.pays = res
      }).catch(err => {
        this.$message.error(err)
      })
    },
    pay(row) {
      apiPay({id: row.id}).then(res => {//支付接口，点击支付后，修改记录状态并重新刷新费用信息
        this.getPays()
        this.$message.success('支付成功')
      }).catch(err => {
        this.$message.error(err)
      })
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
