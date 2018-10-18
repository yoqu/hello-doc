package com.uyoqu.hello.docs.core.gen;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: yoqu
 * @date: 2018/10/18
 * @email: yoqulin@qq.com
 **/
public class FileGen extends AbstractScanGen {
  protected static final String DOC_ROOT = "./docs";

  public String getDocRoot() {
    return StringUtils.isNotBlank(docRoot) ? docRoot : DOC_ROOT;
  }

  public void setDocRoot(String docRoot) {
    this.docRoot = docRoot;
  }


  // 拷贝模板
  public void copyTemplate() throws IOException {
    FileUtils.forceMkdir(new File(getDocRoot()));
    FileUtils.deleteDirectory(new File(getDocRoot() + "/static/data"));
    FileUtils.forceMkdir(new File(getDocRoot() + "/static/data"));
  }

  private void writeFile(String path, String filename, String content) throws IOException {
    FileUtils.forceMkdir(new File(path));
    File file = new File(path + "/" + filename);
    file.delete();
    FileWriter writer = null;
    try {
      writer = new FileWriter(file, false);
      writer.write(content);
      writer.flush();
    } catch (IOException e) {
      try {
        if (writer != null)
          writer.close();
      } catch (Throwable ignored) {

      }
      throw e;
    }
  }


  public void write() {
    // 拷贝模板
    try {
      copyTemplate();
      // 写菜单
      writeFile(getDocRoot() + "/static/data", "nav_menu.json", JSON.toJSONString(menuList, true));
      // 写时间轴
      writeFile(getDocRoot() + "/static/data", "timelines.json", JSON.toJSONString(timelineList, true));
      if (basicInfo != null) {//基础信息
        basicMap.put("basic", basicInfo);
      }
      // 写基础定义
      writeFile(getDocRoot() + "/static/data/", "basic_definition.json", JSON.toJSONString(basicMap, true));
      // 写数据结构
      writeFile(getDocRoot() + "/static/data", "dto.json", JSON.toJSONString(dtoMap, true));
      // 写接口服务
      writeFile(getDocRoot() + "/static/data", "service.json", JSON.toJSONString(serviceMap, true));
    } catch (IOException e) {
      e.printStackTrace();
      log.error("写入文件错误");
    }
  }
}
