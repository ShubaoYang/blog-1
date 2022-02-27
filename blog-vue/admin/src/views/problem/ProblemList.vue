<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <!-- 文章状态 -->

    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button v-if="isDelete == 0" type="danger" size="small" icon="el-icon-delete" :disabled="questionIdList.length == 0" @click="updateIsDelete = true">
        批量删除
      </el-button>
      <el-button v-else type="danger" size="small" icon="el-icon-delete" :disabled="questionIdList.length == 0" @click="remove = true">
        批量删除
      </el-button>
      <!-- 条件筛选 -->
      <div style="margin-left:auto">

        <!-- 分类 -->
        <el-select clearable size="small" v-model="categoryId" filterable placeholder="请选择分类" style="margin-right:1rem">
          <el-option v-for="item in categoryList" :key="item.id" :label="item.categoryName" :value="item.id" />
        </el-select>
        <!-- 标签 -->
        <el-select clearable size="small" v-model="tagId" filterable placeholder="请选择标签" style="margin-right:1rem">
          <el-option v-for="item in tagList" :key="item.id" :label="item.tagName" :value="item.id" />
        </el-select>
        <!-- 文章名 -->
        <el-input clearable v-model="keywords" prefix-icon="el-icon-search" size="small" placeholder="请输入文章名" style="width:200px" @keyup.enter.native="searchQuestions" />
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left:1rem" @click="searchQuestions">
          搜索
        </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table border :data="questionList" @selection-change="selectionChange" v-loading="loading">
      <!-- 表格列 -->
      <el-table-column type="selection" width="55" />
      <!-- 文章标题 -->
      <el-table-column prop="question" label="标题" align="center" />
      <!-- 文章分类 -->
      <el-table-column prop="categoryName" label="分类" width="110" align="center" />
      <!-- 文章标签 -->
      <el-table-column prop="tags" label="标签" width="400" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item of scope.row.tags" :key="item.tagId" style="margin-right:0.2rem;margin-top:0.2rem">
            {{ item.tagName }}
          </el-tag>
        </template>
      </el-table-column>
      
      <!-- 文章发表时间 -->
      <el-table-column prop="createTime" label="发表时间" width="150" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" /> {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      
      <!-- 列操作 -->
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="editQuestion(scope.row.id)" v-if="scope.row.isDelete == 0">
            编辑
          </el-button>
          <el-popconfirm title="确定删除吗？" style="margin-left:10px" @confirm="updateQuestionDelete(scope.row.id)" v-if="scope.row.isDelete == 0">
            <el-button size="mini" type="danger" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm title="确定恢复吗？" v-if="scope.row.isDelete == 1" @confirm="updateQuestionDelete(scope.row.id)">
            <el-button size="mini" type="success" slot="reference">
              恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm style="margin-left:10px" v-if="scope.row.isDelete == 1" title="确定彻底删除吗？" @confirm="deleteQuestions(scope.row.id)">
            <el-button size="mini" type="danger" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination class="pagination-container" background @size-change="sizeChange" @current-change="currentChange" :current-page="current" :page-size="size" :total="count" :page-sizes="[10, 20]" layout="total, sizes, prev, pager, next, jumper" />
    <!-- 批量逻辑删除对话框 -->
    <el-dialog :visible.sync="updateIsDelete" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="updateIsDelete = false">取 消</el-button>
        <el-button type="primary" @click="updateQuestionDelete(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <!-- 批量彻底删除对话框 -->
    <el-dialog :visible.sync="remove" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否彻底删除选中项？</div>
      <div slot="footer">
        <el-button @click="remove = false">取 消</el-button>
        <el-button type="primary" @click="deleteQuestions(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listQuestions();
    this.listCategories();
    this.listTags();
  },
  data: function() {
    return {
      loading: true,
      updateIsDelete: false,
      remove: false,
      activeStatus: "all",
      questionList: [],
      questionIdList: [],
      categoryList: [],
      tagList: [],
      keywords: null,
      categoryId: null,
      tagId: null,
      isDelete: 0,
      current: 1,
      size: 10,
      count: 0
    };
  },
  methods: {
    selectionChange(questionList) {
      this.questionIdList = [];
      questionList.forEach(item => {
        this.questionIdList.push(item.id);
      });
    },
    searchQuestions() {
      this.current = 1;
      this.listQuestions();
    },
    editQuestion(id) {
      this.$router.push({ path: "/problems/" + id });
    },
    updateQuestionDelete(id) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.questionIdList;
      }
      param.isDelete = this.isDelete == 0 ? 1 : 0;
      this.axios.put("/api/admin/questions", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listQuestions();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.updateIsDelete = false;
      });
    },
    deleteQuestions(id) {
      var param = {};
      if (id == null) {
        param = { data: this.questionIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/admin/questions", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listQuestions();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.remove = false;
      });
    },
    sizeChange(size) {
      this.size = size;
      this.listQuestions();
    },
    currentChange(current) {
      this.current = current;
      this.listQuestions();
    },

    changeTop(question) {
      this.axios
        .put("/api/admin/questions/top", {
          id: question.id,
          isTop: question.isTop
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: "置顶成功"
            });
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            });
          }
          this.remove = false;
        });
    },
    listQuestions() {
      this.axios
        .get("/api/admin/questions", {
          params: {
            current: this.current,
            size: this.size,
            keywords: this.keywords,
            categoryId: this.categoryId,
            tagId: this.tagId,
            isDelete: this.isDelete
          }
        })
        .then(({ data }) => {
          this.questionList = data.data.recordList;
          this.count = data.data.count;
          this.loading = false;
        });
    },
    listCategories() {
      this.axios.get("/api/admin/categories/search").then(({ data }) => {
        this.categoryList = data.data;
      });
    },
    listTags() {
      this.axios.get("/api/admin/tags/search").then(({ data }) => {
        this.tagList = data.data;
      });
    }
  },
  watch: {
    categoryId() {
      this.current = 1;
      this.listQuestions();
    },
    tagId() {
      this.current = 1;
      this.listQuestions();
    },
    isDelete() {
      this.current = 1;
      this.listQuestions();
    }
  },
  computed: {
  }
};
</script>

<style scoped>
.operation-container {
  margin-top: 1.5rem;
}
.article-status-menu {
  font-size: 14px;
  margin-top: 40px;
  color: #999;
}
.article-status-menu span {
  margin-right: 24px;
}
.status {
  cursor: pointer;
}
.active-status {
  cursor: pointer;
  color: #333;
  font-weight: bold;
}
.article-cover {
  position: relative;
  width: 100%;
  height: 90px;
  border-radius: 4px;
}
.article-cover::after {
  content: "";
  background: rgba(0, 0, 0, 0.3);
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
}
.article-status-icon {
  color: #fff;
  font-size: 1.5rem;
  position: absolute;
  right: 1rem;
  bottom: 1.4rem;
}
</style>
