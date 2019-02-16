
## 快速使用

插件运行方式有两种：

1. 运行时生成文档类似swagger-ui一样的运行模式

2. 依赖maven插件，编译离线生成文档

本文推荐第一种swagger-ui方式生成文档

文档链接：
* [maven插件使用文档](./plugin-guide.md)
* [运行时生成文档](./runner-guide.md)

### spring mvc demo

demo地址：[spring-mvc](https://github.com/yoqu/spring-cache-demo)


## 字段定义说明

### 注解

| 注解 | 描述 |
| ---- | ---- |
| ApiBasicFiled | 描述请求参数和响应参数的字段描述，需配合@ApiInDTO或@ApiOutDTO使用 |
| ApiCode       | 接口返回码，用于接口类使用，外层必须嵌套`ApiCodes`注解       |
| ApiCodes      | 接口返回码集合，用于接口类使用                               |
| ApiDTO        | 对象实体                                                     |
| ApiGlobalCode | 全局返回码，用于描述整个系统的返回码的类上 |
| ApiIn | 接口入参需要传入`In`数组，和`ApiInDTO`不能同时使用，同时使用优先使用`ApiInDTO` |
| In | 单个入参，描述参数的类型，描述等。 |
| ApiOut | 接口单个出参需要传入`Out`数组，和`ApiOutDTO`不能同时使用，同时使用优先使用`ApiOutDTO` |
| Out | 单个出参，描述参数的类型，描述等。 |
| ApiServiceDocs | 接口服务描述，描述接口地址，简介，mardown文档地址等信息 |
| ApiTimeline | 接口或实体更新日志,参数为`Timeline`数组 |
| Timeline | 描述更新日志 |


## FAQ

Q: 多组件的项目可以生成文档吗？

A: 可以，只需要添加该插件的模块在maven依赖有的，就能够支持扫描生成文档，但是需要**注意**的是，使用maven生成需要将其他模块包`install`后再生成

Q: 生成的文档中没有新追加的接口？

A： 可能的原因是依赖的jar包没有更新或当前项目没有compile导致的。


## 项目源码构建

### 前端构建

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report
```

### 打包java-plugin

1. 执行java-plugins目录下的`build-html.sh`脚本打包前端工程至插件的resource目录下

2. 执行mvn install 将依赖安装到本地仓库
