package com.iflytek.cbg.hello.docs.plugin;

import com.alibaba.fastjson.JSON;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.iflytek.cbg.hello.docs.core.annotation.ApiBasicFiled;
import com.iflytek.cbg.hello.docs.core.annotation.ApiInDTO;
import com.iflytek.cbg.hello.docs.core.annotation.ApiOutDTO;
import com.iflytek.cbg.hello.docs.core.annotation.ApiServiceDocs;
import com.iflytek.cbg.hello.docs.core.gen.ScanWrapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author yoqu
 * @date 2018/5/10 - 19:09
 * 生成测试工具需要的请求接口工具
 */
public class InterfaceDataGen {

//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        List<Map<String, Object>> incs = new ArrayList<Map<String, Object>>();
//        ScanWrapper scan = new ScanWrapper(new String[]{"com.iflytek.cbg.kuyin.movie.api.open.apis"}, new Class[]{ApiServiceDocs.class});
//        Set<Class<?>> set = scan.getClassSet();
//        List<Class<?>> list = sortService(set);
//        for (Class service : list) {
//            Map<String, Object> s = resolveService(service);
//            incs.add(s);
//        }
//        File f = new File(InterfaceDataGen.class.getResource("/").getPath() + "/interface-config.js");
//        String prefix = "module.exports=";
//        String after = ";";
//        FileUtils.writeStringToFile(f, prefix + JSON.toJSONString(incs, true) + after);
//    }

    public static void generateInterface(String[] packages, String destPath) throws IOException, ClassNotFoundException {
        List<Map<String, Object>> incs = new ArrayList<Map<String, Object>>();
        ScanWrapper scan = new ScanWrapper(packages, new Class[]{ApiServiceDocs.class});
        Set<Class<?>> set = scan.getClassSet();
        List<Class<?>> list = sortService(set);
        for (Class service : list) {
            Map<String, Object> s = resolveService(service);
            incs.add(s);
        }
        File f = new File(destPath, File.separator + "static" + File.separator + "data" + File.separator + "interface-config.js");
        String prefix = "module.exports=";
        String after = ";";
        FileUtils.writeStringToFile(f, prefix + JSON.toJSONString(incs, true) + after);
    }

    public static List<Class<?>> sortService(Set<Class<?>> set) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        Map<String, List<Class>> groups = new HashMap<String, List<Class>>();
        for (Class<?> cl : set) {
            ApiServiceDocs apiServiceDocs = cl.getAnnotation(ApiServiceDocs.class);
            if (groups.containsKey(apiServiceDocs.group())) {
                groups.get(apiServiceDocs.group()).add(cl);
            } else {
                List<Class> classList = new ArrayList<Class>();
                classList.add(cl);
                groups.put(apiServiceDocs.group(), classList);
            }
        }
        Set<String> keys = groups.keySet();
        for (String key : keys) {
            for (Class c : groups.get(key)) {
                classes.add(c);
            }
        }
        return classes;
    }


    public static Map<String, Object> resolveService(Class service) {
        Map<String, Object> serviceMap = new HashMap<String, Object>();
        serviceMap.put("api", service.getName());
        ApiServiceDocs serviceDoc = (ApiServiceDocs) service.getAnnotation(ApiServiceDocs.class);
        serviceMap.put("desc", "(" + serviceDoc.group() + "-" + serviceDoc.cnName() + ")" + service.getName());
        serviceMap.put("id", service.getSimpleName());
        serviceMap.put("method", serviceDoc.methods()[0]);
        resolveIn(serviceMap, service);
        return serviceMap;
    }

    public static void resolveIn(Map serviceMap, Class service) {
        Map<String, String> proto = new HashMap<String, String>();
        ApiInDTO in = (ApiInDTO) service.getAnnotation(ApiInDTO.class);
        ApiOutDTO out = (ApiOutDTO) service.getAnnotation(ApiOutDTO.class);
        serviceMap.put("payload", resolveField(in.clazz()));
        proto.put("req", in.clazz().getSimpleName());
        proto.put("res", out.clazz().getSimpleName());
        serviceMap.put("proto", proto);
    }

    public static Map<String, Object> resolveField(Class request) {
        final Map<String, Object> values = new HashMap<String, Object>();
        ReflectionUtils.doWithFields(request, new ReflectionUtils.FieldCallback() {

            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ApiBasicFiled apiField = field.getAnnotation(ApiBasicFiled.class);
                Protobuf protobuf = field.getAnnotation(Protobuf.class);
                String name = field.getName();
                if (name.equals("page")) {
                    values.put(name, 1);
                    return;
                }
                if (name.equals("limit")) {
                    values.put(name, 20);
                    return;
                }
                if (protobuf.fieldType() == FieldType.OBJECT) {
                    Map<String, Object> result = resolveField(field.getType());
                    values.put(name, result);
                } else {
                    if (StringUtils.isBlank(apiField.example())) {
                        values.put(name, apiField.example());
                        return;
                    }
                    if (field.getType().isAssignableFrom(Integer.class)) {
                        values.put(name, Integer.valueOf(apiField.example()));
                    } else if (field.getType().isAssignableFrom(Long.class)) {
                        values.put(name, Long.valueOf(apiField.example()));
                    } else if (field.getType().isAssignableFrom(Double.class)) {
                        values.put(name, Double.valueOf(apiField.example()));
                    } else {
                        values.put(name, apiField.example());
                    }
                }
            }
        });
        return values;
    }

}
