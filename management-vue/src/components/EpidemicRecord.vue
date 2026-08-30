<template>
  <div>
    <div class="flex-row">
      <el-input v-model="searchContent" placeholder="请输入用户姓名或联系方式"></el-input>
      <el-button style="margin-left: 20px" type="primary" @click="search">搜索</el-button>
    </div>
    <div class="flex-x-right" style="margin-top: 15px">
      <el-button style="margin-left: 20px;width: 100px" type="primary" round
                 @click="curData={};isAdd=true;dialogShow=true">添加
      </el-button>
    </div>

    <div style="margin-top: 10px">
      <el-table
          :header-cell-style="{background:'#f5f5f5'}"
          :data="recordData"
          width="100%"
          border>
        <el-table-column
            type="index"
            label="序号"
            width="50px"
        />
        <el-table-column
            prop="name"
            label="姓名"
        />

        <el-table-column
            prop="idCard"
            width="180px"
            label="身份证号"
        />
        <el-table-column
            prop="phone"
            width="120px"
            label="联系方式"
        />
        <el-table-column
            prop="sourceAddress"
            label="来源地"
        />
        <el-table-column
            prop="tripMode"
            :formatter="tripModeFormat"
            width="120px"
            label="出行方式"
        />
        <el-table-column
            prop="trainNumber"
            width="100px"
            label="车次"
        />
        <el-table-column
            prop="returnTime"
            width="110px"
            label="返回时间"
        />
        <el-table-column
            prop="remark"
            label="备注"
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
        :close-on-click-modal="false"
        :title="isAdd?'上报':'修改'"
        :visible.sync="dialogShow"
        width="50%">
      <el-row>
        <el-col :span="LS">姓名：</el-col>
        <el-col :span="RS">
          <el-input v-model="curData.name" maxlength="4" placeholder="请输入姓名"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="LS">身份证号：</el-col>
        <el-col :span="RS">
          <el-input v-model="curData.idCard" maxlength="18" placeholder="请输入身份证号"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="LS">联系方式：</el-col>
        <el-col :span="RS">
          <el-input v-model="curData.phone" maxlength="11" placeholder="请输入联系方式"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="LS">来源地：</el-col>
        <el-col :span="RS">
          <el-input v-model="curData.sourceAddress" maxlength="20" placeholder="请输入来源地"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="3">出行方式：</el-col>
        <el-col :span="9">
          <el-select v-model="curData.tripMode" placeholder="请选择">
            <el-option
                v-for="item in tripModeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :class="[curData.tripMode===2?'show':'hidden']" :span="2">车次：</el-col>
        <el-col :class="[curData.tripMode===2?'show':'hidden']" :span="10">
          <el-input v-model="curData.trainNumber" maxlength="20" placeholder="请输入车次"></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="LS">返回时间：</el-col>
        <el-col :span="RS">
          <el-date-picker
              v-model="curData.returnTime"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期">
          </el-date-picker>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="LS">备注：</el-col>
        <el-col :span="RS">
          <el-input v-model="curData.remark" maxlength="20" placeholder="请输入备注"></el-input>
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
  apiAddRecord,
  apiDeleteRecord, apiGetCarportByCondition,
  apiGetRecord,
  apiUpdateRecord, getOwnerCarByNumber
} from "@/utils/request";

export default {
  created() {
    console.log(1111);
    this.getRecord()
  },
  data() {
    return {
      isAdd: true,
      dialogShow: false,
      LS: 3,
      RS: 21,
      searchContent: '',
      curData: {},
      pickerOptions: {},
      tripModeOptions: [{
        value: 1,
        label: '自驾'
      }, {
        value: 2,
        label: '公共交通工具'
      }],
      recordData: []
    }
  },
  methods: {
    getRecord() {
      apiGetRecord().then(res => {
        this.recordData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    clickOperation(row, isDelete) {
      if (isDelete) {
        apiDeleteRecord({id: row.id}).then(res => {
          this.$message.success('删除成功');
          this.getRecord()
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
      if (this.isAdd) {
        apiAddRecord(this.curData).then(res => {
          this.$message.success('添加成功');
          this.dialogShow = false
          this.getRecord()
        }).catch(err => {
          this.$message.error(err);
        })
      } else {
        apiUpdateRecord(this.curData).then(res => {
          this.$message.success('修改成功');
          this.dialogShow = false
          this.getRecord()
        }).catch(err => {
          this.$message.error(err);
        })
      }
    },
    tripModeFormat(row) {
      for (const item of this.tripModeOptions) {
        if (row.tripMode === item.value) {
          return item.label
        }
      }
    }, search() {
      apiGetCarportByCondition({condition: this.searchContent}).then(res => {
        this.recordData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
  }
}
</script>

<style scoped>

.show {
  visibility: visible
}

.hidden {
  visibility: hidden
}

</style>
