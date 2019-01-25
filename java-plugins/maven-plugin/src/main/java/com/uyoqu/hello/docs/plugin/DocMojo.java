package com.uyoqu.hello.docs.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import cn.hutool.core.date.DateUtil;
import com.uyoqu.hello.docs.core.gen.ApiInfo;
import com.uyoqu.hello.docs.core.gen.FileGen;
import com.uyoqu.hello.docs.core.gen.TopGen;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Mojo(name = "doc", defaultPhase = LifecyclePhase.COMPILE, configurator = "include-project-dependencies",
  requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, threadSafe = true)
public class DocMojo extends AbstractMojo {

  /**
   * output dir
   */
  @Parameter(property = "project.build.directory")
  private File outputDirectory;

  /**
   * skip hello doc generation method.
   */
  @Parameter(property = "hello.doc.skip", defaultValue = "false")
  private boolean skipGeneration;

  /**
   * scan service define package.
   */
  @Parameter(property = "hello.doc.package")
  private String[] scanPackage;

  /**
   * appname
   */
  @Parameter(property = "hello.doc.name", defaultValue = "Hello Doc")
  private String appName;

  /**
   * copyright
   */
  @Parameter(property = "hello.doc.copyright", defaultValue = "hello.doc")
  private String copyright;

  @Parameter(property = "hello.doc.docs.path", defaultValue = "")
  private String docDirectory;

  @Parameter
  private Header[] headers;

  @Parameter
  private String[] tips;

  @Parameter
  private Resp[] resps;


  public String[] getTips() {
    return tips;
  }

  public void setTips(String[] tips) {
    this.tips = tips;
  }

  public Resp[] getResps() {
    return resps;
  }

  public void setResps(Resp[] resps) {
    this.resps = resps;
  }

  public Header[] getHeaders() {
    return headers;
  }

  public void setHeaders(Header[] headers) {
    this.headers = headers;
  }

  public void execute()
    throws MojoExecutionException {
    try {
      if (skipGeneration) {
        getLog().info("Hello Doc generation is skipped");
        return;
      }
      TopGen gen = new FileGen();
      String destDirectory = outputDirectory.getPath() + "/api-doc";
      gen.init(getAppInfo(), destDirectory);
      gen.scanPakcages(scanPackage);
      gen.handler();
      //静态资源文件copy
      copyStaticData(destDirectory);
      //如果包含文档目录,也要复制到指定目录下
      if (!StringUtils.isEmpty(docDirectory)) {
        copyDoc(destDirectory);
      }
    } catch (Throwable e) {
      throw new MojoExecutionException("生成接口文档失败", e);
    }
  }

  private ApiInfo getAppInfo() {
    ApiInfo.Builder builder = new ApiInfo.Builder()
      .name(appName)
      .copyright(copyright)
      .enName(copyright)
      .buildTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm"));
    if (headers != null) {
      for (Header header : headers) {
        builder.header(header.getName(), header.getType(), header.getDesc(), header.getRemark(), header.isRequired());
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

  private void copyStaticData(String destDirectory) throws IOException {
    CodeSource src = DocMojo.class.getProtectionDomain().getCodeSource();
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
          InputStream stream = this.getClass().getResourceAsStream("/" + name);
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

}
