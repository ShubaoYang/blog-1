<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <!-- 文章标题 -->
    <div class="article-title-container">
      <el-input v-model="question.question" size="medium" placeholder="题目" />
      <el-button type="danger" size="medium" class="save-btn" @click="saveQuestionDraft" v-if="question.id == null || question.status == 3">
        保存草稿
      </el-button>
      <el-button type="danger" size="medium" @click="openModel" style="margin-left:10px">
        发布问答
      </el-button>
    </div>
    <!-- 文章内容 -->
    <mavon-editor ref="md" v-model="question.answer" @imgAdd="uploadImg" style="height:calc(100vh - 260px)" />
    <!-- 添加文章对话框 -->
    <el-dialog :visible.sync="addOrEdit" width="40%" top="3vh">
      <div class="dialog-title-container" slot="title">
        发布问答
      </div>
      <!-- 文章数据 -->
      <el-form label-width="80px" size="medium" :model="question">
        <!-- 文章分类 -->
        <el-form-item label="文章分类">
          <el-tag type="success" v-show="question.categoryName" style="margin:0 1rem 0 0" :closable="true" @close="removeCategory">
            {{ question.categoryName }}
          </el-tag>
          <!-- 分类选项 -->
          <el-popover placement="bottom-start" width="460" trigger="click" v-if="!question.categoryName">
            <div class="popover-title">分类</div>
            <!-- 搜索框 -->
            <el-autocomplete style="width:100%" v-model="categoryName" :fetch-suggestions="searchCategories" placeholder="请输入分类名搜索，enter可添加自定义分类" :trigger-on-focus="false" @keyup.enter.native="saveCategory" @select="handleSelectCategories">
              <template slot-scope="{ item }">
                <div>{{ item.categoryName }}</div>
              </template>
            </el-autocomplete>
            <!-- 分类 -->
            <div class="popover-container">
              <div v-for="item of categoryList" :key="item.id" class="category-item" @click="addCategory(item)">
                {{ item.categoryName }}
              </div>
            </div>
            <el-button type="success" plain slot="reference" size="small">
              添加分类
            </el-button>
          </el-popover>
        </el-form-item>
        <!-- 文章标签 -->
        <el-form-item label="文章标签">
          <el-tag v-for="(item, index) of question.tagNameList" :key="index" style="margin:0 1rem 0 0" :closable="true" @close="removeTag(item)">
            {{ item }}
          </el-tag>
          <!-- 标签选项 -->
          <el-popover placement="bottom-start" width="460" trigger="click" v-if="question.tagNameList.length < 3">
            <div class="popover-title">标签</div>
            <!-- 搜索框 -->
            <el-autocomplete style="width:100%" v-model="tagName" :fetch-suggestions="searchTags" placeholder="请输入标签名搜索，enter可添加自定义标签" :trigger-on-focus="false" @keyup.enter.native="saveTag" @select="handleSelectTag">
              <template slot-scope="{ item }">
                <div>{{ item.tagName }}</div>
              </template>
            </el-autocomplete>
            <!-- 标签 -->
            <div class="popover-container">
              <div style="margin-bottom:1rem">添加标签</div>
              <el-tag v-for="(item, index) of tagList" :key="index" :class="tagClass(item)" @click="addTag(item)">
                {{ item.tagName }}
              </el-tag>
            </div>
            <el-button type="primary" plain slot="reference" size="small">
              添加标签
            </el-button>
          </el-popover>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="danger" @click="saveOrUpdateQuestion">
          发 表
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import * as imageConversion from "image-conversion";
export default {
  created() {
    const path = this.$route.path;
    const arr = path.split("/");
    const questionId = arr[2];
    if (questionId) {
      this.axios.get("/api/admin/questions/" + questionId).then(({ data }) => {
        this.question = data.data;
      });
    } else {
      const question = sessionStorage.getItem("question");
      if (question) {
        this.question = JSON.parse(question);
      }
    }
  },
  destroyed() {
    //文章自动保存功能
    this.autoSaveQuestion();
  },
  data: function() {
    return {
      addOrEdit: false,
      autoSave: true,
      categoryName: "",
      tagName: "",
      categoryList: [],
      tagList: [],
      question: {
        id: null,
        question: "",
        answer: "",
        categoryName: null,
        tagNameList: [],
      }
    };
  },
  methods: {
    /**
      查询分类
     */
    listCategories() {
      this.axios.get("/api/admin/categories/search").then(({ data }) => {
        this.categoryList = data.data;
      });
    },
    /**
      查询标签
     */
    listTags() {
      this.axios.get("/api/admin/tags/search").then(({ data }) => {
        this.tagList = data.data;
      });
    },
    /**
      发布文章
     */
    openModel() {
      if (this.question.question.trim() == "") {
        this.$message.error("文章标题不能为空");
        return false;
      }
      if (this.question.answer.trim() == "") {
        this.$message.error("文章内容不能为空");
        return false;
      }
      this.listCategories();
      this.listTags();
      this.addOrEdit = true;
    },
    uploadImg(pos, file) {
      var formdata = new FormData();
      if (file.size / 1024 < this.config.UPLOAD_SIZE) {
        formdata.append("file", file);
        this.axios
          .post("/api/admin/articles/images", formdata)
          .then(({ data }) => {
            this.$refs.md.$img2Url(pos, data.data);
          });
      } else {
        // 压缩到200KB,这里的200就是要压缩的大小,可自定义
        imageConversion
          .compressAccurately(file, this.config.UPLOAD_SIZE)
          .then(res => {
            formdata.append(
              "file",
              new window.File([res], file.name, { type: file.type })
            );
            this.axios
              .post("/api/admin/articles/images", formdata)
              .then(({ data }) => {
                this.$refs.md.$img2Url(pos, data.data);
              });
          });
      }
    },
    autoSaveQuestion() {
      // 自动上传文章
      if (
        this.autoSave &&
        this.question.question.trim() != "" &&
        this.question.answer.trim() != "" &&
        this.question.id != null
      ) {
        this.axios
          .post("/api/admin/questions", this.question)
          .then(({ data }) => {
            if (data.flag) {
              this.$notify.success({
                title: "成功",
                message: "自动保存成功"
              });
            } else {
              this.$notify.error({
                title: "失败",
                message: "自动保存失败"
              });
            }
          });
      }
      // 保存本地文章记录
      if (this.autoSave && this.question.id == null) {
        sessionStorage.setItem("question", JSON.stringify(this.question));
      }
    },


    /**
    草稿箱
     */
    saveQuestionDraft() {
      if (this.question.question.trim() == "") {
        this.$message.error("文章标题不能为空");
        return false;
      }
      if (this.question.answer.trim() == "") {
        this.$message.error("文章内容不能为空");
        return false;
      }
      this.question.status = 3;
      this.axios.post("/api/admin/questions", this.question).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: "保存草稿成功"
          });
        } else {
          this.$notify.error({
            title: "失败",
            message: "保存草稿失败"
          });
        }
      });
      //关闭自动保存功能
      this.autoSave = false;
    },
    /**
    保存问答
     */
    saveOrUpdateQuestion() {
      console.log(this.question);
      if (this.question.question.trim() == "") {
        this.$message.error("文章标题不能为空");
        return false;
      }
      if (this.question.answer.trim() == "") {
        this.$message.error("文章内容不能为空");
        return false;
      }
      if (this.question.categoryName == null) {
        this.$message.error("文章分类不能为空");
        return false;
      }
      if (this.question.tagNameList.length == 0) {
        this.$message.error("文章标签不能为空");
        return false;
      }

      this.axios.post("/api/admin/questions", this.question).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          sessionStorage.removeItem("question");
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.addOrEdit = false;
      });
      //关闭自动保存功能
      this.autoSave = false;
    },
    
    searchCategories(keywords, cb) {
      this.axios
        .get("/api/admin/categories/search", {
          params: {
            keywords: keywords
          }
        })
        .then(({ data }) => {
          cb(data.data);
        });
    },
    handleSelectCategories(item) {
      this.addCategory({
        categoryName: item.categoryName
      });
    },
    saveCategory() {
      if (this.categoryName.trim() != "") {
        this.addCategory({
          categoryName: this.categoryName
        });
        this.categoryName = "";
      }
    },
    addCategory(item) {
      this.question.categoryName = item.categoryName;
    },
    removeCategory() {
      this.question.categoryName = null;
    },
    searchTags(keywords, cb) {
      this.axios
        .get("/api/admin/tags/search", {
          params: {
            keywords: keywords
          }
        })
        .then(({ data }) => {
          cb(data.data);
        });
    },
    handleSelectTag(item) {
      this.addTag({
        tagName: item.tagName
      });
    },
    saveTag() {
      if (this.tagName.trim() != "") {
        this.addTag({
          tagName: this.tagName
        });
        this.tagName = "";
      }
    },
    addTag(item) {
      if (this.question.tagNameList.indexOf(item.tagName) == -1) {
        this.question.tagNameList.push(item.tagName);
      }
    },
    removeTag(item) {
      const index = this.question.tagNameList.indexOf(item);
      this.question.tagNameList.splice(index, 1);
    }
  },
  computed: {
    tagClass() {
      return function(item) {
        const index = this.question.tagNameList.indexOf(item.tagName);
        return index != -1 ? "tag-item-select" : "tag-item";
      };
    }
  }
};
</script>

<style scoped>
.article-title-container {
  display: flex;
  align-items: center;
  margin-bottom: 1.25rem;
  margin-top: 2.25rem;
}
.save-btn {
  margin-left: 0.75rem;
  background: #fff;
  color: #f56c6c;
}
.tag-item {
  margin-right: 1rem;
  margin-bottom: 1rem;
  cursor: pointer;
}
.tag-item-select {
  margin-right: 1rem;
  margin-bottom: 1rem;
  cursor: not-allowed;
  color: #ccccd8 !important;
}
.category-item {
  cursor: pointer;
  padding: 0.6rem 0.5rem;
}
.category-item:hover {
  background-color: #f0f9eb;
  color: #67c23a;
}
.popover-title {
  margin-bottom: 1rem;
  text-align: center;
}
.popover-container {
  margin-top: 1rem;
  height: 260px;
  overflow-y: auto;
}
</style>
