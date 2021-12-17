package com.uyoqu.hello.docs.core.gen;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;

/**
 * @author: yoqu
 * @date: 2018/10/18
 * @email: yoqulin@qq.com
 **/
public class FileGenerate extends ScanGenerate {

  protected static final String DOC_ROOT = "./docs";
  protected String destDir;

  public FileGenerate(String destDir) {
    this.destDir = destDir;
  }

  @Override
  protected void check() {
    super.check();
    Assert.notNull(destDir, "docRot目录");
  }

  public String getDestDir() {
    return StringUtils.isNotBlank(destDir) ? destDir : DOC_ROOT;
  }

  public void setDestDir(String destDir) {
    this.destDir = destDir;
  }


  // 拷贝模板
  public void copyTemplate() throws IOException {
    FileUtils.forceMkdir(new File(getDestDir()));
    FileUtils.deleteDirectory(new File(getDestDir() + "/static/data"));
    FileUtils.forceMkdir(new File(getDestDir() + "/static/data"));
  }

  private void writeFile(String path, String filename, String content) throws IOException {
    FileUtils.forceMkdir(new File(path));
    File file = new File(path + "/" + filename);
    FileUtils.writeStringToFile(file, content, "UTF-8");
  }

  @Override
  public void handler() throws GenException {
    super.handler();
    //写入文件
    write();
  }

  public void write() {
    // 拷贝模板
    try {
      copyTemplate();
      // 写菜单
      writeFile(getDestDir() + "/static/data", "nav_menu.json", JSON.toJSONString(menuList, true));
      // 写时间轴
      writeFile(getDestDir() + "/static/data", "timelines.json", JSON.toJSONString(timelineList, true));
      // 写基础定义
      writeFile(getDestDir() + "/static/data/", "basic_definition.json", JSON.toJSONString(basicMap, true));
      // 写数据结构
      writeFile(getDestDir() + "/static/data", "dto.json", JSON.toJSONString(dtoMap, true));
      // 写接口服务
      writeFile(getDestDir() + "/static/data", "service.json", JSON.toJSONString(serviceMap, true));
    } catch (IOException e) {
      e.printStackTrace();
      log.error("写入文件错误");
    }
  }
}
