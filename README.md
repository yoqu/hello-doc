
# Hello doc


> Hello doc是一款快译通通过代码生成漂亮的api在线文档工具，支持java快速生成接口文档

运行demo：[demo](https://yoqu.gitee.io/hello-doc-demo)

## 文档

Link： [详细文档](https://yoqu.gitee.io/hello-doc/#/guide)


## 特性
* 支持markdown（包含flowchat流程图）
* 参数实体跳转
* java注解支持
* 菜单快速筛选
* 接口搜索
* 接口统计

## 待实现功能
- [ ] mock接口
- [ ] swagger配置文件转换为hello doc
- [ ] markdown功能增强时序图等功能
- [ ] 文档工具国际化支持
- [ ] 文档工具接入在线评论支持评论功能
- [ ] 文档导出为pdf和word格式



## 截图展示：

![demo3](docs/images/demo3.png)

![demo1](docs/images/demo1.png)


## 更新日志

-- 2.0.0-SNAPSHOT
* 重构后端生成器，代码更简介和符合命名规范
* 修复同名dto或接口导致菜单及跳转异常的bug
* 增加通用request参数
* request入参新增传参方式字段
* 修复前端搜索服务不是以路径方式寻址的bug
* 增加request入参树形结构支持
* 升级至iview 4.x最新版本
* 优化菜单跳转视觉效果



-- 1.0.3.1-SNAPSHOT

* 增加web header参数支持
* 修复复制service重复提示的问题
* 增加md文档配置
* 升级前端框架至最新版本，解决搜索框联想重复问题
* 优化doc runner的配置方式，让配置更简洁

-- 1.0.3-SNAPSHOT

* 允许ApiDto中的enName属性为空，自动获取注解类的属性
* 优化在线文档的日志消息
* 优化在spring mvc模式下注解默认参数可允许为空

-- 1.0.2

* 修复spring mvc 运行丢失静态页面资源的bug
* 增加spring mvc 默认路径访问功能

-- 1.0.1

* 修复windows下字符编码的bug
* 增加spring mvc运行时生成文档支持
* 修复spring mvc下菜单渲染的bug
