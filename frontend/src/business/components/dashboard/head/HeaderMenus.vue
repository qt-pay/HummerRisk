<template>
  <div id="menu-bar" v-if="isRouterAlive">
    <el-row type="flex">
      <el-col :span="24">
        <el-menu class="header-menu" :unique-opened="true" mode="horizontal" router :default-active='$route.path'>

          <el-menu-item :index="'/dashboard/panel'">
            {{ $t("dashboard.panel") }}
          </el-menu-item>

          <el-menu-item :index="'/dashboard/dashboard'">
            {{ $t("dashboard.overview") }}
          </el-menu-item>

          <el-menu-item :index="'/dashboard/analysis'">
            {{ $t("dashboard.analysis") }}
          </el-menu-item>

          <el-menu-item :index="'/dashboard/active'">
            {{ $t("dashboard.active") }}
          </el-menu-item>
        </el-menu>
      </el-col>
    </el-row>
  </div>

</template>

<script>

export default {
  name: "HeaderMenus",
  components: {},
  data() {
    return {
      path: '',
      isRouterAlive: true,
    }
  },
  methods: {
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
      });
    },
    init() {
    },
  },
  watch: {
    '$route'(to) {
      this.init();
    }
  },
  mounted() {
    this.init();
  }
}

</script>

<style scoped>
#menu-bar {
  border-bottom: 1px solid #E6E6E6;
  background-color: #FFF;
}

</style>
