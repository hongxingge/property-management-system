<template>
  <div>
    <div class="flex-row">
      <el-input v-model="searchContent" placeholder="请输入完整车位编号"></el-input>
      <el-button style="margin-left: 20px" type="primary" @click="search">搜索</el-button>
    </div>
    <div class="flex-x-right" style="margin-top: 15px">
      <el-button style="margin-left: 20px;width: 100px" type="primary" @click="addRent" round>添加
      </el-button>
    </div>

    <div style="margin-top: 10px">
      <el-table
          :header-cell-style="{background:'#f5f5f5'}"
          :data="rentData"
          width="100%"
          border>
        <el-table-column
            type="index"
            label="序号"
            width="50"
        />
        <el-table-column
            prop="parkBean.number"
            label="车位编号"
        />
        <el-table-column
            prop="parkBean.carportName"
            label="所属停车场"
        />
        <el-table-column
            prop="parkBean.type"
            :formatter="parkFormat"
            label="车位类型"
        />

        <el-table-column
            prop="ownerCarBean.carNumber"
            label="车牌号"
        />
        <el-table-column
            prop="ownerCarBean.carBrand"
            label="车品牌"
        />
        <el-table-column
            prop="ownerCarBean.carType"
            :formatter="carTypeFormat"
            label="车辆类型"
        />

        <el-table-column
            prop="roomUserBean.name"
            label="业主姓名"
        />
        <el-table-column
            prop="roomUserBean.phone"
            width="110"
            label="手机号"
        />
        <el-table-column
            label="租用状态"
            width="70"
        >
          <template slot-scope="scope">
            <el-popover
                placement="top-start"
                title="租用时间"
                width="200"
                trigger="hover"
                :content="scope.row.startTime+' ~ '+scope.row.endTime">
              <el-tag slot="reference">{{ rentState(scope.row) }}</el-tag>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column
            width="220"
            label="操作">
          <template slot-scope="scope">
            <el-button
                type="success"
                @click="clickOperation(scope.row,0)"
                size="mini">详情
            </el-button>
            <el-button
                type="primary"
                @click="clickOperation(scope.row,1)"
                size="mini">修改
            </el-button>
            <el-button
                @click="clickOperation(scope.row,2)"
                type="danger"
                size="mini">删除
            </el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>
    <el-dialog
        title="详情"
        :visible.sync="infoShow"
        :close-on-click-modal="true"
        width="70%">
      <h4>租用时间</h4>
      <el-descriptions direction="vertical" :column="2" border>
        <el-descriptions-item label="开始时间">{{ curData.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ curData.endTime }}</el-descriptions-item>
      </el-descriptions>
      <h4>车位信息</h4>
      <el-descriptions direction="vertical" :column="5" border>
        <el-descriptions-item label="车位编号">{{ curData.parkBean.number }}</el-descriptions-item>
        <el-descriptions-item label="所属停车场">{{ curData.parkBean.carportName }}</el-descriptions-item>
        <!--        row.parkBean.type-->
        <el-descriptions-item label="车位类型">{{ parkFormat(curData) }}</el-descriptions-item>
        <el-descriptions-item label="面积"> {{ curData.parkBean.area }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ curData.parkBean.remark }}</el-descriptions-item>
      </el-descriptions>
      <h4>业主信息</h4>
      <el-descriptions direction="vertical" :column="3" border>
        <el-descriptions-item label="姓名">{{ curData.roomUserBean.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ sexFormat(curData.roomUserBean) }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ curData.roomUserBean.age }}</el-descriptions-item>
        <el-descriptions-item label="身份证号" :span="2"> {{ curData.roomUserBean.idCard }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ curData.roomUserBean.phone }}</el-descriptions-item>
      </el-descriptions>
      <h4>车辆信息</h4>
      <el-descriptions direction="vertical" :column="4" border>
        <el-descriptions-item label="车牌号">{{ curData.ownerCarBean.carNumber }}</el-descriptions-item>
        <el-descriptions-item label="品牌">{{ curData.ownerCarBean.carBrand }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{
            carTypeFormat(curData)
          }}
        </el-descriptions-item>
        <el-descriptions-item label="颜色"> {{ curData.ownerCarBean.carColor }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>

import {
  apiAddPark,
  apiDeletePark, apiDeleteRent, apiGetCarport,
  apiGetPark, apiGetParkByNumber, apiGetRent, apiSearchRent,
  apiUpdatePark
} from "@/utils/request";

export default {
  created() {
    this.getRent()
  },
  data() {
    return {
      isAdd: true,
      searchContent: '',
      dialogShow: false,
      infoShow: false,
      LS: 4,
      RS: 20,
      curData: {
        parkBean: {},
        roomUserBean: {},
        ownerCarBean: {},
        payCostBean: {}
      },
      typeOptions: [{
        value: 1,
        label: '普通车位'
      }, {
        value: 2,
        label: '子母车位'
      }, {
        value: 3,
        label: '豪华车位'
      }],
      carTypeOptions: [
        {
          value: 1,
          label: '家用小汽车'
        }, {
          value: 2,
          label: '客车'
        }, {
          value: 3,
          label: '货车'
        }, {
          value: 4,
          label: '电动车'
        }, {
          value: 5,
          label: '三轮车'
        }],
      rentData: []
    }
  },
  methods: {
    getRent() {
      apiGetRent().then(res => {
        this.rentData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    clickOperation(row, index) {
      if (index === 0) {
        this.curData = JSON.parse(JSON.stringify(row))
        this.infoShow = true
      } else if (index === 1) {
        if (row.payCostBean.payState === 2) {
          this.$message.info('该车位已付款，暂无法修改');
          return
        }
        // this.isAdd = false
        // this.curData = JSON.parse(JSON.stringify(row))
        // this.dialogShow = true;
        this.$router.push({name: 'ParkRentManage', params: {curData: row}});
      } else if (index === 2) {
        apiDeleteRent({id: row.id}).then(res => {
          this.$message.success('删除成功');
          this.getRent()
        }).catch(err => {
          this.$message.error(err);
        })

      }
    },
    addRent() {
      this.$router.push({name: 'ParkRentManage'});
    },
    search() {
      apiSearchRent({number: this.searchContent}).then(res => {
        this.rentData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    parkFormat(row) {
      for (const item of this.typeOptions) {
        if (row.parkBean.type === item.value) {
          return item.label
        }
      }
    }, carTypeFormat(row) {
      for (const item of this.carTypeOptions) {
        if (row.ownerCarBean.carType === item.value) {
          return item.label
        }
      }
    }, rentState(row) {
      let date = new Date()
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();
      month = month < 10 ? "0" + month : month;
      day = day < 10 ? "0" + day : day;
      let curStr = year + '-' + month + '-' + day
      let curTime = new Date(curStr).getTime()
      let startTime = new Date(row.startTime).getTime()
      let endTime = new Date(row.endTime).getTime()
      if (endTime < curTime) {
        return '已到期'
      }
      if (startTime > curTime) {
        return '未开始'
      }
      if (startTime <= curTime && curTime <= endTime) {
        return '在租'
      }


    }, sexFormat(row) {
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
