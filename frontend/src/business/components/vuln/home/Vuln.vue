<template>
  <main-container>
    <el-card class="table-card" v-loading="result.loading">
      <template v-slot:header>
        <table-header :condition.sync="condition" @search="search"
                      :title="$t('vuln.vuln_settings_list')"
                      @create="create" :createTip="$t('vuln.create')"
                      @validate="validate" :validateTip="$t('account.one_validate')"
                      :show-validate="true" :show-create="true"
                      :items="items" :columnNames="columnNames"
                      :checkedColumnNames="checkedColumnNames" :checkAll="checkAll2" :isIndeterminate="isIndeterminate2"
                      @handleCheckedColumnNamesChange="handleCheckedColumnNamesChange" @handleCheckAllChange="handleCheckAllChange"/>
      </template>

      <hide-table
        :table-data="tableData"
        @sort-change="sort"
        @filter-change="filter"
        @select-all="select"
        @select="select"
      >
        <el-table-column type="selection" min-width="40">
        </el-table-column>
        <el-table-column type="index" min-width="40"/>
        <el-table-column prop="name" v-if="checkedColumnNames.includes('name')" :label="$t('vuln.name')" min-width="150" show-overflow-tooltip></el-table-column>
        <el-table-column :label="$t('vuln.platform')" v-if="checkedColumnNames.includes('pluginName')" min-width="110" show-overflow-tooltip>
          <template v-slot:default="scope">
              <span>
                <img :src="require(`@/assets/img/platform/${scope.row.pluginIcon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                 &nbsp;&nbsp; {{ scope.row.pluginName }}
              </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" min-width="90" v-if="checkedColumnNames.includes('status')" :label="$t('vuln.status')"
                         column-key="status"
                         :filters="statusFilters"
                         :filter-method="filterStatus">
          <template v-slot:default="{row}">
            <vuln-status :row="row"/>
          </template>
        </el-table-column>
        <el-table-column min-width="150" v-if="checkedColumnNames.includes('createTime')" :label="$t('account.create_time')" sortable
                         prop="createTime">
          <template v-slot:default="scope">
            <span>{{ scope.row.createTime | timestampFormatDate }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="150" v-if="checkedColumnNames.includes('updateTime')" :label="$t('account.update_time')" sortable
                         prop="updateTime">
          <template v-slot:default="scope">
            <span>{{ scope.row.updateTime | timestampFormatDate }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="userName" v-if="checkedColumnNames.includes('userName')" :label="$t('account.creator')" min-width="90" show-overflow-tooltip/>
        <el-table-column min-width="170" :label="$t('commons.operating')" fixed="right">
          <template v-slot:default="scope">
            <table-operators :buttons="buttons" :row="scope.row"/>
          </template>
        </el-table-column>
      </hide-table>
      <table-pagination :change="search" :current-page.sync="currentPage" :page-size.sync="pageSize" :total="total"/>
    </el-card>

    <!--Create vuln-->
    <el-drawer class="rtl" :title="$t('vuln.create')" :visible.sync="createVisible" size="50%" :before-close="handleClose" :direction="direction"
               :destroy-on-close="true">
      <div v-for="(form, index) in addAccountForm" :key="index">
        <el-form :model="form" label-position="right" label-width="150px" size="medium" :rules="rule" :ref="'addAccountForm' + index">
          <el-form-item :label="$t('vuln.name')" ref="name" prop="name">
            <el-input v-model="form.name" autocomplete="off" :placeholder="$t('vuln.name')"/>
          </el-form-item>
          <el-form-item :label="$t('vuln.platform')" :rules="{required: true, message: $t('vuln.platform') + $t('commons.cannot_be_empty'), trigger: 'change'}">
            <el-select style="width: 100%;" filterable :clearable="true" v-model="form.pluginId" :placeholder="$t('vuln.platform')" @change="changePluginForAdd(form)">
              <el-option
                v-for="item in plugins"
                :key="item.id"
                :label="item.name"
                :value="item.id">
                <img :src="require(`@/assets/img/platform/${item.icon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                &nbsp;&nbsp; {{ item.name }}
              </el-option>
            </el-select>
          </el-form-item>
          <div v-for="(tmp, index) in form.tmpList" :key="index">
            <el-form-item v-if="tmp.inputType === 'password'" :label="tmp.label" style="margin-bottom: 29px">
              <el-input :type="tmp.inputType" v-model="tmp.input" autocomplete="new-password" show-password :placeholder="tmp.description"/>
            </el-form-item>
            <el-form-item v-if="tmp.inputType !== 'password' && tmp.inputType !== 'boolean'" :label="tmp.label">
              <el-input :type="tmp.inputType" v-model="tmp.input" autocomplete="off" :placeholder="tmp.description"/>
            </el-form-item>
          </div>
          <el-form-item v-if="form.isProxy && form.pluginId" :label="$t('commons.proxy')" :rules="{required: true, message: $t('commons.proxy') + $t('commons.cannot_be_empty'), trigger: 'change'}">
            <el-select style="width: 100%;" filterable :clearable="true" v-model="form.proxyId" :placeholder="$t('commons.proxy')">
              <el-option
                v-for="item in proxys"
                :key="item.id"
                :label="item.proxyIp"
                :value="item.id">
                &nbsp;&nbsp; {{ item.proxyIp + ':' + item.proxyPort }}
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-if="form.pluginId" :label="$t('proxy.is_proxy')" :rules="{required: true, message: $t('commons.proxy') + $t('commons.cannot_be_empty'), trigger: 'change'}">
            <el-switch v-model="form.isProxy"></el-switch>
          </el-form-item>
          <el-form-item v-if="index > 0" :label="$t('vuln.delete_this_vuln')">
            <el-button type="danger" icon="el-icon-delete" plain size="small" @click="deleteAccount(addAccountForm, form)">{{ $t('commons.delete') }}</el-button>
          </el-form-item>
        </el-form>
        <el-divider><i class="el-icon-first-aid-kit"> {{ (index + 1) }}</i></el-divider>
      </div>
      <proxy-dialog-create-footer
        @cancel="createVisible = false"
        @addAccount="addAccount(addAccountForm)"
        @confirm="saveAccount(addAccountForm, 'add')"/>
    </el-drawer>
    <!--Create vuln-->

    <!--Update vuln-->
    <el-drawer class="rtl" :title="$t('vuln.update')" :visible.sync="updateVisible" size="50%" :before-close="handleClose" :direction="direction"
               :destroy-on-close="true">
      <el-form :model="form" label-position="right" label-width="150px" size="small" :rules="rule" ref="accountForm">
        <el-form-item :label="$t('vuln.name')"  ref="name" prop="name">
          <el-input v-model="form.name" autocomplete="off" :placeholder="$t('vuln.name')"/>
        </el-form-item>
        <el-form-item :label="$t('vuln.platform')" :rules="{required: true, message: $t('vuln.platform') + $t('commons.cannot_be_empty'), trigger: 'change'}">
          <el-select style="width: 100%;" disabled v-model="form.pluginId" :placeholder="$t('vuln.platform')" @change="changePlugin(form.pluginId)">
            <el-option
              v-for="item in plugins"
              :key="item.id"
              :label="item.name"
              :value="item.id">
              <img :src="require(`@/assets/img/platform/${item.icon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
              &nbsp;&nbsp; {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <div v-for="(tmp, index) in tmpList" :key="index">
          <el-form-item v-if="tmp.inputType === 'password'" :label="tmp.label" style="margin-bottom: 29px">
            <el-input :type="tmp.inputType" v-model="tmp.input" @input="change($event)" autocomplete="new-password" show-password :placeholder="tmp.description"/>
          </el-form-item>
          <el-form-item v-if="tmp.inputType !== 'password' && tmp.inputType !== 'boolean'" :label="tmp.label">
            <el-input :type="tmp.inputType" v-model="tmp.input" @input="change($event)" autocomplete="off" :placeholder="tmp.description"/>
          </el-form-item>
        </div>
      </el-form>
      <proxy-dialog-footer
        @cancel="updateVisible = false"
        @confirm="editAccount(form, 'update')"/>
    </el-drawer>
    <!--Update vuln-->

    <!-- 一键检测选择规则组 -->
    <el-dialog :close-on-click-modal="false"
               :modal-append-to-body="false"
               :title="$t('account.scan_group_quick')"
               :visible.sync="scanVisible"
               class="" width="70%">
      <div v-loading="groupResult.loading">
        <el-card class="box-card el-box-card">
          <div slot="header" class="clearfix">
              <span>
                <img :src="require(`@/assets/img/platform/${accountWithGroup.pluginIcon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
             &nbsp;&nbsp; {{ accountWithGroup.pluginName }} {{ $t('rule.rule_set') }} | {{ accountWithGroup.name }}
              </span>
            <el-button style="float: right; padding: 3px 0" type="text"  @click="handleCheckAllByAccount">{{ $t('account.i18n_sync_all') }}</el-button>
          </div>
          <el-checkbox-group v-model="checkedGroups">
            <el-checkbox v-for="(group, index) in groups" :label="group.id" :value="group.id" :key="index" border >
              {{ group.name }}
            </el-checkbox>
          </el-checkbox-group>
        </el-card>
        <dialog-footer
          @cancel="scanVisible = false"
          @confirm="scanGroup()"/>
      </div>
    </el-dialog>
    <!-- 一键检测选择检测组 -->

  </main-container>
</template>

<script>
import TablePagination from "../../common/pagination/TablePagination";
import TableHeader from "@/business/components/common/components/TableHeader";
import TableOperator from "../../common/components/TableOperator";
import Container from "../../common/components/Container";
import MainContainer from "../../common/components/MainContainer";
import VulnStatus from "./VulnStatus";
import TableOperators from "../../common/components/TableOperators";
import {_filter, _sort} from "@/common/js/utils";
import {VULN_CONFIGS} from "../../common/components/search/search-components";
import ProxyDialogFooter from "@/business/components/common/components/ProxyDialogFooter";
import ProxyDialogCreateFooter from "@/business/components/common/components/ProxyDialogCreateFooter";
import DialogFooter from "@/business/components/common/components/DialogFooter";
import {VULN_ID, VULN_NAME} from "@/common/js/constants";
import HideTable from "@/business/components/common/hideTable/HideTable";

//列表展示与隐藏
const columnOptions = [
  {
    label: 'vuln.name',
    props: 'name',
    disabled: false
  },
  {
    label: 'vuln.platform',
    props: 'pluginName',
    disabled: false
  },
  {
    label: 'vuln.status',
    props: 'status',
    disabled: false
  },
  {
    label: 'account.create_time',
    props: 'createTime',
    disabled: false
  },
  {
    label: 'account.update_time',
    props: 'updateTime',
    disabled: false
  },
  {
    label: 'account.creator',
    props: 'userName',
    disabled: false
  }
];

/* eslint-disable */
export default {
  components: {
    TableOperators,
    VulnStatus,
    MainContainer,
    Container,
    TableHeader,
    TablePagination,
    TableOperator,
    DialogFooter,
    ProxyDialogFooter,
    ProxyDialogCreateFooter,
    HideTable,
  },
  provide() {
    return {
      search: this.search,
    }
  },
  data() {
    return {
      credential: {},
      result: {},
      groupResult: {},
      condition: {
        components: VULN_CONFIGS
      },
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      selectIds: new Set(),
      createVisible: false,
      updateVisible: false,
      scanVisible: false,
      innerDrawer: false,
      innerDrawerProxy: false,
      plugins: [],
      proxys: [],
      tmpList: [],
      item: {},
      form: {},
      addAccountForm: [ { "name":"", "pluginId": "", "isProxy": false, "proxyId": "", "script": "", "tmpList": [] } ],
      proxyForm: {},
      script: '',
      direction: 'rtl',
      rule: {
        name: [
          {required: true, message: this.$t('commons.input_name'), trigger: 'blur'},
          {min: 2, max: 150, message: this.$t('commons.input_limit', [2, 150]), trigger: 'blur'},
          {
            required: true,
            message: this.$t("workspace.special_characters_are_not_supported"),
            trigger: 'blur'
          }
        ],
        proxyIp: [
          {required: true, message: this.$t('proxy.proxy_ip'), trigger: 'blur'},
          {min: 2, max: 50, message: this.$t('commons.input_limit', [2, 50]), trigger: 'blur'},
        ],
        proxyPort: [
          {required: true, message: this.$t('proxy.proxy_port'), trigger: 'blur'},
          {min: 2, max: 50, message: this.$t('commons.input_limit', [2, 50]), trigger: 'blur'},
        ],
        proxyName: [
          {required: false, message: this.$t('proxy.proxy_name'), trigger: 'blur'},
          {min: 2, max: 150, message: this.$t('commons.input_limit', [2, 150]), trigger: 'blur'},
        ],
        proxyPassword: [
          {required: false, message: this.$t('proxy.proxy_password'), trigger: 'blur'},
          {min: 2, max: 50, message: this.$t('commons.input_limit', [2, 50]), trigger: 'blur'},
        ],
      },
      buttons: [
        {
          tip: this.$t('account.one_scan'), icon: "el-icon-s-promotion", type: "success",
          exec: this.openScanGroup
        },
        {
          tip: this.$t('account.tuning'), icon: "el-icon-setting", type: "warning",
          exec: this.handleScan
        }, {
          tip: this.$t('commons.edit'), icon: "el-icon-edit", type: "primary",
          exec: this.handleEdit
        }, {
          tip: this.$t('commons.delete'), icon: "el-icon-delete", type: "danger",
          exec: this.handleDelete
        }
      ],
      statusFilters: [
        {text: this.$t('account.INVALID'), value: 'INVALID'},
        {text: this.$t('account.VALID'), value: 'VALID'},
        {text: this.$t('account.DELETE'), value: 'DELETE'}
      ],
      cmOptions: {
        tabSize: 4,
        mode: {
          name: 'json',
          json: true
        },
        theme: 'bespin',
        lineNumbers: true,
        line: true,
        indentWithTabs: true,
      },
      checkAll: false,
      //选中的规则组的acccount/id集合
      checkedGroups: [],
      //漏洞检测信息规则组list分组
      accountGroups: [],
      isIndeterminate: true,
      //漏洞检测信息规则组拼接可用类型： [acccount/id]
      groupsSelect: [],
      proxyType: [
        {id: 'Http', value: "Http"},
        {id: 'Https', value: "Https"},
      ],
      accountWithGroup: {pluginIcon: 'xray.png'},
      groups: [],
      checkedColumnNames: columnOptions.map((ele) => ele.props),
      columnNames: columnOptions,
      //名称搜索
      items: [
        {
          name: 'vuln.name',
          id: 'name'
        },
        {
          name: 'account.creator',
          id: 'userName'
        }
      ],
      checkAll2: true,
      isIndeterminate2: false,
    }
  },

  watch: {
    '$route': 'init'
  },

  methods: {
    handleCheckedColumnNamesChange(value) {
      const checkedCount = value.length;
      this.checkAll2 = checkedCount === this.columnNames.length;
      this.isIndeterminate2 = checkedCount > 0 && checkedCount < this.columnNames.length;
      this.checkedColumnNames = value;
    },
    handleCheckAllChange(val) {
      this.checkedColumnNames = val ? this.columnNames.map((ele) => ele.props) : [];
      this.isIndeterminate2 = false;
      this.checkAll2 = val;
    },
    create() {
      this.addAccountForm = [ { "name":"", "pluginId": "", "isProxy": false, "proxyId": "", "script": "", "tmpList": [] } ];
      this.createVisible = true;
      this.activePlugin();
      this.activeProxy();
    },
    innerDrawerClose() {
      this.innerDrawer = false;
    },
    innerDrawerProxyClose() {
      this.innerDrawerProxy = false;
    },
    //校验漏洞检测账号
    validate() {
      if (this.selectIds.size === 0) {
        this.$warning(this.$t('vuln.please_choose_vuln'));
        return;
      }
      this.$alert(this.$t('account.one_validate') + this.$t('vuln.vuln_setting') + " ？", '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            let formData = new FormData();
            this.result = this.$request({
              method: 'POST',
              url: "/account/validate",
              data: Array.from(this.selectIds),
              headers: {
                'Content-Type': undefined
              }
            }, res => {
              if (res.data) {
                this.$success(this.$t('account.success'));
              } else {
                this.$error(this.$t('account.error'));
              }
              this.search();
            });
          }
        }
      });
    },
    select(selection) {
      this.selectIds.clear();
      selection.forEach(s => {
        this.selectIds.add(s.id)
      });
    },
    //查询列表
    search() {
      let url = "/vuln/vulnList/" + this.currentPage + "/" + this.pageSize;
      this.result = this.$post(url, this.condition, response => {
        let data = response.data;
        this.total = data.itemCount;
        this.tableData = data.listObject;
      });
    },
    handleEdit(tmp) {
      this.form = tmp;
      this.item.credential = tmp.credential;
      if (!this.form.proxyId) {
        this.form.proxyId = "";
      }
      this.updateVisible = true;
      this.activePlugin();
      this.activeProxy();
      this.changePlugin(tmp.pluginId, 'edit');
    },
    handleClose() {
      this.createVisible =  false;
      this.updateVisible =  false;
      this.handleScan = false;
    },
    handleDelete(obj) {
      this.$alert(this.$t('account.delete_confirm') + obj.name + " ？", '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            this.result = this.$post("/account/delete/" + obj.id, {}, () => {
              this.$success(this.$t('commons.delete_success'));
              this.search();
            });
          }
        }
      });
    },
    change(e) {
      this.$forceUpdate();
    },
    //调参漏洞检测信息对应的规则
    handleScan(params) {
      this.$router.push({
        path: '/vuln/vlunscan/' + params.id,
      }).catch(error => error);
    },
    //查询插件
    activePlugin() {
      let url = "/plugin/vuln";
      this.result = this.$get(url, response => {
        let data = response.data;
        this.plugins =  data;
      });
    },
    //查询代理
    activeProxy() {
      let url = "/proxy/list/all";
      this.result = this.$get(url, response => {
        this.proxys = response.data;
      });
    },
    init() {
      this.selectIds.clear();
      this.search();
    },
    sort(column) {
      _sort(column, this.condition);
      this.init();
    },
    filter(filters) {
      _filter(filters, this.condition);
      this.init();
    },
    filterStatus(value, row) {
      return row.status === value;
    },
    //新增漏洞检测信息/选择插件查询漏洞检测信息
    async changePluginForAdd (form){
      let url = "/plugin/";
      this.result = await this.$get(url + form.pluginId, response => {
        let fromJson = typeof(response.data) === 'string'?JSON.parse(response.data):response.data;
        form.tmpList = fromJson.data;
        for (let tmp of form.tmpList) {
          if (tmp.defaultValue !== undefined) {
            tmp.input = tmp.defaultValue;
          }
        }
      });
    },
    //编辑漏洞检测信息/选择插件查询漏洞检测信息
    async changePlugin (pluginId, type){
      let url = "/plugin/";
      this.result = await this.$get(url + pluginId, response => {
        let fromJson = typeof(response.data) === 'string'?JSON.parse(response.data):response.data;
        this.tmpList = fromJson.data;
        if (type === 'edit') {
          let credentials = typeof(this.item.credential) === 'string'?JSON.parse(this.item.credential):this.item.credential;
          for (let tmp of this.tmpList) {
            if (credentials[tmp.name] === undefined) {
              tmp.input = tmp.defaultValue?tmp.defaultValue:"";
            } else {
              tmp.input = credentials[tmp.name];
            }
          }
        } else {
          for (let tmp of this.tmpList) {
            if (tmp.defaultValue !== undefined) {
              tmp.input = tmp.defaultValue;
            }
          }
        }
      });
    },
    //保存漏洞检测信息
    saveAccount(addAccountForm, type){
      for (let item of addAccountForm) {
        if (!item.tmpList.length) {
          this.$warning(this.$t('account.no_plugin_param'));
          return;
        }
        let data = {}, key = {};
        for (let tmp of item.tmpList) {
          if(!tmp.input) {
            this.$warning(this.$t('vuln.no_plugin_param') + tmp.label);
            return;
          }
          key[tmp.name] = tmp.input.trim();
        }
        data["credential"] = JSON.stringify(key);
        data["name"] = item.name;
        data["pluginId"] = item.pluginId;
        if (item.isProxy) data["proxyId"] = item.proxyId;

        if (type === 'add') {
          this.result = this.$post("/account/add", data,response => {
            if (response.success) {
              this.$success(this.$t('account.i18n_hr_create_success'));
              this.search();
              this.handleClose();
              location.reload();
            } else {
              this.$error(response.message);
            }
          });
        }
      }
    },
    //编辑漏洞检测信息
    editAccount(item, type){
      if (!this.tmpList.length) {
        this.$error(this.$t('account.i18n_account_cloud_plugin_param'));
        return;
      }
      this.$refs['accountForm'].validate(valid => {
        if (valid) {
          let data = {}, key = {};
          for (let tmp of this.tmpList) {
            key[tmp.name] = tmp.input;
          }
          data["credential"] = JSON.stringify(key);
          data["name"] = item.name;
          data["pluginId"] = item.pluginId;
          if (item.isProxy) data["proxyId"] = item.proxyId;

          if (type === 'add') {
            this.result = this.$post("/account/add", data,response => {
              if (response.success) {
                this.$success(this.$t('account.i18n_hr_create_success'));
                this.search();
                this.handleClose();
              } else {
                this.$error(response.message);
              }
            });
          } else {
            data["id"] = item.id;
            this.result = this.$post("/account/update", data,response => {
              if (response.success) {
                this.$success(this.$t('account.i18n_hr_update_success'));
                this.handleClose();
                this.search();
              } else {
                this.$error(response.message);
              }
            });
          }
        } else {
          this.$error(this.$t('rule.full_param'));
          return false;
        }
      });
    },
    openScanGroup(account) {
      this.accountWithGroup = account;
      localStorage.setItem(VULN_ID, account.id);
      localStorage.setItem(VULN_NAME, account.name);
      this.initGroups(account.pluginId);
      this.scanVisible = true;
    },
    scanGroup () {
      let account = this.$t('account.one_scan') + this.$t('vuln.vuln_rule');
      this.$alert( account + " ？", '', {
        confirmButtonText: this.$t('commons.confirm'),
        callback: (action) => {
          if (action === 'confirm') {
            if (this.checkedGroups.length === 0) {
              this.$warning(this.$t('account.please_choose_rule_group'));
              return;
            }
            let params = {
              accountId: this.accountWithGroup.id,
              groups: this.checkedGroups
            }
            this.groupResult = this.$post("/rule/scan", params, () => {
              this.$success(this.$t('account.i18n_hr_create_success'));
              this.scanVisible = false;
              this.$router.push({
                path: '/vuln/result',
              }).catch(error => error);
            });
          }
        }
      });
    },
    createProxy(createProxyForm) {
      this.$refs[createProxyForm].validate(valid => {
        if (valid) {
          this.result = this.$post('/proxy/add', this.proxyForm, () => {
            this.$success(this.$t('commons.save_success'));
            this.innerDrawerProxy = false;
            this.activeProxy();
          });
        } else {
          return false;
        }
      })
    },
    initGroups(pluginId) {
      this.result = this.$get("/rule/groupsByAccountId/" + pluginId,response => {
        this.groups = response.data;
      });
    },
    updateProxy(updateProxyForm) {
      this.$refs[updateProxyForm].validate(valid => {
        if (valid) {
          this.result = this.$post('/proxy/update', this.proxyForm, () => {
            this.$success(this.$t('commons.modify_success'));
            this.innerDrawerProxy = false;
          });
        } else {
          return false;
        }
      })
    },
    handleCheckAllByAccount(val, index) {
      let arr = [];
      if (val) {
        for (let obj of val.groups) {
          arr.push(val.accountWithBLOBs.id + "/" + obj.id);
        }
      }
      let concatArr = this.checkedGroups.concat(arr);
      this.checkedGroups = !this.isContain(this.checkedGroups, arr) ? Array.from(concatArr) : this.checkedGroups.filter(n => !arr.toString().includes(n));
      this.checkAll = this.checkedGroups.length === this.groupsSelect.length;
      this.isIndeterminate = this.checkedGroups.length > 0 && this.checkedGroups.length < this.groupsSelect.length;
    },
    handleCheckAll() {
      this.checkedGroups = this.checkedGroups.length === 0 ? this.groupsSelect : [];
      this.checkAll = this.checkedGroups.length === this.groupsSelect.length;
      this.isIndeterminate = this.checkedGroups.length > 0 && this.checkedGroups.length < this.groupsSelect.length;
    },
    handleCheckedGroupsChange(value) {
      let checkedCount = value.checkedGroups.length;
      this.checkAll = checkedCount === this.groupsSelect.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.groupsSelect.length;
    },
    isContain (arr1, arr2) {
      for (var i = arr2.length - 1; i >= 0; i--) {
        for (let obj of arr1) {
          if(obj === arr2[i]){
            return true;
          }
        }
      }
      return false;
    },
    addAccount (addAccountForm) {
      let newParam = { "name":"", "pluginId": "", "isProxy": false, "proxyId": "", "script": "", "tmpList": [] };
      addAccountForm.push(newParam);
    },
    deleteAccount (parameter, p) {
      for (let i in parameter) {
        if (parameter[i].name === p.name) {
          parameter.splice(i, 1);
          return;
        }
      }
    },
  },
  created() {
    this.init();
  }

}
</script>

<style scoped>
.table-content {
  width: 100%;
}

.el-table {
  cursor: pointer;
}

.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  padding: 10px 10%;
  width: 47%;
}

.rtl >>> .el-drawer__body {
  overflow-y: auto;
  padding: 20px;
}
.rtl >>> input {
  width: 100%;
}
.rtl >>> .el-select {
  width: 80%;
}
.rtl >>> .el-form-item__content {
  width: 75%;
}
.code-mirror {
  height: 600px !important;
}
.code-mirror >>> .CodeMirror {
  /* Set height, width, borders, and global font properties here */
  height: 600px !important;
}
/deep/ :focus{outline:0;}
.el-box-card {
  margin: 10px 0;
}
</style>
