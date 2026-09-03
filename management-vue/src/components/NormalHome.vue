<template>
  <!--主体---->
  <el-container>
    <el-header height="80px">
      <a @click="$router.push('/main')" style="margin-left: 20px;font-size: 24px">
        <span>业主物业操作端</span>
      </a>

      <div>
        <div>
          <a @click="$router.push('/pay_cost')">缴费</a> ｜ <a @click="$router.push('/rent_car')">租车位</a> ｜  <a @click="$router.push('/repairs')">故障保修</a> ｜ <a
            @click="$router.push('/advice')">投诉建议</a>
        </div>

        <el-dropdown style="margin-left: 20px" @command="handleCommand">
          <div class="right-div">
            {{ name }}
          </div>
          <template #dropdown>
            <el-dropdown-item command="logout">退出</el-dropdown-item>
          </template>
        </el-dropdown>
      </div>

    </el-header>

    <el-container>

      <el-main>
        <router-view style="margin-top: 20px; height: 90%"></router-view>
      </el-main>

    </el-container>
  </el-container>

</template>

<script>


export default {
  created() {
    this.name = window.sessionStorage.getItem('name')
    // 只有首次进入 /client(没带具体子路由)才跳主页;
    // 支付返回等场景带子路由(如 /pay_cost),保持目标页不劫持
    if (this.$route.path === '/client') {
      this.$router.push('/main')
    }
  },
  data() {
    return {
      name: ''
    }
  }, methods: {
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

a {
  cursor: pointer;
  font-size: 16px;
}

.el-container {
  height: 100%;
}

.el-header {
  background-color: #91d5ff;
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
