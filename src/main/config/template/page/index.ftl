<style lang="less">

</style>
<template>
    <div class="search">
        <Row>
            <Col>
                <Card>
                  <Row>
                    <Form ref="searchForm" :model="searchForm" inline :label-width="70" class="search-form">
                      <Form-item label="名称" prop="name">
                        <Input type="text" v-model="searchForm.name" clearable placeholder="请输入名称" style="width: 200px"/>
                      </Form-item>
                      <Form-item style="margin-left:-35px;" class="br">
                        <Button @click="handleSearch" type="primary" icon="ios-search">搜索</Button>
                        <Button @click="handleReset" >重置</Button>
                      </Form-item>
                    </Form>
                  </Row>
                    <Row class="operation">
                        <Button @click="add" type="primary" icon="md-add">添加</Button>
                        <Button @click="delAll" icon="md-trash">批量删除</Button>
                        <Button @click="getDataList" icon="md-refresh">刷新</Button>
                    </Row>
                     <Row>
                        <Alert show-icon>
                            已选择 <span class="select-count">{{selectCount}}</span> 项
                            <a class="select-clear" @click="clearSelectAll">清空</a>
                        </Alert>
                    </Row>
                    <Row>
                        <Table :loading="loading" border :columns="columns" :data="data" ref="table" sortable="custom" @on-sort-change="changeSort" @on-selection-change="changeSelect"></Table>
                    </Row>
                    <Row type="flex" justify="end" class="page">
                        <Page :current="searchForm.pageNumber" :total="total" :page-size="searchForm.pageSize" @on-change="changePage" @on-page-size-change="changePageSize" :page-size-opts="[10,20,50]" size="small" show-total show-elevator show-sizer></Page>
                    </Row>
                </Card>
            </Col>
        </Row>
        <Modal :title="modalTitle" v-model="modalVisible" :mask-closable='false' :width="500">
          <Form ref="form" :model="form" :label-width="80" :rules="formValidate">
            <#assign ignoreColumns = ['id', 'tenantId', 'createBy', 'createDatetime', 'updateBy', 'updateDatetime', 'deleted'] />
            <#list columnList as column>
                <#if !_StringUtils.contains(column.humpName, ignoreColumns) >
		            <#if _StringUtils.contain("descr,remark", column.humpName)>
			            <FormItem label="${column.comment}" prop="${column.humpName}">
			              <Input type="textarea" v-model="form.billingRemark" :maxlength="2000"/>
			            </FormItem>
		            <#else>
			            <FormItem label="${column.comment}" prop="${column.humpName}">
			              <Input v-model="form.${column.humpName}"/>
			            </FormItem>
		            </#if>
              	</#if>
            </#list>          
          </Form>
          <div slot="footer">
            <Button type="text" @click="handleCancel">取消</Button>
            <Button type="primary" :loading="submitLoading" @click="handleSubmit">提交</Button>
          </div>
        </Modal>
    </div>
</template>

<script>
  import axios from '@/libs/api.request'
export default {
  name: "${module.instanceName}_index",
  data() {
    return {
      loading: true, // 表单加载状态
      modalType: 0, // 添加或编辑标识
      modalVisible: false, // 添加或编辑显示
      modalTitle: "", // 添加或编辑标题
      searchForm: {
        code: "",
        name: "",
        //分页排序参数
        sortColumn: "id", // 排序字段
        sortType: "desc", // 排序方式
        pageNumber: 1, // 当前页数
        pageSize: 10, // 页面大小
      },
      //表单
      form: {
      <#assign ignoreColumns = ['id', 'tenantId', "createBy", "createDatetime", 'updateBy', 'updateDatetime', 'deleted'] />
      <#list columnList as column>
      <#if !_StringUtils.contains(column.humpName, ignoreColumns) >
        ${column.humpName}: "",
      </#if>
      </#list>
      },
      //准备数据
      prepareData: {
        //chargeUserList:[]
      },
      // 表单验证规则
      formValidate: {
        //code: [{required: true, message: "不能为空", trigger: "blur"}],
        //name: [{required: true, message: "不能为空", trigger: "blur"}],
        //chargeBy: [{required: true, message: "不能为空", trigger: "blur"}],
        //remark: [{ required: false, message: "可以为空，最大长度1000", trigger: "blur"}]
      },
      submitLoading: false, // 添加或编辑提交状态
      selectList: [], // 多选数据
      selectCount: 0, // 多选计数
      columns: [
        // 表头
        {
          type: "selection",
          width: 60,
          align: "center"
        },
    <#assign ignoreColumns = ['id', 'tenantId', "createBy", "createDatetime", 'updateBy', 'updateDatetime', 'deleted'] />
      <#list columnList as column>
    <#if !_StringUtils.contains(column.humpName, ignoreColumns) >
        {
          title: "${column.comment}",
          key: "${column.humpName}",
          sortable: false
        },
      </#if>
    </#list>
        {
          title: "操作",
          key: "action",
          align: "center",
          width: 240,
          render: (h, params) => {

              return h("div", [
                h(
                  "Button",
                  {
                    props: {
                      type: "info",
                      size: "small",
                      icon: "ios-eye"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.show(params.row);
                      }
                    }
                  },
                  "查看"
                ),
                h(
                  "Button",
                  {
                    props: {
                      type: "primary",
                      size: "small",
                      icon: "ios-create-outline"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.edit(params.row);
                      }
                    }
                  },
                  "编辑"
                ),
                h(
                  "Button",
                  {
                    props: {
                      type: "error",
                      size: "small",
                      icon: "md-trash"
                    },
                    on: {
                      click: () => {
                        this.del(params.row);
                      }
                    }
                  },
                  "删除"
                )
              ]);

          }
        }
      ],
      data: [], // 表单数据
      total: 0 // 表单数据总数
    };
  },
  methods: {
    init() {
      this.getPrepareData();
      this.getDataList();
    },
    handleSearch() {
      this.searchForm.pageNumber = 1;
      this.getDataList();
    },
    handleReset() {
      //this.searchForm.name="";
      this.searchForm.pageNumber = 1;
      // 重新加载数据
      this.getDataList();
    },
    changePage(v) {
      this.searchForm.pageNumber = v;
      this.getDataList();
      this.clearSelectAll();
    },
    changePageSize(v) {
      this.pageSize = v;
      this.getDataList();
    },
    changeSort(e) {
      this.sortColumn = e.key;
      this.sortType = e.order;
      if (e.order === "normal") {
        this.sortType = "";
      }
      this.getDataList();
    },
    getPrepareData() {
      //请求参数字典
      // axios.get('mgr/dictParam/selectByType',{
      //     dictType : "customer-grade"
      //  }).then(res => {
      //   this.prepareData.gradeList = res.data;
      // });
      //获取用户列表
      // axios.get('mgr/tenantMember/selectAll', {}).then(res => {
      //   this.prepareData.chargeUserList = res.data;
      // });
    },
    getDataList() {
      this.loading = true;
      // 请求后端获取表单数据 请自行修改接口
      axios.get('mgr/${domain.packageName}/${module.instanceName}/selectByPage', this.searchForm).then(res => {
        this.loading = false;
        this.data = res.data.records;
        this.total = res.data.total;
        this.selectCount = 0;
        this.selectList = [];
      });

    },
    handleCancel() {
      this.modalVisible = false;
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          if (this.modalType === 0) {
            // 添加 避免编辑后传入id等数据 记得删除
            delete this.form.id;
            axios.post('mgr/${domain.packageName}/${module.instanceName}/save', this.form).then(res => {
              this.submitLoading = false;
              this.$Message.success("操作成功");
              this.getDataList();
              this.modalVisible = false;
            });
          } else {
            // 编辑
            axios.post('mgr/${domain.packageName}/${module.instanceName}/update', this.form).then(res => {
              this.submitLoading = false;
              this.$Message.success("操作成功");
              this.getDataList();
              this.modalVisible = false;
            });
          }
        }
      });
    },
    add() {
      this.modalType = 0;
      this.modalTitle = "添加";
      this.$refs.form.resetFields();
      delete this.form.id;
      this.modalVisible = true;
    },
    edit(data) {
      this.modalType = 1;
      this.modalTitle = "编辑";
      this.form = data;
      this.modalVisible = true;
    },
    del(v) {
      this.$Modal.confirm({
        title: "确认删除",
        // 记得确认修改此处
        content: "您确认要删除 " + v.name + " ?",
        onOk: () => {
          // 删除
          axios.post('mgr/${domain.packageName}/${module.instanceName}/delete',{
              id:v.id
            }).then(res => {
            this.submitLoading = false;
            this.$Message.success("操作成功");
            this.getDataList();
            this.modalVisible = false;
          });
        }
      });
    },
    delAll() {
      if (this.selectCount <= 0) {
        this.$Message.warning("您还未选择要删除的数据");
        return;
      }
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除所选的 " + this.selectCount + " 条数据?",
        onOk: () => {
          let ids = [];
          this.selectList.forEach(function(e) {
            ids.push(e.id);
          });
          // 批量删除
          axios.post('mgr/${domain.packageName}/${module.instanceName}/deleteBatch',{
            ids:ids
          }).then(res => {
            this.submitLoading = false;
            this.$Message.success("操作成功");
            this.getDataList();
            this.modalVisible = false;
          });
        }
      });
    },
    clearSelectAll() {
      this.$refs.table.selectAll(false);
    },
    changeSelect(e) {
      this.selectList = e;
      this.selectCount = e.length;
    },

  },
  mounted() {
    this.init();
  }
};
</script>
