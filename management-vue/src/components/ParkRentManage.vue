<template>
  <div>
    <el-page-header @back="goBack" style="margin-top: 10px;padding: 0px">
    </el-page-header>
    <el-steps style="margin-top: 30px" :active="active" finish-status="success">
      <el-step title="选择车位"></el-step>
      <el-step title="选择业主"></el-step>
      <el-step title="选择车辆"></el-step>
    </el-steps>

    <div style="margin-top: 20px">
      <el-descriptions v-show="active===0" direction="vertical" :column="5" border>
        <el-descriptions-item label="车位编号">{{ selectPark.number }}</el-descriptions-item>
        <el-descriptions-item label="所属停车场">{{ selectPark.carportName }}</el-descriptions-item>
        <el-descriptions-item label="车位类型">{{ parkFormat({type: selectPark.type}) }}</el-descriptions-item>
        <el-descriptions-item label="面积"> {{ selectPark.area }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ selectPark.remark }}</el-descriptions-item>
      </el-descriptions>

      <el-descriptions v-show="active===1" direction="vertical" :column="3" border>
        <el-descriptions-item label="姓名">{{ selectOwner.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ sexFormat({sex: selectOwner.sex}) }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ selectOwner.age }}</el-descriptions-item>
        <el-descriptions-item label="身份证号" :span="2"> {{ selectOwner.idCard }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ selectOwner.phone }}</el-descriptions-item>
      </el-descriptions>

      <el-descriptions v-show="active===2||active===3" direction="vertical" :column="4" border>
        <el-descriptions-item label="车牌号">{{ selectCar.carNumber }}</el-descriptions-item>
        <el-descriptions-item label="品牌">{{ selectCar.carBrand }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ carTypeFormat({carType: selectCar.carType}) }}</el-descriptions-item>
        <el-descriptions-item label="颜色"> {{ selectCar.carColor }}</el-descriptions-item>
      </el-descriptions>

      <div v-show="active===2||active===3" style="margin-top: 10px">
        <div class="block flex-x-center">
          <span class="demonstration">租用时间:</span>
          <el-date-picker
              style="margin-left: 30px"
              v-model="rentTime"
              type="daterange"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
          </el-date-picker>
        </div>
      </div>
    </div>

    <div class="flex-x-around" style="margin-top: 30px">
      <el-button style="width: 20%" type="primary" :disabled="active===0" @click="active--" round>上一步</el-button>
      <el-button style="width:20%" type="primary" round @click="nextStep">{{ active === 3 ? '提交' : '下一步' }}</el-button>
    </div>


    <div class="flex-row" style="margin-top: 20px">
      <el-input v-model="searchContent" :placeholder="placeholder"></el-input>
      <el-button style="margin-left: 20px" type="primary" @click="search">搜索</el-button>
    </div>
    <div v-show="noCar&&active>=2" class="flex-x-right" style="margin-top: 15px">
      <el-button style="margin-left: 20px;width: 100px" type="primary" round @click="curData={};dialogShow=true">
        添加
      </el-button>
    </div>
    <div v-show="active===0" style="margin-top: 10px">
      <el-table
          :header-cell-style="{background:'#f5f5f5'}"
          :data="parkData"
          width="100%"
          border>
        <el-table-column
            type="index"
            label="序号"
            width="50"
        />
        <el-table-column
            prop="number"
            label="车位编号"
        />
        <el-table-column
            prop="carportName"
            label="所属停车场"
        />
        <el-table-column
            prop="type"
            :formatter="parkFormat"
            label="车位类型"
        />

        <el-table-column
            prop="area"
            label="面积"
        />
        <el-table-column
            prop="remark"
            label="备注"
        />
        <el-table-column
            width="100"
            label="操作">
          <template #default="scope">
            <el-button
                type="primary"
                @click="clickOperation(scope.row)"
                size="small">选择
            </el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>
    <div v-show="active===1" style="margin-top: 10px">
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
            label="姓名"
        />
        <el-table-column
            prop="sex"
            :formatter="sexFormat"
            label="性别"
        />
        <el-table-column
            prop="age"
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
            width="100"
            label="操作">
          <template #default="scope">
            <el-button
                type="primary"
                @click="clickOperation(scope.row)"
                size="small">选择
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div v-show="active===2||active===3" style="margin-top: 10px">
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
            width="100"
            label="操作">
          <template #default="scope">
            <el-button
                type="primary"
                @click="clickOperation(scope.row)"
                size="small">选择
            </el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>

    <el-dialog
        title="该业主无车辆信息，需添加车辆"
        :close-on-click-modal="false"
        v-model="dialogShow"
        width="50%">
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
          <el-input v-model="curData.carColor" maxlength="10" placeholder="请输入车辆颜色"></el-input>
        </el-col>
      </el-row>

      <template #footer>
        <el-button @click="dialogShow = false">取 消</el-button>
        <el-button type="primary" @click="operationOk">确 定</el-button>
    </template>
    </el-dialog>
  </div>
</template>

<script>

import {
  apiGetOwnerCarByOwnerId,
  apiGetPark,
  apiQueryRoomUserByCondition,
  apiGetParkByNumber,
  apiQueryRoomUser,
  apiAddRent,
  apiAddOwnerCar,
  getOwnerCarByNumber, apiUpdateRent
} from "@/utils/request";

export default {
  created() {
    let data = this.$route.params.curData
    if (data) {
      this.isAdd = false
      this.id = data.id
      this.selectPark = data.parkBean
      this.selectOwner = data.roomUserBean
      this.selectCar = data.ownerCarBean
      this.rentTime[0] = data.startTime
      this.rentTime[1] = data.endTime
    } else {
      this.curData = {}
    }
    this.getPark()
  },
  data() {
    return {
      id: -1,
      isAdd: true,
      active: 0,
      rentTime: [],
      searchContent: '',
      dialogShow: false,
      placeholder: '请输入完整车位编号',
      LS: 3,
      RS: 21,
      noCar: false,
      curData: {},
      selectPark: {},
      selectOwner: {},
      selectCar: {},
      typeOptions: [
        {
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
      carportData: [],
      roomUsers: [],
      carData: [],
      parkData: [
        {
          number: '342车位',
          carportId: 'A区停车场',
          carportName: 'A区停车场',
          type: 2,
          area: 30,
          remark: '13787431943'
        }
      ]
    }
  },
  watch: {
    active(newValue, oldValue) {
      if (newValue === 0) {
        this.getPark()
        this.placeholder = '请输入完整车位编号'
      } else if (newValue === 1) {
        this.getRoomUser()
        this.placeholder = '请输入姓名或手机号'
      } else if (newValue === 2) {
        this.placeholder = '请输入完整车牌号'
        this.getCar()
      }
    }
  },
  methods: {
    getPark() {
      apiGetPark().then(res => {
        this.parkData = res
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
    getCar() {
      apiGetOwnerCarByOwnerId({ownerId: this.selectOwner.id}).then(res => {
        if (res != null && res.length > 0) {
          this.carData = res
          this.noCar = false
        } else {
          this.noCar = true
          this.dialogShow = true
          this.$message.warning('该业主无车辆信息，请先添加');
        }

      }).catch(err => {
        this.$message.error(err);
      })
    },
    clickOperation(row) {
      if (this.active === 0) {
        this.selectPark = row
        this.active = 1
      } else if (this.active === 1) {
        this.selectOwner = row
        this.active = 2
      } else  {
        this.selectCar = row
        this.active = 3
      }
    },
    parkFormat(row) {
      for (const item of this.typeOptions) {
        if (row.type === item.value) {
          return item.label
        }
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
    },
    carTypeFormat(row) {
      for (const item of this.carTypeOptions) {
        if (row.carType === item.value) {
          return item.label
        }
      }
    },
    search() {
      if (this.active === 0) {
        apiGetParkByNumber({number: this.searchContent}).then(res => {
          this.parkData = res
        }).catch(err => {
          this.$message.error(err);
        })
      } else if (this.active === 1) {
        apiQueryRoomUserByCondition({condition: this.searchContent}).then(res => {
          this.roomUsers = res
        }).catch(err => {
          this.$message.error(err);
        })
      } else if (this.active === 2) {
        getOwnerCarByNumber({number: this.searchContent}).then(res => {
          this.carData = res
        }).catch(err => {
          this.$message.error(err);
        })
      }
    },
    operationOk() {
      this.curData.ownerId = this.selectOwner.id
      if (!this.curData.ownerId || !this.curData.carType
          || !this.curData.carNumber || !this.curData.carBrand || !this.curData.carColor) {
        this.$message.warning('请输入相关信息');
        return
      }
      apiAddOwnerCar(this.curData).then(res => {
        this.$message.success('添加成功');
        this.dialogShow = false
        this.getCar()
      }).catch(err => {
        this.$message.error(err);
      })
    },
    nextStep() {
      if (this.active === 0) {
        if (!this.selectPark.number) {
          this.$message.warning('请先选择车位');
        } else {
          this.active++
        }
        return
      }

      if (this.active === 1) {
        if (!this.selectOwner.name) {
          this.$message.warning('请先选择车主');
        } else {
          this.active++
        }
        return
      }

      if (this.active === 2) {
        if (!this.selectCar.carNumber) {
          this.$message.warning('请先选择车辆');
        } else {
          this.active++
        }
        return
      }
      if (this.active === 3) {
        if (this.rentTime.length < 1) {
          this.$message.success('请选择租用时间');
          return;
        }
        let data = {
          id: this.id,
          carId: this.selectCar.id,
          ownerId: this.selectOwner.id,
          parkId: this.selectPark.id,
          startTime: this.rentTime[0],
          endTime: this.rentTime[1]
        }
        if (this.isAdd) {
          apiAddRent(data).then(res => {
            this.$message.success('添加成功');
            this.$router.go(-1)
          }).catch(err => {
            this.$message.error(err);
          })
        } else {
          apiUpdateRent(data).then(res => {
            this.$message.success('修改成功');
            // this.$router.push({path: '/rent_manage'});
            this.$router.go(-1)
          }).catch(err => {
            this.$message.error(err);
          })
        }

      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
</style>
