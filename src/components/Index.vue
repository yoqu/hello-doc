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
                    <article class="ivu-article" v-show="documentBody==''">
                        无 <br>
                        ------- <br>
                        基础文档可以通过配置ApiInfo的basicDoc参数值地址进行展示项目基础文档
                    </article>
                </div>
            </Panel>
            <Panel name="2" v-show="timelines">
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
    import markedHelper from "../utils/render";
    import {getNotes, getTimeline} from "@/api/doc";

    export default {
        name: "Index",
        data: function () {
            return {
                timelines: this.getData(),
                collapse: ["1", "2"],
                documentBody: ""
            };
        },
        computed: {
            getMdBaseUrl() {
                return this.$store.state.basicInfo['basic']['docBaseUrl']
            },
            getBasic() {
                return this.$store.state.basicInfo;
            }
        },
        watch: {
            getBasic(v) {
                console.log(v)
            },
            documentBody: function () {
                let contents = document.getElementsByClassName("markdown-body");
                markedHelper.render(contents[0], this.documentBody);
            },
        },
        mounted() {
            let note = this.getBasic['basic']['basicDoc']
            console.log(note)
            if (note != undefined && note != "") {
                this.loadDoc(note)
            }
        },
        methods: {
            loadDoc(note) {
                console.log(note)
                getNotes(note, this.getMdBaseUrl)
                    .then(response => {
                        this.documentBody = response;
                    })
                    .catch(err => {
                        this.documentBody = "";
                        console.error(response);
                    });
            },
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
            },
            linkTo() {
                document.body.scrollTop = 0;
                document.documentElement.scrollTop = 0;
            }
        }
    };
</script>
