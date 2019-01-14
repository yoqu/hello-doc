<style scoped>
</style>

<template>
<div>
    <Alert show-icon>
        特殊约定
        <Icon type="ios-bulb-outline" slot="icon"></Icon>
        <template slot="desc">
            <ul>
                <li v-for="item in basic_tip">{{item}}</li>
            </ul>
        </template>
    </Alert>
    <Card style="margin-bottom: 10px;">
        <p slot="title">通用请求头</p>
        <Table :columns="columns" :data="header_base"></Table>
    </Card>

    <Card style="margin-bottom: 10px;">
        <p slot="title">响应包基础参数</p>
        <Table :columns="columns" :data="data_response"></Table>
    </Card>
    <Card style="margin-bottom: 10px;">
        <p slot="title">通用返回码</p>
        <Table :columns="code" :data="data_code" :loading="loading_code"></Table>
    </Card>
    <Card v-show="documentBody!=''">
        <p slot="title">说明</p>
        <div class="markdown-body">
        </div>
    </Card>
</div>
</template>

<script>
import {
    dataToArray
} from "../utils/data";
export default {
    name: "BasicDefinition",
    data: function () {
        return {
            columns: [{
                    title: "参数名称",
                    key: "name"
                },
                {
                    title: "类型",
                    key: "type"
                },
                {
                    title: "描述",
                    key: "desc"
                },
                {
                    title: "备注",
                    key: "remark"
                }
            ],
            code: [{
                    title: "返回码",
                    key: "code"
                },
                {
                    title: "说明",
                    key: "des"
                }
            ],
            documentBody: "",
            header_base: [],
            // data_post: this.getDataPost(),
            data_code: [],
            basic_tip: {},
            data_response: [],
            loading_post: false,
            loading_get: false,
            loading_code: false,
            loading_response: false
        };
    },
    mounted() {
        this.getBasic();
        this.getHeader();
        this.getDataCode();
        this.getResp();
    },
    methods: {
        getBasic() {
            this.basic_tip = this.$store.state.basicInfo.basic["basicTip"];
        },
        getHeader() {
            this.header_base = this.$store.state.basicInfo.basic.header;
        },
        getResp() {
            this.data_response = this.$store.state.basicInfo.basic.basicResp;
        },
        getDataCode() {
            this.data_code = this.$store.state.basicInfo.code;
        }
    }
};
</script>
