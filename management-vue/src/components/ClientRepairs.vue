<template>
  <div>

    <div class="flex-x-center" style="font-size: 18px;margin-bottom: 20px">
      故障报修
    </div>
    <div class="flex-x-right" style="margin-top: 15px">
      <el-button style="margin-left: 20px;width: 100px;margin-bottom: 20px" type="primary" round
                 @click="dialogShow = true;  isAdd = true;  curData={};value=[];fileList=[];uploadLists=[]">添加
      </el-button>
    </div>
    <!--报修表格-->
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
          prop="faultDesc"
          label="故障描述"
      />
      <el-table-column
          prop="curState"
          label="处理状态">
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
              @click="clickOperation(scope.row,false)"
              size="small">修改
          </el-button>
          <el-button
              type="danger"
              @click="clickOperation(scope.row,true)"
              size="small">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--操作对话框-->
    <el-dialog
        :title="isAdd?'添加':'修改'"
        v-model="dialogShow"
        @closed="dialogClose"
        :close-on-click-modal="false"
        width="40%">
      <el-row>
        <el-col :span="LS">姓名：</el-col>
        <el-col :span="RS">
          <el-input style="width: 100%" v-model="curData.name" maxlength="4" placeholder="请输入姓名"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="LS">联系方式：</el-col>
        <el-col :span="RS">
          <el-input style="width: 100%" v-model="curData.phone" maxlength="11" placeholder="请输入联系方式"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="LS">故障类型：</el-col>
        <el-col :span="RS">
          <el-cascader
              style="width: 100%"
              v-model="value"
              placeholder="请选择故障类型"
              :options="options"
          ></el-cascader>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="LS">故障描述：</el-col>
        <el-col :span="RS">
          <el-input
              style="width: 100%"
              type="textarea"
              :rows="5"
              placeholder="请输入内容描述"
              v-model="curData.faultDesc">
          </el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="LS">故障图片：</el-col>
        <el-col :span="RS">
          <el-upload
              :action="baseUrl+'/fileUpload'"
              list-type="picture-card"
              name="file"
              :on-success="uploadFileSuccess"
              :on-exceed="exceedImg"
              :limit="2"
              :file-list="fileList"
              :on-remove="handleRemove">
            <i class="el-icon-plus"></i>
          </el-upload>
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

import {apiAddFault, apiDeleteFault, apiGetFaultByUid, apiUpdateFaultForClient, getBaseUrl} from "@/utils/request";

export default {
  created() {
    this.getFaults()
    this.baseUrl = getBaseUrl()
  },
  data() {
    return {
      isAdd: true,
      dialogShow: false,
      baseUrl: '',
      uploadLists: [],
      fileList: [],
      LS: 4,
      RS: 20,
      curData: {},
      value: [],
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
      repairs: [
        {
          id: 1,
          name: 'zhangsan',
          phone: '12313121',
          faultType: '房屋损坏',
          faultDesc: '等你dosed极大',
          curState: '维修中',
          repairsTime: '2012-21-2',
        }

      ]

    }
  },
  methods: {
    getFaults() {//通过业主id获取报修信息
      apiGetFaultByUid({uid: window.sessionStorage.getItem('userid')}).then(res => {
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
    clickOperation(row, isDelete) {
      if (isDelete) {
        apiDeleteFault({id: row.id}).then(res => {
          this.$message.success('删除成功');
          this.getFaults()
        }).catch(err => {
          this.$message.error(err);
        })
      } else {
        this.isAdd = false
        this.curData = JSON.parse(JSON.stringify(row))
        this.getFileList(this.curData.files)
        this.value = this.curData.faultType.split(';')
        this.dialogShow = true;
      }
    }, operationOk() {
      if (!this.curData.name || !this.curData.phone ||
          !this.value || !this.curData.faultDesc) {
        this.$message.warning('请输入相关信息');
        return
      }
      let type = ''
      for (const v of this.value) {
        type += (v + ';')
      }
      this.curData.faultType = type.slice(0, type.length - 1)
      let strs = ''
      for (const item of this.uploadLists) {
        strs += item.newFileName + ','
      }
      this.curData.files = strs
      if (this.isAdd) {
        this.curData.uid = window.sessionStorage.getItem('userid')

        apiAddFault(this.curData).then(res => {
          this.$message.success('添加成功');
          this.dialogShow = false
          this.getFaults()
        }).catch(err => {
          this.$message.error(err);
        })
      } else {
        apiUpdateFaultForClient(this.curData).then(res => {
          this.$message.success('修改成功');
          this.dialogShow = false
          this.getFaults()
        }).catch(err => {
          this.$message.error(err);
        })
      }
    }, faultTypeFormat(row) {
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
    handleRemove(file, fileList) {//删除文件
      let index = 0
      for (let i = 0; i < this.uploadLists.length; i++) {
        if (this.uploadLists[i].fileName === file.name) {
          index = i
        }
      }
      this.uploadLists.splice(index, 1)
    }, uploadFileSuccess(response, file, fileList) {//上传文件
      console.log(file.name)
      this.uploadLists.push({
        fileName: file.name,
        newFileName: response.data
      })
    }
    ,
    exceedImg() {
      this.$message.info('最多上传2张图片');
    }, getFileList(files) {
      console.log(files)
      if (files) {
        let fileList = files.substring(0, files.lastIndexOf(',')).split(',')
        console.log(fileList)
        this.fileList = []
        this.uploadLists = []
        for (const item of fileList) {
          console.log(item)
          this.uploadLists.push({
            fileName: item,
            newFileName: item
          })
          this.fileList.push({
            name: item,
            url: this.baseUrl + '/user_images?fileName=' + item
          })
        }
      }
    }, dialogClose() {
      this.fileList=[]
      this.uploadLists=[]
    }
  }
}
</script>

<style scoped>

</style>
