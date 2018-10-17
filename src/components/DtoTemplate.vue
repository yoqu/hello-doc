<style scoped>
.dto-header {
  font-size: 16px;
  font-weight: bold;
}

.dto-sub-header {
  font-size: 14px;
  margin: 10px 20px;
}

.dto-time {
  font-size: 14px;
  font-weight: bold;
}

.dto-content {
  padding-left: 5px;
}
.md-content img {
  max-width: 100%;
}
</style>

<template>
    <div>
        <p class="dto-header">{{cnName}} ({{enName}})</p>
        <p class="dto-sub-header">{{desc}}</p>
        <Table :columns="columns" :data="table_data" :loading="table_loading" style="margin-top: 10px;"></Table>
        <Card v-if="documentBody!=''">
            <p slot="title">说明</p>
            <div v-html="complieDocument" class="md-content markdown-body">
            </div>
        </Card>
        <Card style="margin-top: 10px;">
            <p slot="title">修定记录</p>
            <Timeline>
                <TimelineItem v-for="item in timelines" :key="item.time + item.content">
                    <p class="dto-time">{{item.time}}</p>
                    <p class="dto-content">{{item.content}}</p>
                </TimelineItem>
            </Timeline>
        </Card>
    </div>
</template>

<script>
import { dataToField, dataToArray, checkUrl } from "../utils/data";
import markedHelper from "../utils/render";
import { getDtos } from "@/api/doc";
export default {
  name: "DtoTemplate",
  watch: {
    documentBody: function() {
      let contents = document.getElementsByClassName("markdown-body");
      markedHelper.render(contents[0], this.documentBody);
    }
  },
  data: function() {
    return {
      columns: [
        {
          title: "字段名称",
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
        // {
        //     title: '是否必填',
        //     key: 'required'
        // },
        {
          title: "描述",
          key: "desc"
        },
        {
          title: "备注",
          key: "remark"
        }
      ],
      table_loading: false,
      table_data: [],
      timelines: [],
      cnName: "",
      enName: "",
      desc: "",
      documentBody: ""
    };
  },
  created: function() {
    this.getData();
  },
  watch: {
    $route(to, from) {
      // 路由变化重新获取数据。
      this.getData();
    }
  },
  methods: {
    link(link) {
      if (checkUrl(link)) {
        location.href = link;
      } else {
        this.$router.push("/dto/" + link);
      }
    },
    getDocuments(doc) {
      let url =
        this.$store.state.config["baseUrl"] +
        "/docs/" +
        doc +
        ".md" +
        "?r=" +
        new Date().getTime();
      this.$http.get(url).then(
        response => {
          this.loading_post = false;
          this.documentBody = response.body;
        },
        response => {
          this.documentBody = "";
          console.error(response);
          // this.$Message.error({ content: "获取数据失败！ " + response.status + ", " + response.statusText });
        }
      );
    },
    getData() {
      if (this.$store.state.dto[this.$route.params.name] == null) {
        getDtos().then(response => {
          let dtos = response;
          this.$store.commit('setDto',dtos);
          let dto = dtos[this.$route.params.name];
          this.cnName = dataToField(dto.cnName);
          this.enName = dataToField(dto.enName);
          this.desc = dataToField(dto.desc);
          this.table_data = dataToArray(dto.data);
          this.timelines = dataToArray(dto.timelines);
        });
      } else {
        console.log(this.$route.params.name);
        let dto = this.$store.state.dto[this.$route.params.name];
        this.cnName = dataToField(dto.cnName);
        this.enName = dataToField(dto.enName);
        this.desc = dataToField(dto.desc);
        this.table_data = dataToArray(dto.data);
        this.timelines = dataToArray(dto.timelines);
      }     
    }
  }
};
</script>
