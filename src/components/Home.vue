<style scoped>
.home-layout {
  border: 0;
  padding: 0;
  margin: 0;
  width: 100%;
}

.ivu-layout-header {
  padding: 0;
  margin: 0;
  width: 100%;
  height: 80px;
  background-color: #ffffff;
  border-bottom: 1px solid #e1e1e1;
  box-shadow: 0 1px 1px #14000000;
}

.home-layout-logo {
  width: auto;
  line-height: 50px;
  height: 50px;
  text-align: center;
  font-size: 20px;
  color: rgb(82, 168, 255);
  border-radius: 3px;
  float: left;
  position: relative;
  top: 15px;
  left: 20px;
}

.home-layout-nav {
  width: auto;
  line-height: 50px;
  height: 50px;
  text-align: center;
  font-size: 16px;
  color: #999;
  float: right;
  right: 100px;
  top: 15px;
  position: absolute;
}

.ivu-layout-content {
  height: auto;
  margin-top: 12px;
  overflow: hidden;
  background: #fff;
}
.ivu-layout-sider {
  background-color: #ffffff;
  margin-top: 12px;
}

.ivu-layout-content {
  text-align: left;
  padding: 15px;
  height: auto;
}

.ivu-layout-footer {
  text-align: center;
  border: 0;
  padding: 0 0 10px 0;
  color: #9ea7b4;
  width: 100%;
  line-height: 28px;
  padding-top: 12px;
}


.badge-inc {
  width: 42px;
  height: 42px;
  background: #eee;
  border-radius: 6px;
  display: inline-block;
  font-size: 9px;
}
</style>
<style>
  .ivu-select-dropdown {
    z-index: 9999 !important;
  }

  .auto-search {
    width: 250px !important;
    margin-left: 20px;
  }

  .demo-auto-complete-item {
    padding: 4px 0;
    border-bottom: 1px solid #f6f6f6;

    cursor: pointer;
  }

  .demo-auto-complete-group {
    font-size: 6px;
    padding: 4px 6px;
  }

  .demo-auto-complete-group span {
    color: #666;
    font-weight: bold;
  }

  .demo-auto-complete-group span.search-result-title {
    color: #5cadff;
  }

</style>
<template>
<div class="home-layout">
    <Header>
      <div class="home-layout-logo">
            <span> {{info.name}}  接口协议</span>
            <AutoComplete class="auto-search" placeholder="搜索" icon="ios-search" @on-search="handleSearch">
                <div class="demo-auto-complete-item" v-for="item in searchList" v-on:click="handleCheckMenu(item)">
                    <div class="demo-auto-complete-group" v-if="item.name !='...'">
                        <span class="search-result-title" v-html="splitTitle(item.title)"></span>-<span class="name">{{ item.name }}</span>
                    </div>
                    <div v-else>
                        <span class="center">...</span>
                    </div>
                </div>
            </AutoComplete>
        </div>
        <div class="home-layout-nav">
          <span data-v-8dc7cce2="" class="ivu-badge" v-if="info.finishCount>0" >
            <Button type="dashed">完成数</Button>    
            <sup class="ivu-badge-count badge-finish ivu-badge-count-success" style="top:0">{{info.finishCount}}</sup>
          </span>
          <span data-v-8dc7cce2="" class="ivu-badge"  v-if="info.incCount>0">
            <Button type="dashed">接口总数</Button>
            <sup class="ivu-badge-count ivu-badge-count-normal" style="top:0">{{info.incCount}}</sup>
          </span>                       
          <Button @click="handleConfig" type="primary">系统配置</Button>
        </div>
    </Header>
    <Layout>
        <Sider width="300">                       
            <NavMenu ref="menu"></NavMenu>               
        </Sider>
        <Content>
          <router-view></router-view>
        </Content>
    </Layout>
    <Footer>
      2017-2018 &copy; {{info.copyright}}
    </Footer>
      <Drawer title="系统配置" :closable="false" v-model="configDrawer" width="450">
        <Form>
          <FormItem label="文档地址" label-position="top">
          <Input v-model="configData.baseUrl" placeholder="接口文档地址"/>
          </FormItem>
        </Form>
        <div class="demo-drawer-footer">
          <Button type="primary" @click="handleSaveConfig">保存</Button>
        </div>
      </Drawer>   
</div>
</template>

<script>
import NavMenu from "@/components/NavMenu";
import { dataToArray } from "../utils/data";
import { getBasicDefinition } from "@/api/doc";
export default {
  name: "Home",
  components: {
    NavMenu: NavMenu
  },
  data() {
    return {
      configDrawer: false,
      configData: {
        baseUrl: ""
      },
      info: {
        name: "",
        copyright: "",
        incCount: 0,
        finishCount: 0
      },
      menuHeight: document.documentElement.clientHeight * 0.9,
      searchList: []
    };
  },
  created() {
    this.initConfig();
    getBasicDefinition((isSuccess, response) => {
      if (isSuccess) {
        this.info = response.basic;
        this.$store.commit("setBasicInfo", response);
        document.title = response.basic.name;
      } else {
        console.log("初始化失败");
      }
    });
  },
  mounted() {
    this.$nextTick(function() {
      this.routePath = this.$route.path;
    });
  },
  methods: {
    handleSearch(value) {
      var result = this.$refs.menu.searchapi(value);
      this.searchList = result;
    },
    splitTitle(title) {
      var titleArray = title.split(".");
      return titleArray[titleArray.length - 1];
    },
    handleConfig() {
      this.configDrawer = true;
    },
    handleCheckMenu(value) {
      if (value.hasOwnProperty("url")) {
        this.$refs.menu.menuLink(value.url);
        if (value.group != undefined && value.group != "") {
          this.$refs.menu.openNames = [value.group];
        } else {
          this.$refs.menu.openNames = [value.name];
        }
      }
    },
    handleSaveConfig() {
      let configStr = JSON.stringify(this.configData);
      localStorage.setItem("config", configStr);
      location.reload();
    },
    initConfig() {
      let config = localStorage.getItem("config");
      if (config != null) {
        try {
          config = JSON.parse(config);
          this.configData = config;
        } catch (e) {
          this.$Notice.warning({
            title: "初始化失败",
            desc: "系统配置失败"
          });
        }
      }
      this.$store.commit("setConfig", this.configData);
    }
  }
};
</script>
