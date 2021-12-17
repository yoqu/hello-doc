package com.uyoqu.hello.docs.core.gen.resolver;

import com.uyoqu.hello.docs.core.vo.DataVO;

import java.lang.reflect.AnnotatedElement;
import java.util.List;

public interface Resolver {
  List<DataVO> resolve(AnnotatedElement element);
}
