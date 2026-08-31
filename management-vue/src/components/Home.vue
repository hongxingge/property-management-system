<template>
  <el-container>
    <!--头部-->
    <el-header height="10%">
      <div>
        <!--        <img style="margin-left: 20px" width="50px" height="50px" src="../../assets/logo.png" alt/>-->
        <span>物业管理系统</span>
      </div>

      <el-dropdown @command="handleCommand">
        <div class="right-div">
          {{ name }}
        </div>
        <template #dropdown>
          <el-dropdown-item command="logout">退出</el-dropdown-item>
        </template>
      </el-dropdown>

      <!--      <el-button @click="logout" type="info">退出</el-button>-->
    </el-header>

    <!--主体-->
    <el-container>
      <!-- 侧边栏-->
      <el-aside width="200px">
        <el-menu unique-opened background-color="#333744" :collapse-transition="false" router text-color="#fff"
                 active-text-color="#409FFF" :default-active="activePath">

          <el-submenu :index="index+''" v-for="(item ,index) in sideData" :key="index+''">
            <template #title>{{ item.name }}</template>

            <el-menu-item :index="subItem.path" v-for="(subItem,subIndex) in item.childData" :key="index+'-'+subIndex"
                          @click="setNavPath(subItem.path);">
              <template #title>{{ subItem.name }}</template>
            </el-menu-item>
          </el-submenu>

        </el-menu>
      </el-aside>

      <el-main>
        <router-view style="margin-top: 20px; height: 90%"></router-view>
      </el-main>

    </el-container>
  </el-container>
</template>

<script>
import CarportManage from "@/components/CarportManage";
import ParkManage from "@/components/ParkManage";
import OwnerCarManage from "@/components/OwnerCarManage";

export default {

  mounted() {
    this.name = window.sessionStorage.getItem('name')
    this.activePath = window.sessionStorage.getItem('activePath')
    console.log(this.name)
  },
  data() {
    return {
      name: '',
      activePath: '',
      sideData: [
        {
          name: '管理员',
          childData: [
            {
              name: '人员管理',
              path: '/property_manage'
            }
          ]
        }, {
          name: '物业管理',
          childData: [
            {
              name: '业主管理',
              path: '/room_user_manage'
            }
          ]
        }, {
          name: '车位管理',
          childData: [
            {
              name: '停车场管理',
              path: '/carport_manage'
            }, {
              name: '车位管理',
              path: '/park_manage'
            },
            // {
            //   name: '车位出租',
            //   path: '/park_rent_manage'
            // },
            {
              name: '租售管理',
              path: '/rent_manage'
            }, {
              name: '业主车辆',
              path: '/owner_manage'
            }
          ]
        }, {
          name: '疫情防控',
          childData: [
            {
              name: '返省登记',
              path: '/epidemic_record'
            }
          ]
        }, {
          name: '费用管理',
          childData: [
            {
              name: '费用管理',
              path: '/pay_cost_manage'
            }, {
              name: '停车费设置',
              path: '/car_cost'
            }
          ]
        }, {
          name: '上报',
          childData: [
            {
              name: '故障上报',
              path: '/repairs_manage'
            },
            {
              name: '投诉与建议',
              path: '/advice_manage'
            }
          ]
        }, {
          name: '客户端管理',
          childData: [
            {
              name: '轮播图',
              path: '/banner_manage'
            },
            {
              name: '公告',
              path: '/news_manage'
            }
          ]
        }
      ]
    }
  },
  watch: {
    $route(to, from) {
      for (let i = 0; i < this.sideData.length; i++) {
        let currSideData = this.sideData[i]
        for (let j = 0; j < currSideData.childData.length; j++) {
          let childData = currSideData.childData[j]
          if (childData.path === to.path) {
            window.sessionStorage.setItem('activePath', childData.path)
            this.activePath = childData.path
            return
          }
        }
      }
    }
  },
  methods: {
    setNavPath(path) {
      if (path) {
        this.$router.push(path)
      }
    },
    handleCommand(command) {
      if (command === 'logout') {
        window.sessionStorage.removeItem('userid')
        window.sessionStorage.removeItem('name')
        this.$router.push('/login')
      }
    }
  }
}
</script>


<style lang="less" scoped>
.el-container {
  height: 100%;
}

.el-header {
  background-color: #373f41;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  align-items: center;
  color: #fff;
  font-size: 20px;

  > div {
    display: flex;
    align-items: center;

    img {
      height: 40px;
    }

    span {
      margin-left: 15px;
    }

  }
}

.el-aside {
  background-color: #333744;

  .el-menu {
    border: none;
  }
}

.right-div {
  background: #42b983;
  width: 50px;
  height: 50px;
  border-radius: 25px;
  font-size: 16px;
  text-align: center;
  color: white;
  line-height: 50px;
}

.el-main {
  background-color: #eaedf1;
}
</style>
