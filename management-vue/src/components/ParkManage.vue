<template>
  <div>
    <div class="flex-row">
      <el-input v-model="searchContent" placeholder="请输入完整车位编号"></el-input>
      <el-button style="margin-left: 20px" type="primary" @click="search">搜索</el-button>
    </div>
    <div class="flex-x-right" style="margin-top: 15px">
      <el-button style="margin-left: 20px;width: 100px" type="primary" round
                 @click="curData={};isAdd =true;dialogShow=true">添加
      </el-button>
    </div>

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
        :visible.sync="dialogShow"
        :close-on-click-modal="false"
        width="50%">
      <el-row>
        <el-col :span="LS">车位编号：</el-col>
        <el-col :span="RS">
          <el-input v-model="curData.number" maxlength="10" placeholder="请输入车位编号"></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="LS">所属停车场：</el-col>
        <el-col :span="RS">
          <el-select v-model="curData.carportId" placeholder="请选择所属停车场">
            <el-option
                v-for="item in carportData"
                :key="item.id"
                :label="item.number"
                :value="item.id">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="LS">车位类型：</el-col>
        <el-col :span="RS">

          <el-select v-model="curData.type" placeholder="请选择停车场类型">
            <el-option
                v-for="item in typeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="LS">面积：</el-col>
        <el-col :span="RS">
          <el-input v-model="curData.area" maxlength="3" placeholder="请输入面积"></el-input>
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
  apiAddPark,
  apiDeletePark, apiGetCarport,
  apiGetPark, apiGetParkByNumber,
  apiUpdatePark
} from "@/utils/request";

export default {
  created() {
    this.getCarport()
    this.getPark()
  },
  data() {
    return {
      isAdd: true,
      searchContent: '',
      dialogShow: false,
      LS: 4,
      RS: 20,
      curData: {},
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
      carportData: [],
      parkData: []
    }
  },
  methods: {
    getCarport() {
      apiGetCarport().then(res => {
        this.carportData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    getPark() {
      apiGetPark().then(res => {
        this.parkData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    clickOperation(row, isDelete) {
      if (isDelete) {
        apiDeletePark({id: row.id}).then(res => {
          this.$message.success('删除成功');
          this.getPark()
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
      if (!this.curData.number || !this.curData.carportId
          || !this.curData.type|| !this.curData.area) {
        this.$message.warning('请输入相关信息');
        return
      }
      if (this.isAdd) {
        apiAddPark(this.curData).then(res => {
          this.$message.success('添加成功');
          this.dialogShow = false
          this.getPark()
        }).catch(err => {
          this.$message.error(err);
        })
      } else {
        apiUpdatePark(this.curData).then(res => {
          this.$message.success('修改成功');
          this.dialogShow = false
          this.getPark()
        }).catch(err => {
          this.$message.error(err);
        })
      }
    },
    search() {
      apiGetParkByNumber({number: this.searchContent}).then(res => {
        this.parkData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    parkFormat(row) {
      for (const item of this.typeOptions) {
        if (row.type === item.value) {
          return item.label
        }
      }
    }
  }
}
</script>

<style scoped>
</style>
