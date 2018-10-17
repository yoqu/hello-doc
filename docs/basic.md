# Hello doc

> API接口协议模板

## 快速启动

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

## 流程图demo

```flowchart
st=>start: Start
e=>end: End
接收用户名和密码=>operation: 接收用户名和密码
使用用户名查询数据库=>operation: 使用用户名查询数据库
数据库中是否有数据=>condition: 数据库中是否有数据?
走登录逻辑=>operation: 走登录逻辑
走注册逻辑=>operation: 走注册逻辑
密码是否正确=>condition: 密码是否正确?
把用户名和密码写入数据库=>inputoutput: 把用户名和密码写入数据库 
st->接收用户名和密码->使用用户名查询数据库->数据库中是否有数据
数据库中是否有数据(yes)->走登录逻辑->密码是否正确
数据库中是否有数据(no)->走注册逻辑->把用户名和密码写入数据库->e
密码是否正确(yes)->e
密码是否正确(no,down)->接收用户名和密码
```

```flowchart
    st=>start: Start|past:>http://www.google.com[blank]
    e=>end: Ende|future:>http://www.google.com
    op1=>operation: My Operation|past
    op2=>operation: Stuff|current
    sub1=>subroutine: My Subroutine|invalid
    cond=>condition: Yes
    or No?|approved:>http://www.google.com
    c2=>condition: Good idea|rejected
    io=>inputoutput: catch something...|future

    st->op1(right)->cond
    cond(yes, right)->c2
    cond(no)->sub1(left)->op1
    c2(yes)->io->e
    c2(no)->op2->e
```
