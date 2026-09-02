<template>
  <div class="container flex-x-center">

    <div style="width: 350px;padding:30px;color: white;border: 1px solid white">
      <div style="height: 50px;font-size: 20px;text-align: center">物业管理系统</div>
      <el-row>
        <el-col :span="lSpan">用户名：</el-col>
        <el-col :span="rSpan">
          <el-input v-model="userInfo.phone" maxlength="11" placeholder="请输入用户名"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="lSpan">{{ updatePwd ? '当前密码:' : '密码:' }}</el-col>
        <el-col :span="rSpan">
          <el-input v-model="userInfo.pwd" maxlength="12" show-password type="password" placeholder="请输入密码"></el-input>
        </el-col>
      </el-row>

      <el-row v-show="updatePwd">
        <el-col :span="lSpan">修改密码:</el-col>
        <el-col :span="rSpan">
          <el-input v-model="userInfo.uPwd" maxlength="12" show-password type="password" placeholder="请输入密码"></el-input>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="lSpan">身份：</el-col>
        <el-col :span="rSpan">
          <el-select style="width: 100%" v-model="role" placeholder="请选择是否审核">
            <el-option
                v-for="item in roleOption"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <div v-show="role==1" class="flex-x-right" style="font-size: 10px;margin-top: 10px"><a
          style="cursor: pointer" @click="updatePwd=!updatePwd">{{ updatePwd ? '去登录' : '修改密码' }}</a></div>

      <el-row style="margin-top: 30px">
        <el-col :span="lSpan">
          <el-checkbox style="color: white" v-model="remember">记住</el-checkbox>
        </el-col>
        <el-col :span="rSpan">
          <el-button type="primary" @click="login" style="width:100%;">{{ updatePwd ? '修改密码' : '登录' }}</el-button>
        </el-col>
      </el-row>

    </div>
  </div>
</template>

<script>
import {apiLogin, apiNormalLogin, apiUpdatePwd} from "@/utils/request";

export default {
  mounted() {
    this.userInfo.phone = window.sessionStorage.getItem('phone')
    this.userInfo.pwd = window.sessionStorage.getItem('pwd')
  },
  data() {
    return {
      lSpan: 5,
      rSpan: 19,
      updatePwd: false,
      role: 1,
      remember: true,
      roleOption: [{
        value: 1,
        label: '业主'
      }, {
        value: 2,
        label: '管理员'
      }],
      userInfo: {
        phone: '',
        pwd: '',
        uPwd: ''
      }
    }
  }, methods: {
    login() {
      if (this.updatePwd) {
        if (!this.userInfo.phone || !this.userInfo.pwd || !this.userInfo.uPwd) {
          this.$message.warning('请输入用户名、原密码、修改密码')
          return
        }
        apiUpdatePwd({
          phone: this.userInfo.phone,
          pwd: this.userInfo.pwd,
          uPwd: this.userInfo.uPwd
        }).then(res => {
          this.userInfo.pwd =this.userInfo.uPwd
          this.$message.success('修改成功，去登录')
          this.updatePwd = false
        }).catch(err => {
          this.$message.error(err)
        })
      } else {

        if (!this.userInfo.phone || !this.userInfo.pwd) {
          this.$message.warning('请输入用户名、密码')
          return
        }
        if (this.role == 1) {
          apiNormalLogin({phone: this.userInfo.phone, pwd: this.userInfo.pwd}).then(res => {
            if (this.remember) {
              window.sessionStorage.setItem('phone', res.user.phone)
              window.sessionStorage.setItem('pwd', this.userInfo.pwd)
            }
            window.sessionStorage.setItem('token', res.token)
            window.sessionStorage.setItem('userid', res.user.id)
            window.sessionStorage.setItem('name', res.user.name)
            window.sessionStorage.setItem('activePath', '')
            this.$router.push('/client')
          }).catch(err => {
            this.$message.error(err)
          })
        } else {
          apiLogin({phone: this.userInfo.phone, pwd: this.userInfo.pwd}).then(res => {
            if (this.remember) {
              window.sessionStorage.setItem('phone', res.user.phone)
              window.sessionStorage.setItem('pwd', this.userInfo.pwd)
            }
            window.sessionStorage.setItem('token', res.token)
            window.sessionStorage.setItem('userid', res.user.uid)
            window.sessionStorage.setItem('name', res.user.name)
            window.sessionStorage.setItem('activePath', '')
            this.$router.push('/home')
          }).catch(err => {
            this.$message.error(err)
          })
        }

      }
    }
  }
}
</script>

<style scoped>
.container {
  background: url("../assets/img/bg.png");
  width: 100%;
  height: 100%;
  background-size: 100% 100%;
}

/*.el-input {*/
/*  width: auto;*/
/*}*/
</style>
