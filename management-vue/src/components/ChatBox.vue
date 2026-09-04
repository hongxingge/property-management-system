<template>
  <div class="chat-container">
    <div class="chat-header">物业智能客服</div>
    <div class="chat-body" ref="chatBody">
      <div v-for="(msg, i) in messages" :key="i" class="chat-msg" :class="msg.role">
        <div class="bubble">{{ msg.content }}</div>
      </div>
      <div v-if="loading" class="chat-msg ai">
        <div class="bubble">正在思考...</div>
      </div>
    </div>
    <div class="chat-footer">
      <el-input v-model="input" placeholder="请输入问题，如：停车费怎么算"
                @keyup.enter="send"></el-input>
      <el-button type="primary" @click="send">发送</el-button>
    </div>
  </div>
</template>

<script>
import { apiChatAsk } from "@/utils/request";

export default {
  data() {
    return {
      input: '',
      loading: false,
      messages: []
    }
  },
  methods: {
    send() {
      const q = this.input.trim()
      if (!q || this.loading) return
      this.messages.push({ role: 'user', content: q })
      this.input = ''
      this.loading = true
      this.scrollBottom()
      apiChatAsk({ question: q }).then(answer => {
        this.messages.push({ role: 'ai', content: answer })
      }).catch(err => {
        this.messages.push({ role: 'ai', content: '抱歉，' + err })
      }).finally(() => {
        this.loading = false
        this.scrollBottom()
      })
    },
    scrollBottom() {
      this.$nextTick(() => {
        const el = this.$refs.chatBody
        if (el) el.scrollTop = el.scrollHeight
      })
    }
  }
}
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #fff;
  border: 1px solid #eee;
  border-radius: 6px;
}
.chat-header {
  padding: 12px 16px;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid #eee;
  background: #f7f8fa;
}
.chat-body {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}
.chat-msg {
  display: flex;
  margin-bottom: 12px;
}
.chat-msg.user { justify-content: flex-end; }
.chat-msg.ai { justify-content: flex-start; }
.bubble {
  max-width: 70%;
  padding: 10px 14px;
  border-radius: 8px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-all;
}
.chat-msg.user .bubble { background: #409eff; color: #fff; }
.chat-msg.ai .bubble { background: #f0f2f5; color: #333; }
.chat-footer {
  display: flex;
  gap: 10px;
  padding: 12px 16px;
  border-top: 1px solid #eee;
}
</style>