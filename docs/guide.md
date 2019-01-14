
## 快速使用

### 1.设置pom

pom.xml文件的`plugins`下引入以下配置

```xml
 <plugin>
                <groupId>com.uyoqu</groupId>
                <artifactId>hello-docs-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <configuration>
                    <docDirectory>${project.basedir}/docs/api-docs</docDirectory>
                    <scanPackage>
                        <package>com.xx.api</package>
                        <package>com.xx.vo</package>
                    </scanPackage>
                    <appName>案例项目</appName>
                    <copyright>demo</copyright>
                    <headers>
                        
                        <header>
                            <name>X-Ca-Version</name>
                            <desc>接口版本号</desc>
                            <required>true</required>
                        </header>
                        <header>
                            <name>X-Ca-Api</name>
                            <desc>接口服务名</desc>
                            <required>true</required>
                        </header>
                        <header>
                            <name>X-Ca-Timestamp</name>
                            <desc>时间戳，单位毫秒</desc>
                            <required>true</required>
                        </header>                       
                        <header>
                            <name>Content-Type</name>
                            <desc>application/octet-stream</desc>
                            <remark>请求文本类型,二进制数据流</remark>
                            <required>true</required>
                        </header>
                        <header>
                            <name>X-Ca-Signature</name>
                            <remark>签名</remark>
                            <required>true</required>
                        </header>
                    </headers>
                    <resps>
                        <resp>
                            <name>code</name>
                            <desc>返回编码</desc>
                        </resp>
                        <resp>
                            <name>msg</name>
                            <desc>返回消息</desc>
                        </resp>
                    </resps>
                    <tips>
                        <tip>请求头必须加上application/json</tip>
                        <tip>数据格式:JSON</tip>
                    </tips>
                </configuration>
            </plugin>
```

### 2. 配置解释

`scanPackage`为必填项，需要指定扫描接口的包名，其他均为非必填项目

`appName`为项目名称

`docDirectory` 如果接口有指定api文档，那么将markdown的文档需要放到这个配置项对应的目录

`headers`此项为基础约定的header参数

`resps`为统一返回数据格式

`tips`为基础提示


### 3. java类添加注解

1. 在接口类中添加以下注解

```java
@RestController
@RequestMapping("/")
public class HelloController {

    @Autowired
    IUserSvc userSvc;
    
    @GetMapping("/")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiServiceDocs(cnName = "首页", group = "缓存模块", desc = "首页获取用户列表", finish = 100, version = "1.0")
    @ApiOut(@Out(param = "List<User>", link = "User", desc = "用户列表", type = "User"))
    public List<User> index() {
        return userSvc.findAll();
    }

    @GetMapping("/{id}")
    @ApiServiceDocs(cnName = "用户详情", group = "缓存模块", desc = "用户详情", finish = 100, version = "1.0")
    public User detail(@In(remark = "这是一个备注", desc = "主键id") @PathVariable("id") String id) {
        return userSvc.findById(id);
    }

    @GetMapping("test")
    @ApiServiceDocs(cnName = "测试", group = "缓存模块", desc = "test", finish = 50, version = "1.0")
    @ApiOut(@Out(param = "List<User>", link = "User", desc = "用户列表", type = "User"))
    @ApiTimeline(@Timeline(time = "2018-10-18", content = "first test"))
    public List<User> test(User user) {
        return Collections.emptyList();
    }
}

```

`@ApiServiceDocs`为描述接口的信息，可以使用在方法级别或类级别
`@ApiInDTO`和`@ApiOutDTO`为请求和相应实体

2. 对象参数
`@ApiInDTO`响应实体:
```java
@ApiDTO(cnName = "用户", enName = "User", desc = "用户信息")
public class User implements Serializable {

    @ApiBasicFiled(desc = "名字")
    private String name;

    @ApiBasicFiled(desc = "id")
    private String id;

    @ApiBasicFiled(desc = "年龄")
    private int age;

    @ApiBasicFiled(desc = "区域")
    private String area;
}
```
通过对字段使用`@ApiBasicField`进行字段注解，描述参数信息

3. 全局返回码

`@ApiGlobalCode`标记返回码
```java
@ApiGlobalCode
public class ApiRetCodes {

    /**
     * 00xx - 成功
     */
    public static final ApiRet SUCCESS = new ApiRet("0000", "成功");

    /**
     * 9xxx - 异常
     */
    public static final ApiRet FAIL = new ApiRet("9999", "系统异常");
}
public class ApiRet implements Serializable {
    private String code;
    private String msg;
}
```
`@ApiGlobalCode`注解内容定义的是`ApiRet`字段变量名字，默认为code和msg


### 4.运行插件

在项目目录下执行命令`mvn clean compile hello-docs:doc`

执行完成后在`target/api-doc`目录下生成了html，打开即可。


### 5. spring mvc demo

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
