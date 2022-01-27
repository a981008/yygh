<template>
  <div class="app-container">
    <!-- 条件查询 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="query.hosname" placeholder="医院名称" />
      </el-form-item>
      <el-form-item>
        <el-input v-model="query.hoscode" placeholder="医院编号" />
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="fetchData()"
        >查询</el-button
      >
    </el-form>
    <!-- 批量删除 -->
    <div>
      <el-button type="danger" size="mini" @click="removeRows()"
        >批量删除</el-button
      >
    </div>

    <!-- banner列表 -->
    <el-table
      :data="list"
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column type="index" width="50" label="序号" />
      <el-table-column prop="hosname" label="医院名称" />
      <el-table-column prop="hoscode" label="医院编号" />
      <el-table-column prop="apiUrl" label="api基础路径" width="200" />
      <el-table-column prop="contactsName" label="联系人姓名" />
      <el-table-column prop="contactsPhone" label="联系人手机" />

      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          {{ scope.row.status === 1 ? "可用" : "不可用" }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="removeDataById(scope.row.id)"
          >
          </el-button>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-edit"
               @click="updateById(scope.row.id)"
            >编辑
            </el-button>
          

          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="removeDataById(scope.row.id)"
            >删除</el-button
          >
          <el-button
            v-if="scope.row.status == 1"
            type="primary"
            size="mini"
            icon="el-icon-delete"
            @click="lockHostSet(scope.row.id, 0)"
            >锁定</el-button
          >
          <el-button
            v-if="scope.row.status == 0"
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="lockHostSet(scope.row.id, 1)"
            >取消锁定</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="page"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center"
      layout="total, prev, pager, next, jumper"
      @current-change="fetchData"
    />
  </div>
</template>


<script>
import hospset from "@/api/hospset";

export default {
  data() {
    return {
      current: 1,
      limit: 10,
      query: {},
      list: [],
      total: 0,
      multipleSelection: [], // 批量选择中选择的记录列表
      page: 1,
    };
  },
  created: function () {
    // 页面渲染之前执行
    this.fetchData();
  },
  methods: {
    fetchData(page = 1) {
      this.current = page;
      hospset
        .getHospSetiSetList(this.current, this.limit, this.query)
        .then((res) => {
          // 成功时调用
          this.list = res.data.records;
          this.total = res.data.total;
        })
        .catch((err) => {
          // 失败时调用
          console.log(err);
        });
    },
    updateById(id) {
      this.$router.push('/hospSet/edit/' + id)
    },
    removeDataById(id) {
      this.$confirm("此操作将永久删除医院是设置信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        //确定执行then方法
        //调用接口
        hospset.deleteHospSet(id).then((res) => {
          //提示
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          //刷新页面
          this.fetchData(1);
        });
      });
    },
    // 当表格复选框选项发生变化的时候触发
    handleSelectionChange(selection) {
      this.multipleSelection = selection;
    },
    //批量删除
    removeRows() {
      this.$confirm("此操作将永久删除医院是设置信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        //确定执行then方法
        var idList = [];
        //遍历数组得到每个id值，设置到idList里面
        for (var i = 0; i < this.multipleSelection.length; i++) {
          var obj = this.multipleSelection[i];
          var id = obj.id;
          idList.push(id);
        }
        //调用接口
        hospset.batchRemoveHospSet(idList).then((response) => {
          //提示
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          //刷新页面
          this.fetchData(1);
        });
      });
    },
    //锁定和取消锁定
    lockHostSet(id, status) {
      hospset.lockHospSet(id, status).then((response) => {
        //刷新
        this.fetchData();
      });
    },
  },
};
</script>
