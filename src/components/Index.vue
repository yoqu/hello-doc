<style scoped>
.index-time {
  font-size: 14px;
  font-weight: bold;
}

.index-content {
  padding-left: 5px;
}

.index-link {
  margin-left: 5px;
}
code {
  display: inline-block;
  background: #f7f7f7;
  font-family: Consolas, Monaco, Andale Mono, Ubuntu Mono, monospace;
  margin: 0 3px;
  padding: 1px 5px;
  border-radius: 3px;
  color: #666;
  border: 1px solid #eee;
}
</style>

<template>
  <div>
     <Collapse v-model="collapse">
       <Panel name="1">
         文档信息
           <div slot="content">
             <article class="ivu-article" v-show="documentBody!=''">
               <div class="markdown-body">
               </div>
             </article>
           </div>
        </Panel>
        <Panel name="2">
          修定记录
            <div slot="content">
              <Timeline>
                <TimelineItem v-for="(item,index) in timelines" :key="item.time + item.content+index">
                  <p class="index-time">{{item.time}}</p>
                  <p class="index-content">{{item.content}}
                    <span class="index-link" @click="linkTo">
                      <router-link v-if="item.url!=null" :to="item.url">详情</router-link>
                    </span>
                  </p>
                </TimelineItem>
              </Timeline>
            </div>
        </Panel>
      </Collapse>
  </div>
</template>

<script>
import { dataToArray } from "../utils/data";
import markedHelper from "../utils/render";
import { getTimeline, getNotes } from "@/api/doc";
export default {
  name: "Index",
  data: function() {
    return {
      timelines: this.getData(),
      collapse: ["1", "2"],
      documentBody: ""
    };
  },
  watch: {
    documentBody: function() {
      let contents = document.getElementsByClassName("markdown-body");
      markedHelper.render(contents[0], this.documentBody);
    }
  },
  methods: {
    getData() {
      this.table_loading = true;
      getTimeline()
        .then(response => {
          this.timelines = response;
        })
        .catch(err => {
          this.$Message.error({
            content:
              "获取数据失败！ " +
              err.response.status +
              ", " +
              err.response.statusText
          });
        });
      getNotes("basic")
        .then(response => {
          this.documentBody = response;
        })
        .catch(err => {
          this.documentBody = "";
          console.error(response);
        });
    },
    linkTo() {
      document.body.scrollTop = 0;
      document.documentElement.scrollTop = 0;
    }
  }
};
</script>
