<template>
  <div>

    <div class="flex-x-right" style="margin-top: 15px">
      <el-button style="margin-left: 20px;width: 100px" type="primary" round
                 @click="currentNews={};isAdd =true;showNewsDialog=true">添加
      </el-button>
    </div>
    <el-table
        :header-cell-style="{background:'#f5f5f5'}"
        :data="newsData"
        style="margin-top: 20px"
        border>
      <el-table-column
          type="index"
          label="序号"
          width="60"
      />
      <el-table-column
          prop="title"
          label="标题"
      />
      <el-table-column
          prop="time"
          width="110"
          :formatter="timeFormat"
          label="操作时间"
      />
      <el-table-column
          width="180"
          label="操作">
        <template slot-scope="scope">
          <el-button
              type="primary"
              @click="clickUpdateNews(scope.row)"
              size="mini">编辑
          </el-button>
          <el-button
              type="danger"
              @click="clickDeleteNews(scope.row)"
              size="mini">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
        :close-on-click-modal="false"
        :title="isAdd?'添加':'修改'"
        :visible.sync="showNewsDialog"
        style="height: 80%"
        @close="currentNews ={}"
        width="60%">
      <el-row>
        <el-col :span="leftSpan">标题：</el-col>
        <el-col :span="rightSpan">
          <el-input v-model="currentNews.title" placeholder="请输入标题"></el-input>
        </el-col>
      </el-row>

      <quill-editor
          style="margin-top: 10px"
          ref="myQuillEditor"
          v-model="currentNews.content"
          :options="editorOption"
          @blur="onEditorBlur"
      />
      <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogOk">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import {quillEditor, hljs} from 'vue-quill-editor'
import {apiAddNews, apiDeleteNews, apiGetNewsById, apiGetNews, apiUpdateNews} from "@/utils/request";

export default {
  components: {
    quillEditor
  },
  created() {
    this.getNews()
  },
  data() {
    return {
      //富文本编辑器配置
      editorOption: {
        modules: {
          syntax: {
            highlight: text => hljs.highlightAuto(text).value
          },
          toolbar: [
            ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
            ['blockquote', 'code-block'],
            [{'header': 1}, {'header': 2}],               // custom button values
            [{'list': 'ordered'}, {'list': 'bullet'}],
            [{'script': 'sub'}, {'script': 'super'}],      // superscript/subscript
            [{'indent': '-1'}, {'indent': '+1'}],          // outdent/indent
            [{'direction': 'rtl'}],                         // text direction
            [{'size': ['small', false, 'large', 'huge']}],  // custom dropdown
            [{'header': [1, 2, 3, 4, 5, 6, false]}],
            [{'color': []}, {'background': []}],          // dropdown with defaults from theme
            [{'font': []}],
            [{'align': []}],
            ['link', 'image']//去除video即可
          ]
        },
        placeholder: '编辑内容...'
      },
      content: '',
      isAdd: false,
      showNewsDialog: false,
      leftSpan: 2,
      rightSpan: 22,
      newsData: [],
      currentNews: {
        id: -1,
        title: '',
        source: '',
        content: ''
      }
    }
  },
  methods: {
    btnClick() {
      this.showNewsDialog = true;
      this.isAdd = true
    },
    onEditorBlur() {
      let e = document.querySelector('.ql-tooltip,.ql-editing');
      if (e) {
        let left = e.style.left
        if (left.indexOf('-') === 0) {
          e.style.left = '5px'
        }
      }
    },
    getNews() {
      apiGetNews().then(res => {
        this.newsData = res
      }).catch(err => {
        this.$message.error(err);
      })
    },
    timeFormat(row) {
      let date = new Date(row.time);
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();
      month = month < 10 ? "0" + month : month;
      day = day < 10 ? "0" + day : day;
      return year + '/' + month + '/' + day;
    },
    dialogOk() {
      if (this.isAdd) {
        apiAddNews(this.currentNews).then(res => {
          this.$message.success('添加成功');
          this.showNewsDialog = false
          this.getNews()
        }).catch(err => {
          this.$message.error(err);
        })
      } else {
        apiUpdateNews(this.currentNews).then(res => {
          this.$message.success('修改成功');
          this.showNewsDialog = false
          this.getNews()
        }).catch(err => {
          this.$message.error(err);
        })
      }

    },
    clickUpdateNews(row) {
      this.isAdd = false
      apiGetNewsById({'id': row.id}).then(res => {
        this.currentNews = res
        this.showNewsDialog = true
      }).catch(err => {
        this.$message.error(err);
      })
    },
    clickDeleteNews(row) {
      apiDeleteNews({'id': row.id}).then(res => {
        this.$message.success('删除成功');
        this.getNews()
      }).catch(err => {
        this.$message.error(err);
      })
    }
  }
}
</script>

<style scoped>
.el-row {
  display: flex;
  margin-top: 10px;
  justify-content: center;
  align-items: center;
}
</style>
