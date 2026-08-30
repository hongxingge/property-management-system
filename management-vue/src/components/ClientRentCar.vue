<template>
  <div>
    <div class="flex-x-center" style="font-size: 18px;margin-bottom: 20px">
      租车位
    </div>
    <div class="flex-x-between">
      <div style="font-size: 15px;margin: 20px 0 10px 0">已租车位</div>
      <el-button v-if="noCar" size="mini" type="primary" style="width: 80px;height: 30px;margin-left: 20px" round
                 @click="dialogShow=true">添加车辆
      </el-button>

      <el-button v-if="!noCar" size="mini" type="primary" style="width: 80px;height: 30px;margin-left: 20px" round
                 @click="selectRent=!selectRent">租车位
      </el-button>
    </div>
    <!--车位表格-->
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
            :formatter="parkFormat2"
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
            :formatter="carTypeFormat2"
            label="车辆类型"
        />

        <el-table-column
            label="状态"
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
            label="操作">
          <template slot-scope="scope">
            <el-button
                type="success"
                @click="showInfo(scope.row)"
                size="mini">详情
            </el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>
    <!--车位租用详细信息对话框-->
    <el-dialog
        title="详情"
        :visible.sync="infoShow"
        width="70%">
      <h4>租用时间</h4>
      <el-descriptions direction="vertical" :column="2" border>
        <el-descriptions-item label="开始时间">{{ curData2.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ curData2.endTime }}</el-descriptions-item>
      </el-descriptions>
      <h4>车位信息</h4>
      <el-descriptions direction="vertical" :column="5" border>
        <el-descriptions-item label="车位编号">{{ curData2.parkBean.number }}</el-descriptions-item>
        <el-descriptions-item label="所属停车场">{{ curData2.parkBean.carportName }}</el-descriptions-item>
        <!--        row.parkBean.type-->
        <el-descriptions-item label="车位类型">{{ parkFormat2(curData2) }}</el-descriptions-item>
        <el-descriptions-item label="面积"> {{ curData2.parkBean.area }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ curData2.parkBean.remark }}</el-descriptions-item>
      </el-descriptions>
      <h4>车辆信息</h4>
      <el-descriptions direction="vertical" :column="4" border>
        <el-descriptions-item label="车牌号">{{ curData2.ownerCarBean.carNumber }}</el-descriptions-item>
        <el-descriptions-item label="品牌">{{ curData2.ownerCarBean.carBrand }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{
            carTypeFormat2(curData2)
          }}
        </el-descriptions-item>
        <el-descriptions-item label="颜色"> {{ curData2.ownerCarBean.carColor }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
    <div class="flex-x-center" style="margin-top: 20px">
      <div v-if="selectRent" style="width: 50%;padding: 20px; box-shadow: 2px 2px 6px #c8c9cc; background: white">
        <div >
          <div class="flex-x-between">
            <div style="font-size: 15px;margin: 20px 0px">已选车位</div>
            <el-button size="mini" type="primary" style="width: 80px;height: 30px;margin-left: 20px" round
                       @click="selectParkDialog = true;active=1">选择
            </el-button>
          </div>
          <el-descriptions direction="vertical" :column="5" border>
            <el-descriptions-item label="车位编号">{{ selectPark.number }}</el-descriptions-item>
            <el-descriptions-item label="所属停车场">{{ selectPark.carportName }}</el-descriptions-item>
            <el-descriptions-item label="车位类型">{{ parkFormat({type: selectPark.type}) }}</el-descriptions-item>
            <el-descriptions-item label="面积"> {{ selectPark.area }}</el-descriptions-item>
            <el-descriptions-item label="备注">{{ selectPark.remark }}</el-descriptions-item>
          </el-descriptions>

          <el-dialog
              title="选择车位"
              :visible.sync="selectParkDialog"
              width="50%">
            <div style="margin-top: 10px">
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
                  <template slot-scope="scope">
                    <el-button
                        type="primary"
                        @click="clickOperation(scope.row,1)"
                        size="mini">选择
                    </el-button>
                  </template>
                </el-table-column>

              </el-table>
            </div>
          </el-dialog>

          <div class="flex-x-between" style="margin-top: 20px">
            <div style="font-size: 15px;margin: 20px 0px">已选车辆</div>
            <el-button size="mini" type="primary" style="width: 80px;height: 30px;margin-left: 20px" round
                       @click="selectCarDialog=true;active=2">选择
            </el-button>
          </div>
          <el-descriptions direction="vertical" :column="4" border>
            <el-descriptions-item label="车牌号">{{ selectCar.carNumber }}</el-descriptions-item>
            <el-descriptions-item label="品牌">{{ selectCar.carBrand }}</el-descriptions-item>
            <el-descriptions-item label="类型">{{ carTypeFormat({carType: selectCar.carType}) }}</el-descriptions-item>
            <el-descriptions-item label="颜色"> {{ selectCar.carColor }}</el-descriptions-item>
          </el-descriptions>

          <el-dialog
              title="选择车辆"
              :visible.sync="selectCarDialog"
              width="50%">
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
                    width="100"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button
                        type="primary"
                        @click="clickOperation(scope.row,2)"
                        size="mini">选择
                    </el-button>
                  </template>
                </el-table-column>

              </el-table>
            </div>
          </el-dialog>

          <div style="font-size: 15px;margin: 40px 0px 20px 0">租用时间</div>
          <div style="margin-top: 10px">
            <el-date-picker
                v-model="rentTime"
                type="daterange"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
            </el-date-picker>
          </div>

          <div class="flex-x-center" style="margin-top: 80px">
            <el-button type="primary" round size="medium" style="width: 200px"
                       @click="addRent">提交
            </el-button>
          </div>

        </div>
      </div>
      <el-dialog
          title="您暂无车辆信息，需添加车辆"
          :close-on-click-modal="false"
          :visible.sync="dialogShow"
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

        <span slot="footer" class="dialog-footer">
        <el-button @click="dialogShow = false">取 消</el-button>
        <el-button type="primary" @click="operationOk">确 定</el-button>
    </span>
      </el-dialog>
    </div>

  </div>
</template>

<script>
import {
  apiAddOwnerCar,
  apiAddRent,
  apiAddRoomUser,
  apiDeleteRoomUserById,
  apiDeleteUserByUid,
  apiGetCost,
  apiGetCostByUid,
  apiGetOwnerCarByOwnerId,
  apiGetPark,
  apiGetParkByNumber,
  apiGetRent,
  apiPay,
  apiQueryRoomUser,
  apiUpdateRent,
  apiUpdateRoomUserById,
  apiUpdateUserById,
  apiUpdateUserByUid,
  getOwnerCarByNumber, getRentByOwnerId
} from "@/utils/request";

export default {
  created() {
    this.ownerId = window.sessionStorage.getItem('userid')
    this.getCar()
    this.getPark()
    this.getRent()
  },
  data() {
    return {
      id: -1,
      ownerId: '',
      selectRent: false,
      infoShow: false,
      selectParkDialog: false,
      selectCarDialog: false,
      rentTime: [],
      dialogShow: false,
      LS: 3,
      RS: 21,
      noCar: false,
      curData: {},
      curData2: {
        parkBean: {},
        roomUserBean: {},
        ownerCarBean: {}
      },
      rentData: [],
      selectPark: {},
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
  methods: {
    getPark() {
      apiGetPark().then(res => {
        this.parkData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    getCar() {
      apiGetOwnerCarByOwnerId({ownerId: this.ownerId}).then(res => {
        if (res != null && res.length > 0) {
          this.carData = res
          this.noCar = false
        } else {
          this.noCar = true
          this.dialogShow = true
          this.$message.warning('您无车辆信息，请先添加');
        }

      }).catch(err => {
        this.$message.error(err);
      })
    },
    getRent() {
      getRentByOwnerId({ownerId: this.ownerId}).then(res => {
        this.rentData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    clickOperation(row, index) {
      if (index === 1) {
        this.selectPark = row
      } else if (index === 2) {
        this.selectCar = row
      }
    },
    parkFormat(row) {
      for (const item of this.typeOptions) {
        if (row.type === item.value) {
          return item.label
        }
      }
    },
    carTypeFormat(row) {
      for (const item of this.carTypeOptions) {
        if (row.carType === item.value) {
          return item.label
        }
      }
    },
    parkFormat2(row) {
      for (const item of this.typeOptions) {
        if (row.parkBean.type === item.value) {
          return item.label
        }
      }
    },
    carTypeFormat2(row) {
      for (const item of this.carTypeOptions) {
        if (row.ownerCarBean.carType === item.value) {
          return item.label
        }
      }
    },
    operationOk() {
      this.curData.ownerId = this.ownerId
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
    addRent() {
      if (!this.selectPark.number) {
        this.$message.warning('请先选择车位');
        return
      }

      if (!this.selectCar.carNumber) {
        this.$message.warning('请先选择车辆');
        return
      }

      if (this.rentTime.length < 1) {
        this.$message.success('请选择租用时间');
        return;
      }
      let data = {
        id: this.id,
        carId: this.selectCar.id,
        ownerId: this.ownerId,
        parkId: this.selectPark.id,
        startTime: this.rentTime[0],
        endTime: this.rentTime[1]
      }
      apiAddRent(data).then(res => {
        this.$message.success('提交成功');
        this.selectRent = false
        this.getRent()
      }).catch(err => {
        this.$message.error(err);
      })
    },
    rentState(row) {
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
    }, showInfo(row) {
      this.curData2 = JSON.parse(JSON.stringify(row))
      this.infoShow = true;
    }
  }
}
</script>

<style scoped>
</style>
