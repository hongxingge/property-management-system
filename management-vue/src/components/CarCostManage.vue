<template>
  <div>
    <p>停车费用（每天/单位元）： </p>
    <el-input v-model="cost" type="number" style="width: 200px" placeholder="请输入停车费"></el-input>
    <el-button type="primary" style="margin-left: 30px" @click="updateParkingChargeFun">保存</el-button>
  </div>
</template>

<script>
import {getParkingCharge, updateParkingCharge} from "@/utils/request";

export default {
  name: "CarCostManage",
  created() {
    this.getParkingCharge()
  },
  data() {
    return {
      cost: 5
    }
  },
  methods: {
    getParkingCharge() {
      getParkingCharge().then(res => {
        this.cost = res
      }).catch(err => {
        this.$message.error(err)
      })
    },

    updateParkingChargeFun() {
      if (this.cost <= 0) {
        this.$message.error('费用必须大于0')
        return
      }
      updateParkingCharge({parkingCharge: this.cost}).then(res => {
        this.$message.success('修改成功')
      }).catch(err => {
        this.$message.error(err)
      })
    },
  }
}
</script>

<style scoped>

</style>
