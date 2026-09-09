<template>
  <el-container>
    <el-header height="10%">
      <div>
        <span>物业管理系统 · 员工端</span>
      </div>
      <el-dropdown @command="handleCommand">
        <div class="right-div">{{ name }}</div>
        <template #dropdown>
          <el-dropdown-item command="logout">退出</el-dropdown-item>
        </template>
      </el-dropdown>
    </el-header>

    <el-container>
      <el-aside width="200px">
        <el-menu unique-opened background-color="#333744" :collapse-transition="false" router
                 text-color="#fff" active-text-color="#409FFF" :default-active="activePath">
          <el-submenu :index="index+''" v-for="(item, index) in sideData" :key="index+''">
            <template #title>{{ item.name }}</template>
            <el-menu-item :index="subItem.path" v-for="(subItem, subIndex) in item.childData"
                          :key="index+'-'+subIndex" @click="setNavPath(subItem.path);">
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
export default {
  mounted() {
    this.name = window.sessionStorage.getItem('name')
    this.activePath = window.sessionStorage.getItem('activePath')
  },
  data() {
    return {
      name: '',
      activePath: '',
      // 物业员工只有三个功能:报修、投诉、公告(复用管理员端的组件)
      sideData: [
        {
          name: '工作台',
          childData: [
            { name: '报修处理', path: '/staff/repairs_manage' },
            { name: '投诉处理', path: '/staff/advice_manage' },
            { name: '公告管理', path: '/staff/news_manage' }
          ]
        }
      ]
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
.el-container { height: 100%; }
.el-header {
  background-color: #373f41;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  align-items: center;
  color: #fff;
  font-size: 20px;
  > div { display: flex; align-items: center; span { margin-left: 15px; } }
}
.el-aside { background-color: #333744; .el-menu { border: none; } }
.right-div {
  background: #42b983;
  width: 50px; height: 50px; border-radius: 25px;
  font-size: 16px; text-align: center; color: white; line-height: 50px;
}
.el-main { background-color: #eaedf1; }
</style>