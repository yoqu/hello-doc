package com.uyoqu.hello.docs.core.gen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.uyoqu.hello.docs.core.config.Header;
import com.uyoqu.hello.docs.core.config.Resp;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author: yoqu
 * @date: 2019-01-25
 * @email: yoqulin@qq.com
 **/
@Slf4j
@Data
@Builder
public class GenHelper {
  /**
   * scan service define package.
   */

  private String[] scanPackage;

  /**
   * appname
   */

  private String appName;

  /**
   * copyright
   */

  private String copyright = "Hello Doc";

  private String enName = "hello-doc";

  private String docDirectory;

  private String configFile;

  private Map<String, Object> configs;

  private Header[] headers;

  private String[] tips;

  private Resp[] resps;

  private File outputDirectory;

  /**
   * 执行api文档生成
   *
   * @param docDir     文档路径
   * @param configFile 配置文件路径
   * @param outputDir  生成文件的路径
   * @throws Exception 生成失败返回异常
   */
  public static void run(String docDir, String configFile, String outputDir) throws Exception {
    GenHelper helper = GenHelper.builder().build();
    helper.setDocDirectory(docDir);
    helper.setConfigFile(configFile);
    helper.setOutputDirectory(new File(outputDir));
    helper.overrideConfig();
    helper.generate();
  }

  /**
   * 执行api文档生成
   *
   * @param docDir    文档路径
   * @param outputDir 生成文件的路径
   * @param builder   Builder工厂，用户直接调用api初始化文档配置
   * @throws Exception 生成失败返回异常
   */
  public static void run(String docDir, String outputDir, GenHelper.GenHelperBuilder builder) throws Exception {
    run(docDir, outputDir, null, builder);
  }

  /**
   * 执行api文档生成
   *
   * @param docDir     文档路径
   * @param outputDir  生成文件的路径
   * @param configFile 配置文件路径
   * @param builder    Builder工厂，用户直接调用api初始化文档配置
   * @throws Exception 生成失败返回异常
   */
  public static void run(String docDir, String outputDir, String configFile, GenHelper.GenHelperBuilder builder) throws Exception {
    GenHelper helper = builder
      .docDirectory(docDir)
      .outputDirectory(new File(outputDir))
      .configFile(configFile)
      .build();
    helper.overrideConfig();
    helper.generate();
  }


  private void generate() throws Exception {
    String destDirectory = outputDirectory.getPath() + "/api-doc";
    Gen gen = new FileGen(destDirectory);
    gen.scanPackages(scanPackage);
    gen.init(getAppInfo());
//    gen.setConfigs(configs);
    gen.handler();
    //如果包含文档目录,也要复制到指定目录下
    if (!StringUtils.isEmpty(docDirectory)) {
      copyDoc(destDirectory);
    }
  }

  /**
   * 复制静态资源
   *
   * @param destDirectory
   * @throws IOException
   */
  private void copyStaticData(String destDirectory) throws IOException {
    CodeSource src = this.getClass().getProtectionDomain().getCodeSource();
    if (src != null) {
      URL jar = src.getLocation();
      ZipInputStream zip = new ZipInputStream(jar.openStream());
      while (true) {
        ZipEntry e = zip.getNextEntry();
        if (e == null)
          break;
        String name = e.getName();
        if (!name.startsWith("static")) {
          continue;
        }
        if (e.isDirectory()) {
          name = name.replaceFirst("static/", "");
          FileUtils.forceMkdir(new File(destDirectory + File.separator + name));
        } else {
          InputStream stream = this.getClass().getResourceAsStream(File.separator + name);
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          for (int b = stream.read(); b >= 0; b = stream.read()) {
            baos.write(b);
          }
          byte[] data = baos.toByteArray();
          name = name.replaceFirst("static/", "");
          FileUtils.writeByteArrayToFile(new File(destDirectory + File.separator + name), data);
        }
      }
    }
  }

  /**
   * 复制文档
   *
   * @param destDirectory
   * @throws IOException
   */
  private void copyDoc(String destDirectory) throws IOException {
    File fileDocDirectory = new File(docDirectory);
    File destDocDir = new File(destDirectory + File.separator + "docs");
    //如果目录不存在创建一个目录.
    if (!destDocDir.exists()) {
      FileUtils.forceMkdirParent(destDocDir);
    }
    if (fileDocDirectory.isDirectory()) {
      for (File f : fileDocDirectory.listFiles()) {
        if (f.isDirectory()) {
          FileUtils.copyDirectoryToDirectory(f, destDocDir);
        } else {
          FileUtils.copyFileToDirectory(f, destDocDir);
        }
      }
    } else {
      FileUtils.copyFileToDirectory(fileDocDirectory, destDocDir);
    }
  }

  /**
   * 覆盖默认配置
   */
  private void overrideConfig() {
    if (StringUtils.isEmpty(configFile)) {
      log.info("配置文件为空，无法使用配置");
      return;
    }
    Map<String, Object> map = readConfig();
    if (map != null && !map.isEmpty()) {
      if (map.containsKey("scanPackage")) {
        scanPackage = ((JSONArray) map.get("scanPackage")).toArray(new String[0]);
      }
      if (map.containsKey("copyright")) {
        copyright = (String) map.get("copyright");
      }
      if (map.containsKey("enName")) {
        enName = (String) map.get("enName");
      }
      if (map.containsKey("appName")) {
        appName = (String) map.get("appName");
      }
      if (map.containsKey("headers")) {
        JSONArray jsonArray = (JSONArray) map.get("headers");
        headers = new Header[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
          Header header = jsonArray.getObject(i, Header.class);
          headers[i] = header;
        }
      }
      if (map.containsKey("tips")) {
        tips = ((JSONArray) map.get("tips")).toArray(new String[0]);
      }
      if (map.containsKey("resps")) {
        JSONArray jsonArray = (JSONArray) map.get("resps");
        resps = new Resp[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
          resps[i] = jsonArray.getObject(i, Resp.class);
        }
      }
      configs = map;
    }
  }

  /**
   * 初始化appInfo信息
   *
   * @return
   */
  private ApiInfo getAppInfo() {
    ApiInfo.Builder builder = new ApiInfo.Builder()
      .name(appName)
      .copyright(copyright)
      .enName(enName)
      .buildTime(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"));
    if (headers != null) {
      for (Header header : headers) {
        builder.header(header.getName(), header.getType(), header.getDesc(), header.getRemark(), header.isRequired(), header.getExample());
      }
    }
    if (resps != null) {
      for (Resp resp : resps) {
        builder.response(resp.getName(), resp.getType(), resp.getDesc(), resp.getRemark(), resp.isRequired());
      }
    }
    if (tips != null) {
      for (String tip : tips) {
        builder.tip(tip);
      }
    }
    return builder.build();
  }

  /**
   * 读取配置文件
   *
   * @return
   */
  private Map<String, Object> readConfig() {
    try {
      String data = FileUtils.readFileToString(new File(configFile), "UTF-8");
      Map<String, Object> map = JSON.parseObject(data, Map.class);
      return map;
    } catch (Exception e) {
      log.error("解析配置文件失败，文件路径:{" + configFile + "}", e);
      return null;
    }
  }
}
