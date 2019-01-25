package com.uyoqu.hello.docs.core.gen;

import java.io.IOException;

/**
 * @author: yoqu
 * @date: 2018/10/18
 * @email: yoqulin@qq.com
 **/
public interface TopGen {
  void scanPakcages(String... packageName);

   void init(ApiInfo basicInfo);

  void handler() throws GenException, IOException;
}
