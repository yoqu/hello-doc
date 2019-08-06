
使用前提：使用运行时生成文档必须本项目要能够运行依赖`web`运行环境或

# 1.设置pom

pom.xml文件的追加依赖

```xml
        <dependency>
            <groupId>com.uyoqu</groupId>
            <artifactId>docs-runner</artifactId>
            <version>1.0.3-SNAPSHOT</version>
        </dependency>
```
# 2. 新建java的spring配置文件

```java
@Configuration
@EnableHelloDocConfig//1
public class HelloDocConfiguration {

    @Bean
    public DocConfigEntity configEntity(DocConfig docConfig) {//2
        return DocConfigEntity.build(docConfig);
    }

    @Bean
    public DocConfig docConfig() {//3
        return DocConfig.builder()
                .apiInfo(new ApiInfo.Builder()
                        .copyright("Hello Web")
                        .enName("HelloWeb")
                        .name("Hello Web管理系统快速开发框架")
                        .build())
                .scanPackage(new String[]{"com.hello.support.fast.module"})
                .build();
    }
}
```
需要注意以上标注的三点。
必须追加`@EnableHelloDocConfig`注解让spring启动时能够运行时生成HelloDoc。
必须添加`DocConfig`和`DocConfigEntity`到spring容器中，其中DocConfig为对文档的配置，通过链式操作来定义文档。

> **注意**：如果项目有使用了`shiro`或`spring security`等第三方安全认证框架，需要将`/hello-docs/**`路径排除认证.
例如shiro:
```java
 filterMap.put("/hello-docs/**", "anon");
```

# 3.运行项目

直接运行集成本模块的项目，打开浏览器，输入`server:port/hello-docs/index.html`即可查看文档


# 4. 配置解释

`scanPackage`为必填项，需要指定扫描接口的包名，其他均为非必填项目

`appName`为项目名称

`docDirectory` 如果接口有指定api文档，那么将markdown的文档需要放到这个配置项对应的目录

`headers`此项为基础约定的header参数

`resps`为统一返回数据格式

`tips`为基础提示


# 5. java类添加注解

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
