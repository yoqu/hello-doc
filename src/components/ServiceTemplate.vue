<style>
.service-header {
  font-size: 16px;
  font-weight: bold;
}

.service-header-wide {
  padding: 10px 0;
}

.service-sub-header {
  font-size: 14px;
  margin: 0 20px;
}

.service-time {
  font-size: 14px;
  font-weight: bold;
}

.service-content {
  padding-left: 5px;
}
.md-content img {
  max-width: 100%;
}
.service-tag {
  margin-left: 10px;
}
.progress {
  display: inline-block;
  width: 20%;
  margin-left: 10px;
  vertical-align: top;
}
</style>

<template>
    <div>
        <h2 class="service-header">{{cnName}}</h2>
        <p class="service-sub-header service-header-wide">{{desc}}</p>
        <p class="service-sub-header">服务名称：{{serviceName}}  <Divider type="vertical" /> 
        <Button size="small" icon="ios-copy" type="success" shape="circle" class="copy-service">复制</Button></p>
        <p class="service-sub-header">版本号：{{version}}</p>
        <p class="service-sub-header">方法：{{method}}</p>
        <div class="service-sub-header" v-if="finish">开发进度: <div class="progress"> <Progress :percent="finish"></Progress></div> </div>
        <Card style="margin-top: 10px;">
            <p slot="title">请求数据包</p>
            <Table :columns="columns" :data="request_data" :loading="table_loading"></Table>
        </Card>
        <Card style="margin-top: 10px;">
            <p slot="title">响应数据包</p>
            <Table :columns="columns" :data="response_data" :loading="table_loading"></Table>
        </Card>
        <Card v-if="api_codes_data.length>0">
            <p slot="title">返回码</p>
            <Table :columns="retcodes_columns" :data="api_codes_data" :loading="table_loading"></Table>
        </Card>
        <Card v-show="documentBody!=''">
            <p slot="title">说明</p>
            <div class="md-content markdown-body">
            </div>
        </Card>
        
        <Card style="margin-top: 10px;">
            <p slot="title">修定记录</p>
            <Timeline>
                <TimelineItem v-for="item in timelines" :key="item.time + item.content">
                    <p class="service-time">{{item.time}}</p>
                    <p class="service-content">{{item.content}}</p>
                </TimelineItem>
            </Timeline>
        </Card>
    </div>
</template>

<script>
import { dataToField, dataToArray, checkUrl } from "../utils/data";
import markedHelper from "../utils/render";
import { getServices, getNotes } from "@/api/doc";
import Clipboard from "clipboard";
export default {
  name: "ServiceTemplate",
  data: function() {
    return {
      columns: [
        {
          title: "参数名称",
          key: "name"
        },
        {
          title: "类型",
          key: "type",
          render: (h, params) => {
            if (params.row.hasOwnProperty("link") && params.row.link !== "") {
              return h("div", [
                h(
                  "a",
                  {
                    props: {
                      type: "primary",
                      size: "small"
                    },
                    on: {
                      click: () => {
                        this.link(params.row.link);
                      }
                    }
                  },
                  params.row.type
                )
              ]);
            } else {
              return h("div", params.row.type);
            }
          }
        },
        {
          title: "描述",
          key: "desc"
        },
        {
          title: "是否必填",
          key: "required"
        },
        {
          title: "备注",
          key: "remark"
        }
      ],
      retcodes_columns: [
        {
          title: "返回码",
          key: "code"
        },
        {
          title: "返回描述",
          key: "des"
        }
      ],
      table_loading: false,
      request_data: [],
      response_data: [],
      api_codes_data: [],
      timelines: [],
      cnName: "",
      serviceName: "",
      desc: "",
      version: "",
      method: "",
      documentBody: "",
      finish: 0
    };
  },
  created: function() {
    this.getData();
  },
  mounted: function() {
    let self = this;
    let clipboard = new Clipboard(".copy-service", {
      text: function(trigger) {
        return self.serviceName;
      }
    });
    clipboard.on('success',function(e){
      self.$Message.success("复制成功");
    })
    clipboard.on('error',function(e){
      self.$Message.error('复制失败');
    })
  },
  watch: {
    $route(to, from) {
      // 路由变化重新获取数据。
      this.getData();
    },
    documentBody: function() {
      let contents = document.getElementsByClassName("markdown-body");
      markedHelper.render(contents[0], this.documentBody);
    }
  },
  methods: {
    copyService() {},
    link(link) {
      if (checkUrl(link)) {
        location.href = link;
      } else {
        this.$router.push("/dto/" + link);
      }
    },
    getDocuments(doc) {
      getNotes(doc)
        .then(response => {
          this.documentBody = response;
        })
        .catch(err => {
          this.documentBody = "";
          console.error(err);
        });
    },
    getData() {
      if (this.$store.state.service[this.$route.params.name] == null) {
        getServices().then(response => {
          let services = response;
          this.$store.commit("setService", services);
          let service = services[this.$route.params.name];
          this.cnName = dataToField(service.cnName);
          this.serviceName = dataToField(service.serviceName);
          this.desc = dataToField(service.desc);
          this.version = dataToField(service.version);
          this.method = dataToField(service.method);
          this.request_data = dataToArray(service.requests);
          this.response_data = dataToArray(service.responses);
          this.api_codes_data = dataToArray(service.apiCodes);
          this.timelines = dataToArray(service.timelines);
          this.finish = service.finish;
          if (
            service.doc != null &&
            service.doc != undefined &&
            service.doc != ""
          ) {
            this.getDocuments(service.doc);
          } else {
            this.documentBody = "";
          }
        });
      } else {
        console.log(this.$route.params.name);
        let service = this.$store.state.service[this.$route.params.name];
        this.cnName = dataToField(service.cnName);
        this.serviceName = dataToField(service.serviceName);
        this.desc = dataToField(service.desc);
        this.version = dataToField(service.version);
        this.method = dataToField(service.method);
        this.request_data = dataToArray(service.requests);
        this.response_data = dataToArray(service.responses);
        this.api_codes_data = dataToArray(service.apiCodes);
        this.timelines = dataToArray(service.timelines);
        this.finish = service.finish;
        if (
          service.doc != null &&
          service.doc != undefined &&
          service.doc != ""
        ) {
          this.getDocuments(service.doc);
        } else {
          this.documentBody = "";
        }
      }
    }
  }
};
</script>
