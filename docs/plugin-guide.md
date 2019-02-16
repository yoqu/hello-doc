
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


> **注意**：生成的html文件打开不能使用本地文件file打开，文档的路由功能无法正常使用。推荐放置`tomcat`或`nginx`下带域名或ip形式访问
