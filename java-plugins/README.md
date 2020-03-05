# 发布中央仓库步骤

1. run `mvn clean package -P release deploy`

2. 在`oss.sonatype.org`中进行对包进行审核

# 更新maven版本号方法

`mvn versions:set -DnewVersion=1.0.5-RELEASE`

