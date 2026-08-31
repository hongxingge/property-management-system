<template>
  <div>

    <el-table
        :header-cell-style="{background:'#f5f5f5'}"
        :data="repairs"
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
          prop="phone"
          label="联系方式"
      />
      <el-table-column
          prop="faultType"
          :formatter="faultTypeFormat"
          label="故障类型"
      />
      <el-table-column
          prop="files"
          label="图片"
      >
        <template #default="scope">
          <template v-for="(item,index) in getFileList(scope.row.files)">
            <img  @click="imgClick(baseUrl+'/user_images?fileName='+item)" style="width: 60px;height: 60px;margin-left: 10px"
                 :src="baseUrl+'/user_images?fileName='+item">
          </template>
        </template>
      </el-table-column>
      <el-table-column
          prop="faultDesc"
          label="故障描述"
      />
      <el-table-column
          prop="state"
          label="处理状态"
      >
        <template #default="scope">
          <el-tag type="success" v-show="scope.row.state===3">已处理</el-tag>
          <el-tag type="danger" v-show="scope.row.state===1">未处理</el-tag>
          <el-tag v-show="scope.row.state===2">处理中</el-tag>
        </template>

      </el-table-column>
      <el-table-column
          prop="repairsTime"
          :formatter="formatTime"
          label="上报时间"
      />
      <el-table-column
          label="操作">
        <template #default="scope">
          <el-button
              type="primary"
              @click="clickOperation(scope.row)"
              size="small">修改进度
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
        title="修改进度"
        v-model="dialogShow"
        width="40%">
      <el-row>
        <el-col :span="LS">进度：</el-col>
        <el-col :span="RS">
          <el-select style="width: 100%" v-model="curData.state" placeholder="请选择当前进度">
            <el-option
                v-for="item in stateOption"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <template #footer>
        <el-button @click="dialogShow = false">取 消</el-button>
        <el-button type="primary" @click="operationOk">确 定</el-button>
    </template>
    </el-dialog>

    <el-dialog
        title="图片"
        v-model="imgShow"
        width="50%"
        :close-on-click-modal="false"
        >
      <div class="flex-x-center">
        <img  width="80%" :src="curImg">
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {apiGetFault, apiUpdateFaultForAdmin, apiUpdateFaultForClient, getBaseUrl} from "@/utils/request";

export default {
  created() {
    this.getFaults()
    this.baseUrl = getBaseUrl()
  },
  data() {
    return {
      dialogShow: false,
      curImg: '',
      imgShow: false,
      baseUrl: '',
      LS: 4,
      RS: 20,
      curData: {},
      stateOption: [{
        value: 1,
        label: '未处理'
      }, {
        value: 2,
        label: '处理中'
      }, {
        value: 3,
        label: '已处理'
      }],
      options: [
        {
          value: '1',
          label: '房屋维修',
          children: [
            {
              value: '1-1',
              label: '水路卫浴',
              children: [
                {
                  value: '1-1-1',
                  label: '马桶'
                }, {
                  value: '1-1-2',
                  label: '地漏'
                }, {
                  value: '1-1-3',
                  label: '淋浴器'
                }, {
                  value: '1-1-4',
                  label: '水管'
                }, {
                  value: '1-1-5',
                  label: '洗菜池'
                }, {
                  value: '1-1-6',
                  label: '水龙头'
                }, {
                  value: '1-1-7',
                  label: '暖气'
                }, {
                  value: '1-1-8',
                  label: '洗手池'
                }, {
                  value: '1-1-9',
                  label: '浴缸'
                }, {
                  value: '1-1-10',
                  label: '增压泵'
                }, {
                  value: '1-1-11',
                  label: '阀门'
                }]
            },
            {
              value: '1-2',
              label: '家具家居',
              children: [{
                value: '1-2-1',
                label: '床'
              }, {
                value: '1-2-2',
                label: '衣柜'
              }, {
                value: '1-2-3',
                label: '椅子'
              }, {
                value: '1-2-4',
                label: '橱柜'
              }, {
                value: '1-2-5',
                label: '晒衣杆'
              }, {
                value: '1-2-6',
                label: '书桌'
              }, {
                value: '1-2-7',
                label: '窗帘杆'
              }, {
                value: '1-2-8',
                label: '置物架'
              }, {
                value: '1-2-9',
                label: '沙发'
              }, {
                value: '1-2-10',
                label: '茶几'
              }, {
                value: '1-2-11',
                label: '电视柜'
              }, {
                value: '1-2-12',
                label: '卫浴镜'
              }, {
                value: '1-2-13',
                label: '餐桌'
              }, {
                value: '1-2-14',
                label: '书柜'
              }]
            },
            {
              value: '1-3',
              label: '电路灯具',
              children: [{
                value: '1-3-1',
                label: '灯具'
              }, {
                value: '1-3-2',
                label: '浴霸'
              }, {
                value: '1-3-3',
                label: '电闸'
              }, {
                value: '1-3-4',
                label: '插座'
              }, {
                value: '1-3-5',
                label: '开关'
              }, {
                value: '1-3-6',
                label: '电线'
              }, {
                value: '1-3-7',
                label: '排风扇'
              }]
            },
            {
              value: '1-4',
              label: '房屋主体',
              children: [{
                value: '1-4-1',
                label: '门'
              }, {
                value: '1-4-2',
                label: '窗'
              }, {
                value: '1-4-3',
                label: '纱窗'
              }, {
                value: '1-4-4',
                label: '地面'
              }, {
                value: '1-4-5',
                label: '墙角'
              }, {
                value: '1-4-6',
                label: '天花板'
              }]
            }
          ]
        },
        {
          value: '2',
          label: '电器维修',
          children: [
            {
              value: '2-1',
              label: '天花板'
            }, {
              value: '2-2',
              label: '燃气热水器'
            }, {
              value: '2-3',
              label: '壁挂炉'
            }, {
              value: '2-4',
              label: '灶具'
            }, {
              value: '2-5',
              label: '洗衣机'
            }, {
              value: '2-6',
              label: '电热水器'
            }, {
              value: '2-7',
              label: '油烟机'
            }, {
              value: '2-8',
              label: '冰箱'
            }, {
              value: '2-9',
              label: '电视'
            }, {
              value: '2-10',
              label: '微波炉'
            }, {
              value: '2-11',
              label: '中央空调'
            }
          ]
        },
        {
          value: '3',
          label: '开锁换锁',
          children: [
            {
              value: '3-1',
              label: '电子锁'
            }, {
              value: '3-2',
              label: '机械锁'
            }]
        }
      ],
      repairs: []

    }
  },
  methods: {
    getFaults() {
      apiGetFault().then(res => {
        this.repairs = res
      }).catch(err => {
        this.$message.error(err)
      })
    },
    formatTime(row) {
      let date = new Date(row.time);
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();
      month = month < 10 ? "0" + month : month;
      day = day < 10 ? "0" + day : day;
      return year + '-' + month + '-' + day;
    },
    clickOperation(row) {
      this.curData = JSON.parse(JSON.stringify(row))
      this.dialogShow = true;
    },
    operationOk() {
      apiUpdateFaultForAdmin({id: this.curData.id, state: this.curData.state}).then(res => {
        this.$message.success('修改成功');
        this.dialogShow = false
        this.getFaults()
      }).catch(err => {
        this.$message.error(err);
      })
    },
    faultTypeFormat(row) {
      let level = 0
      let str = row.faultType.split(';')// 3; 3-2
      let maxLevel = str.length
      for (const item of this.options) {
        if (str[0] === item.value) {
          level++
          for (const item2 of item.children) {
            if (str[1] === item2.value) {
              level++
              if (level === maxLevel) {
                return item2.label
              } else {
                for (const item3 of item2.children) {
                  if (str[2] === item3.value) {
                    return item3.label
                  }
                }
              }
            }
          }
        }


      }
    },
    sateFormat(row) {
      switch (row.state) {
        case 1:
          return '未处理'
        case 2:
          return '处理中'
        case 3:
          return '已处理'
      }
    },
    getFileList(files) {
      console.log(files)
      if (files) {
        let fileList = files.substring(0, files.lastIndexOf(',')).split(',')
        return fileList
      } else {
        return []
      }
    },
    imgClick(img) {
      this.imgShow = true
      this.curImg = img
    }
  }
}
</script>

<style scoped>

</style>
