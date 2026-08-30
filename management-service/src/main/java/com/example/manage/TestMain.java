//package com.example.manage;
//
//
//import com.alibaba.fastjson.JSON;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.*;
//import java.text.Format;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class TestMain {
//    public static IvParameterSpec a;
//
//    public static SecretKeySpec b;
//
//    public static Cipher c;
//
//    public static StringBuilder p(String paramString1, String paramString2) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(paramString1);
//        stringBuilder.append(paramString2);
//        return stringBuilder;
//    }
//
//    public static StringBuilder o(String paramString) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(paramString);
//        return stringBuilder;
//    }
//
//    public static String a(byte[] paramArrayOfByte) {
//        if (paramArrayOfByte == null)
//            return null;
//        int i = paramArrayOfByte.length;
//        byte b1 = 0;
//        String str = "";
//        while (b1 < i) {
//            byte b2;
//            StringBuilder stringBuilder = null;
//            if ((paramArrayOfByte[b1] & 0xFF) < 16) {
//                stringBuilder = p(str, "0");
//                b2 = paramArrayOfByte[b1];
//            } else {
//                stringBuilder = o(stringBuilder.toString());
//                b2 = paramArrayOfByte[b1];
//            }
//            stringBuilder.append(Integer.toHexString(b2 & 0xFF));
//            str = stringBuilder.toString();
//            b1++;
//        }
//        return str;
//    }
//
//    public static String b(String str) {
//        if (str == null || str.length() == 0) {
//            return "数据=null";
//        }
//
//        try {
//            if (c == null) {
//                c = Cipher.getInstance("AES/CBC/NoPadding");
//                a = new IvParameterSpec("9292929292929292".getBytes());
//                b = new SecretKeySpec("1234567812345678".getBytes(), "AES");
//            }
//            c.init(2, b, a);
//            Cipher cipher = c;
//            byte[] bArr = null;
//            if (str.length() >= 2) {
//                int length = str.length() / 2;
//                bArr = new byte[length];
//                for (int i = 0; i < length; i++) {
//                    int i2 = i * 2;
//                    bArr[i] = (byte) ((byte) Integer.parseInt(str.substring(i2, i2 + 2), 16));
//                }
//            }
//            return new String(cipher.doFinal(bArr));
//        } catch (Exception e) {
//            return "解密出错";
//        }
//    }
//
//    static class RuleBean {
//        private String packageName;
//        private String description;
//        private String path;
//
//        public RuleBean(String packageName, String path) {
//            this.packageName = packageName;
//            this.path = path;
//        }
//
//        public RuleBean(String packageName, String path, String description) {
//            this.packageName = packageName;
//            this.description = description;
//            this.path = path;
//        }
//
//        public String getPackageName() {
//            return packageName;
//        }
//
//        public void setPackageName(String packageName) {
//            this.packageName = packageName;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//        public String getPath() {
//            return path;
//        }
//
//        public void setPath(String path) {
//            this.path = path;
//        }
//    }
//
//    public static Map<String, List<RuleBean>> map = new HashMap<>();
//
//    public static void main(String[] args) {
//        try {
//            File file = new File("info.txt");
//            String encoding = "UTF-8";
//            if (file.isFile() && file.exists()) { //判断文件是否存在
//                InputStreamReader read = new InputStreamReader(
//                        new FileInputStream(file), encoding);//考虑到编码格式
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt = null;
//                StringBuilder str = new StringBuilder();
//                while ((lineTxt = bufferedReader.readLine()) != null) {
////                    System.out.println(lineTxt);
//                    str.append(lineTxt);
//                }
//                read.close();
//                String b = b(str.toString());
//                String[] split = b.split("\\|");
//                for (String s : split) {
//                    if (!s.contains("Android/data")) {
//                        String description = v(s, "<description>", "<description>");
//                        String name = v(s, "<packetName>", "<packetName>");
//                        String path = v(s, "<path>", "<path>");
////                        System.out.println("description = "+description+", name = "+name+", path = "+path);
////                        System.out.println(java.lang.String.format("RuleBean ruleBean = new RuleBean(\"%s\",\"%s\");", name, path));
////                        System.out.println(String.format("ruleBeans.add(new RuleBean(\"%s\", \"%s\", \"%s\"));", name, path,description));
//                        if (description.contains("广告")) {
//                            if (map.containsKey("Ad junks")) {
//                                List<RuleBean> list = map.get("Ad junks");
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Ad junks", list);
//                            } else {
//                                List<RuleBean> list = new ArrayList<>();
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Ad junks", list);
//                            }
//                        } else if (description.contains("缩略图")) {
//                            if (map.containsKey("Thumb junks")) {
//                                List<RuleBean> list = map.get("Thumb junks");
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Thumb junks", list);
//                            } else {
//                                List<RuleBean> list = new ArrayList<>();
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Thumb junks", list);
//                            }
//                        } else if (description.contains("临时") || description.contains("日志")) {
//                            if (map.containsKey("Temp files & logs")) {
//                                List<RuleBean> list = map.get("Temp files & logs");
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Temp files & logs", list);
//                            } else {
//                                List<RuleBean> list = new ArrayList<>();
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Temp files & logs", list);
//                            }
//                        } else if (description.contains("缓存")) {
//                            if (map.containsKey("Cache junks")) {
//                                List<RuleBean> list = map.get("Cache junks");
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Cache junks", list);
//                            } else {
//                                List<RuleBean> list = new ArrayList<>();
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Cache junks", list);
//                            }
//                        } else if (description.contains("垃圾")) {
//                            if (map.containsKey("Residual junks")) {
//                                List<RuleBean> list = map.get("Residual junks");
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Residual junks", list);
//                            } else {
//                                List<RuleBean> list = new ArrayList<>();
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Residual junks", list);
//                            }
//                        } else {
//                            if (map.containsKey("Unknown")) {
//                                List<RuleBean> list = map.get("Unknown");
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Unknown", list);
//                            } else {
//                                List<RuleBean> list = new ArrayList<>();
//                                list.add(new RuleBean(name, path, description));
//                                map.put("Unknown", list);
//                            }
//                        }
//
//                    }
//
//                }
//                String s = JSON.toJSONString(map);
//
//                System.out.println(s);
//
//            } else {
//                System.out.println("找不到指定的文件");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String v(String str, String str2, String str3) {
//        int indexOf;
//        int indexOf2;
//        if (!str2.isEmpty()) {
//            indexOf = str.indexOf(str2);
//            if (indexOf > -1) {
//                indexOf += str2.length();
//                indexOf2 = str.indexOf(str3, indexOf);
//                if (indexOf2 < 0 || str3.isEmpty()) {
//                    indexOf2 = str.length();
//                }
//                return str.substring(indexOf, indexOf2);
//            }
//        }
//        indexOf = 0;
//        indexOf2 = str.indexOf(str3, indexOf);
//        indexOf2 = str.length();
//        return str.substring(indexOf, indexOf2);
//    }
//
//}
