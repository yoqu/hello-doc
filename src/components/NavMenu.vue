<style scoped>
.menu-layout {
  padding: 15px 0;
}

.menu-item-dot {
  width: 6px;
  height: 6px;
  margin-bottom: 6px;
  display: inline-block;
  background-color: #ff0000;
  -moz-border-radius: 3px;
  -webkit-border-radius: 3px;
  border-radius: 3px;
}

.menu-item-name {
  margin-left: 2px;
  font-size: 12px;
}

.ivu-menu-item-selected span.menu-item-name {
  color: #2d8cf0;
}
.ivu-menu-item {
  word-break: break-all;
}

ul.ivu-menu {
  text-align: left;
}
div.home-layout-content-menu {
  max-height: 800px;
}
.finish-icon {
  vertical-align: middle;
}
</style>

<template>
    <div class="menu-layout">
        <Menu ref="navMenu" theme="light" :active-name="routePath" @on-select="menuLink" width="100%" :open-names="openNames">
            <MenuItem name="/">首页
            </MenuItem>
            <MenuItem name="/basic-definition">基础定义
            </MenuItem>
            <MenuGroup v-for="(item,index) in menus" :key="item.title" :name="item.title" :title="item.title">
              <template slot="title">
                {{item.title}}
              </template>
              <template  v-for="subItem in item.subs">
                <template v-if="subItem.hasOwnProperty('subs')">
                  <template v-if="index == 0">
                    <MenuItem v-for="item2 in subItem.subs" :key="item2.title" :name="item2.url" ref="menuItem">
                      <span class="menu-item-name">{{item2.name}}</span>
                      <span class="menu-item-dot" v-if="item2.isnew==1"></span>
                    </MenuItem>
                  </template>
                  <template v-else>
                    <Submenu  :key="subItem.name" :name="subItem.name" :title="subItem.name" :opened="subItem.opened">
                      <template slot="title">
                        {{subItem.name}}
                      </template>
                      <MenuItem v-for="item2 in subItem.subs" :key="item2.title" :name="item2.url" ref="menuItem">
                        <i-circle :percent="item2.finish" :size="iconSize" class="finish-icon" v-if="item2.finish>0">
                          <Icon type="ios-checkmark" :size="iconSize" style="color:#5cb85c" v-if="item2.finish==100"></Icon>
                          <Icon type="ios-code" :size="iconSize" style="color:#5cb85c" v-else></Icon>
                        </i-circle>
                        <span class="menu-item-name">{{item2.name}}</span>
                        <span class="menu-item-dot" v-if="item2.isnew==1"></span>
                      </MenuItem>
                    </Submenu>
                  </template>
                </template>
                <template v-else>
                  <MenuItem :key="subItem.title" :name="subItem.url" ref="menuItem">
                    <span class="menu-item-name">{{subItem.name}}</span>
                    <span class="menu-item-dot" v-if="subItem.isnew==1"></span>
                  </MenuItem>
                </template>
              </template>
            </MenuGroup>
        </Menu>
    </div>
</template>

<script>
import $ from "jquery"
import { getMenu } from "@/api/doc";
export default {
  name: "NavMenu",
  data: function() {
    return {
      menus: [],
      routePath: "/",
      openNames: [],
      iconSize: 18
    };
  },
  created: function() {
    this.getData();
  },
  watch: {
    $route(to, from) {
      // 路由变化。
      this.$nextTick(function() {
        this.routePath = this.$route.path;
      });
    },
    openNames(before, after) {
      this.$nextTick(function() {
        this.$refs.navMenu.updateOpened();
      });
    }
  },
  methods: {
    menuLink: function(e) {
      this.$router.push(encodeURI(e));
      $('html,body').animate({ scrollTop: 0 }, 500);
    },
    searchapi(str) {
      if (str == "") {
        return [];
      }
      var result = [];
      for (var key in this.menus) {
        for (var key2 in this.menus[key]["subs"]) {
          var subMenu = this.menus[key]["subs"][key2];
          if (subMenu.hasOwnProperty("subs")) {
            for (var key3 in subMenu.subs) {
              var item = subMenu["subs"][key3];
              var titleArray = item.title.split(".");
              var lastTitle = titleArray[titleArray.length - 1];
              lastTitle = lastTitle.toLocaleUpperCase();
              str = str.toLocaleUpperCase();
              name = item.name;
              if (lastTitle.indexOf(str) != -1 || name.indexOf(str) != -1) {
                if (result.length > 10) {
                  //最多只展示十条
                  item = {
                    title: "...",
                    name: "..."
                  };
                  result.push(item);
                  return result;
                }
                result.push(item);
              }
            }
          }
        }
      }
      return result;
    },
    getData() {
      getMenu()
        .then(menus => {
          //重组数据,将group的进行分类.
          for (var i = 0; i < menus.length; i++) {
            var groupKey = {};
            var menuSubGroup = [];
            for (var j = 0; j < menus[i]["subs"].length; j++) {
              //一级菜单遍历
              if (
                menus[i]["subs"][j]["group"] !== undefined &&
                menus[i]["subs"][j]["group"] !== ""
              ) {
                //如果不为空表示它做了分组,我们需要对其进行分组处理.
                if (groupKey[menus[i]["subs"][j]["group"]] !== undefined) {
                  //有值的话直接push
                  menuSubGroup[groupKey[menus[i]["subs"][j]["group"]]][
                    "subs"
                  ].push(menus[i]["subs"][j]);
                } else {
                  groupKey[menus[i]["subs"][j]["group"]] = j;
                  menuSubGroup[
                    groupKey[menus[i]["subs"][j]["group"]]
                  ] = new Object();
                  menuSubGroup[groupKey[menus[i]["subs"][j]["group"]]][
                    "subs"
                  ] = new Array(menus[i]["subs"][j]);
                  menuSubGroup[groupKey[menus[i]["subs"][j]["group"]]]["name"] =
                    menus[i]["subs"][j]["group"];
                }
                delete menus[i]["subs"][j];
              }
            }
            if (menuSubGroup.length > 0) {
              for (var key in menuSubGroup) {
                menuSubGroup[key]["opened"] = false;
                menus[i]["subs"].push(menuSubGroup[key]);
              }
              menus[i]["subs"] = menus[i]["subs"].filter(function(item) {
                return item != undefined;
              });
            }
          }
          this.menus = menus;
          // 渲染好页面后更新当前活动的菜单
          this.$nextTick(function() {
            // 根据路由获取活动菜单
            this.routePath = this.$route.path;
            // this.$refs.navMenu.updateActiveName(this.$route.path);
          });
        })
        .catch(err => {
          this.menus = [];
          console.error(response);
          this.$Message.error({
            content:
              "获取数据失败！ " +
              err.response.status +
              ", " +
              err.response.statusText
          });
        });
    }
  },
  directives: {
    group: {
      inserted: function(el) {
        var dom = el.firstElementChild;
        if (dom != null && dom != undefined) {
          // dom.style.color = '#495060';
          dom.style.fontWeight = "bold";
          dom.style.paddingLeft = "15px";
        }
      }
    }
  }
};
</script>
