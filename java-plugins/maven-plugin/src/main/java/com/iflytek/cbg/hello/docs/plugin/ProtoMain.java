package com.iflytek.cbg.hello.docs.plugin;


import cn.hutool.core.util.ZipUtil;
import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import com.iflytek.cbg.hello.common.annotation.JProtobuf;
import com.iflytek.cbg.hello.docs.core.gen.ScanWrapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lanjian
 * 生成proto文件的工具类.
 */
public class ProtoMain {
    public static final Logger logger = LoggerFactory.getLogger(ProtoMain.class);

//    public static void main(String[] args) {
//        try {
//            String root = ProtoMain.class.getResource("/").getPath() + File.separator + "proto";
//            ScanWrapper scan = new ScanWrapper(new String[]{"com.iflytek.cbg.kuyin.movie.api.open.entity", "com.iflytek.cbg.kuyin.movie.common.vo"}, JProtobuf.class);
//            Set<Class<?>> cls = scan.getClassSet();
//            for (Class c : cls) {
//                String idl = ProtobufIDLGenerator.getIDL(c);
//                String proto = idl.replace("$$ByJProtobuf", "Protobuf");
//                //去除proto的内部类定义,采用import形式导入
//                String regexImport = "message ((?!" + c.getSimpleName() + ").*) \\{ *([^}]*\\n)*}";
//                Pattern patternImport = Pattern.compile(regexImport);
//                Matcher matcherImport = patternImport.matcher(proto);
//                proto = matcherImport.replaceAll("import \"$1.proto\";");
//                //指定proto的版本号
//                proto = "syntax = \"proto2\"; \n" + proto;
//                //更新包名,将其输出在同一package下
//                proto = proto.replaceFirst("package .*", "package com.iflytek.cbg.kuyin.movie.api.open.entity;");
//                proto = handler(proto);
//                proto = formatProto(proto);
//                FileUtils.writeStringToFile(new File(root + File.separator + c.getSimpleName() + ".proto"), proto, "UTF-8");
//            }
//            System.out.println("proto done");
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

    public static void generate(String[] packages, String destDirectory) {
        try {
            String root = destDirectory + File.separator + "static" + File.separator + "data";
            String protoPath = root + File.separator + "proto";
            ScanWrapper scan = new ScanWrapper(packages, JProtobuf.class);
            Set<Class<?>> cls = scan.getClassSet();
            for (Class c : cls) {
                JProtobuf protobuf = (JProtobuf) c.getAnnotation(JProtobuf.class);
                if (protobuf.skipGen()) {
                    logger.info("skip {} pb file.", c.getName());
                    continue;
                }
                String idl = ProtobufIDLGenerator.getIDL(c);
                String proto = idl.replace("$$ByJProtobuf", "Protobuf");
                //去除proto的内部类定义,采用import形式导入
                String regexImport = "message ((?!" + c.getSimpleName() + ").*) \\{ *([^}]*\\n)*}";
                Pattern patternImport = Pattern.compile(regexImport);
                Matcher matcherImport = patternImport.matcher(proto);
                proto = matcherImport.replaceAll("import \"$1.proto\";");
                //指定proto的版本号
                proto = "syntax = \"proto2\"; \n" + proto;
                //更新包名,将其输出在同一package下
                proto = proto.replaceFirst("package .*", "package com.iflytek.cbg.kuyin.movie.api.open.entity;");
                proto = handler(proto);
                proto = formatProto(proto);
                FileUtils.writeStringToFile(new File(protoPath + File.separator + c.getSimpleName() + ".proto"), proto, "UTF-8");
            }
            if(cls!=null && cls.size()>0){
                ZipUtil.zip(protoPath, root + File.separator + "proto.zip");
                System.out.println("proto done");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String handler(String str) {
        str = convertMap(str);
        return str;
    }

    public static String convertMap(String str) {
        return str.replaceFirst("optional map", "map");
    }

    /**
     * 支撑测试脚本需求，将import位置调整到message前
     *
     * @return
     */
    public static String formatProto(String proto) {
        if (proto.indexOf("import") == -1 || proto.indexOf("message") == -1) {
            return proto;
        }
        int indexOfImport = proto.indexOf("import");
        int indexOfMessage = proto.indexOf("message");
        if (indexOfImport < indexOfMessage) {
            return proto;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(proto.substring(0, indexOfMessage));
        stringBuilder.append(proto.substring(indexOfImport, proto.length()));
        stringBuilder.append(proto.substring(indexOfMessage, indexOfImport));
        return stringBuilder.toString();
    }
}
