<template>
  <div>
    <div class="flex-row">
      <el-input v-model="searchContent" placeholder="请输入完整车牌号"></el-input>
      <el-button style="margin-left: 20px" type="primary" @click="search">搜索</el-button>
    </div>
    <div class="flex-x-right" style="margin-top: 15px">
      <el-button style="margin-left: 20px;width: 100px" type="primary" round @click="curData={};isAdd=true;dialogShow=true">添加</el-button>
    </div>

    <div style="margin-top: 10px">
      <el-table
          :header-cell-style="{background:'#f5f5f5'}"
          :data="carData"
          width="100%"
          border>
        <el-table-column
            type="index"
            label="序号"
            width="50"
        />
        <el-table-column
            prop="carNumber"
            label="车牌号"
        />
        <el-table-column
            prop="carBrand"
            label="车品牌"
        />
        <el-table-column
            prop="carType"
            :formatter="carTypeFormat"
            label="车辆类型"
        />

        <el-table-column
            prop="carColor"
            label="车颜色"
        />
        <el-table-column
            prop="ownerName"
            label="业主姓名"
        />
        <el-table-column
            prop="ownerIdCard"
            min-width="130px"
            label="业主身份证号"
        />
        <el-table-column
            width="200"
            label="操作">
          <template slot-scope="scope">
            <el-button
                type="primary"
                @click="clickOperation(scope.row,false)"
                size="mini">修改
            </el-button>
            <el-button
                @click="clickOperation(scope.row,true)"
                type="danger"
                size="mini">删除
            </el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>

    <el-dialog
        :title="isAdd?'添加':'修改'"
        :close-on-click-modal="false"
        :visible.sync="dialogShow"
        width="50%">

      <el-row>
        <el-col :span="LS">业主：</el-col>
        <el-col :span="RS">
          <el-select style="width: 50%" filterable v-model="curData.ownerId" placeholder="请选择业主">
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
        <el-col :span="LS">车类型：</el-col>
        <el-col :span="RS">
          <el-select style="width: 50%" v-model="curData.carType" placeholder="请选择车辆类型">
            <el-option
                v-for="item in carTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="LS">车牌号：</el-col>
        <el-col :span="RS">
          <el-input v-model="curData.carNumber" maxlength="10" placeholder="请输入车牌号"></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="LS">品牌：</el-col>
        <el-col :span="RS">
          <el-input v-model="curData.carBrand" maxlength="10" placeholder="请输入品牌"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="LS">颜色：</el-col>
        <el-col :span="RS">
          <el-input v-model="curData.carColor"  maxlength="10" placeholder="请输入车辆颜色"></el-input>
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
  apiAddOwnerCar,
  apiDeleteOwnerCar,
  apiGetOwnerCar,   apiQueryRoomUser, apiUpdateOwnerCar, getOwnerCarByNumber,
} from "@/utils/request";

export default {
  created() {
    this.getCar()
    this.getRoomUser()
  },
  data() {
    return {
      isAdd: true,
      searchContent: '',
      dialogShow: false,
      LS: 3,
      RS: 21,
      curData: {},
      ownerOptions: [{
        ownerIdCard: '342426187628751832',
        ownerName: '张三'
      }, {
        ownerIdCard: '4352426187628751834',
        ownerName: '李四'
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
      roomUsers:[],
      carData: [
        {
          carNumber: '泸A88888',
          carBrand: '宝马X6',
          carType: 2,
          carColor: 30,
          ownerName: '张三',
          ownerId:1,
          ownerIdCard: '342417823293892382'
        }
      ]
    }
  },
  methods: {
    getCar() {
      apiGetOwnerCar().then(res => {
        this.carData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    getRoomUser() {
      apiQueryRoomUser().then(res => {
        this.roomUsers = res
      }).catch(err => {
        this.$message.error(err)
      })
    },
    clickOperation(row, isDelete) {
      if (isDelete) {
        apiDeleteOwnerCar({id: row.id}).then(res => {
          this.$message.success('删除成功');
          this.getCar()
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
      if (!this.curData.ownerId || !this.curData.carType
          || !this.curData.carNumber|| !this.curData.carBrand|| !this.curData.carColor) {
        this.$message.warning('请输入相关信息');
        return
      }
      if (this.isAdd) {
        apiAddOwnerCar(this.curData).then(res => {
          this.$message.success('添加成功');
          this.dialogShow = false
          this.getCar()
        }).catch(err => {
          this.$message.error(err);
        })
      } else {
        apiUpdateOwnerCar(this.curData).then(res => {
          this.$message.success('修改成功');
          this.dialogShow = false
          this.getCar()
        }).catch(err => {
          this.$message.error(err);
        })
      }
    },
    search() {
      getOwnerCarByNumber({number: this.searchContent}).then(res => {
        this.carData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    carTypeFormat(row) {
      for (const item of this.carTypeOptions) {
        if (row.carType === item.value) {
          return item.label
        }
      }
    }
  }
}
</script>

<style scoped>
</style>
