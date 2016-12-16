package com.yd.controller.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;




/***
 *
 * @author huangwei
 * @since 2013-08-15
 */
public class HeaderUtil {
    public static final String OSTYPE_ANDROID="Android";
    public static final String OSTYPE_IOS="Ios";
    public static final String OSTYPE_WP="WINDOWS PHONE";
    public static final String OSTYPE_BLACKBERRY="BLACKBERRY";


    /***
     * pad
     */
    public static final String DEVICE_TYPE_PAD="Pad";
    /***
     * 手机
     */
    public static final String DEVICE_TYPE_PHONE="Phone";


    /***
     * 校验渠道终端版本号是否合法,eg:0.0.0.3
     *
     * @param clientVersion
     * @return true-->合法 ;false-->非法
     */
    public static boolean verifyClientVersion(String clientVersion) {
        boolean result = Pattern.matches("[\\d\\.]+", clientVersion);
        if (result) {
            result = Pattern.matches("^\\d\\.\\d\\.\\d\\.\\d$", clientVersion);
            return result;
        } else {
            return false;
        }
    }

    /**
     * 根据useragent和手机厂商查手机型号
     *
     * @param UA
     * @param
     * @return
     */
    public static String getMobModel(String UA, String operatingSystem) {

        if (UA == null) {
            return null;
        }
        if (operatingSystem == null) {
            return null;
        }
        UA=UA.toUpperCase();
        operatingSystem=operatingSystem.toUpperCase();
        // 存放正则表达式
        String rex = "";
        // 苹果产品
        if (operatingSystem.indexOf("IOS") != -1) {
            if (UA.indexOf("IPAD") != -1) {// 判断是否为ipad
                return "IPAD";
            }
            if (UA.indexOf("IPOD") != -1) {// 判断是否为ipod
                return "IPOD";
            }
            if (UA.indexOf("IPONE") != -1) {// 判断是否为ipone
                return "IPONE";
            }
            return "IOS DEVICE";

        }
        // 安卓系统产品
        if (operatingSystem.indexOf("ANDROID") != -1) {
//            String re = "BUILD";
//            rex = ".*" + ";" + "(.*)" + re;
//
//            Pattern p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE);
            Pattern p=Pattern.compile(";\\s?([^;]+?)\\s?(BUILD)?/");
            Matcher m = p.matcher(UA);
            boolean rs = m.find();
            if (rs) {
                 System.out.println("通过userAgent解析出机型222：" + m.group(1));
                return m.group(1);
            }
        }
        return null;
    }

    /**
     * 判断手机的操作系统 IOS/android/windows phone/BlackBerry
     *
     * @param UA
     * @return
     */
    public static ClientOsInfo getMobilOS(String UA) {
        UA=UA.toUpperCase();
        if (UA == null) {
            return null;
        }
        ClientOsInfo osInfo=new  ClientOsInfo();
        // 存放正则表达式
        String rex = "";
        // IOS 判断字符串
        String iosString = " LIKE MAC OS X";
        if (UA.indexOf(iosString) != -1) {
            if(isMatch(UA, "\\([\\s]*iPhone[\\s]*;", Pattern.CASE_INSENSITIVE)){
                osInfo.setDeviceType(DEVICE_TYPE_PHONE);
            }else if(isMatch(UA, "\\([\\s]*iPad[\\s]*;", Pattern.CASE_INSENSITIVE)){
                osInfo.setDeviceType(DEVICE_TYPE_PAD);
            }
            rex = ".*" + "[\\s]+(\\d[_\\d]*)" + iosString;
            Pattern p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(UA);
            boolean rs = m.find();
            if (rs) {
                String osVersion= m.group(1).replace("_", ".");
                osInfo.setVersion(osVersion);
//				System.out.println("Mobil OS is" + " IOS" +osVersion);
                osInfo.setOsTypeVersion(OSTYPE_IOS+"_" + osVersion);
            }else{
                System.out.println("IOS");
                osInfo.setOsTypeVersion(OSTYPE_IOS);
            }
            osInfo.setOsType(OSTYPE_IOS);
            return osInfo;
        }
        // Android 判断
        String androidString = "ANDROID";
        if (UA.indexOf(androidString) != -1) {
            if(isMatch(UA, "\\bMobi", Pattern.CASE_INSENSITIVE)){
                osInfo.setDeviceType(DEVICE_TYPE_PHONE);
            }else {
                osInfo.setDeviceType(DEVICE_TYPE_PAD);
            }
            rex = ".*" + androidString + "[\\s]*(\\d*[\\._\\d]*)";
            Pattern p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(UA);
            boolean rs = m.find();
            if (rs) {
                String version=m.group(1).replace("_", ".");
                osInfo.setVersion(version);
               // System.out.println("Mobil OS is " + OSTYPE_ANDROID + version);
                osInfo.setOsTypeVersion(OSTYPE_ANDROID+"_" + version);
            }else{
                System.out.println("Android");
                osInfo.setOsTypeVersion(OSTYPE_ANDROID);
            }
            osInfo.setOsType(OSTYPE_ANDROID);
            return osInfo;
        }
        // windows phone 判断
        String wpString = "WINDOWS PHONE";
        if (UA.indexOf(wpString) != -1) {
            rex = ".*" + wpString + "[\\s]*[OS\\s]*([\\d][\\.\\d]*)";
            Pattern p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(UA);
            boolean rs = m.find();
            if (rs) {
                System.out.println("Mobil OS is " + OSTYPE_WP + m.group(1));
                String version=m.group(1);
                osInfo.setVersion(version);
                osInfo.setOsTypeVersion(OSTYPE_WP+"_" + version);
            }else{
                System.out.println("WINDOWS PHONE");
                osInfo.setOsTypeVersion(OSTYPE_WP);
            }
            osInfo.setOsType(OSTYPE_WP);
            return osInfo;
        }
        // BlackBerry 黑莓系统判断
        String bbString = "BLACKBERRY";
        if (UA.indexOf(bbString) != -1) {
            rex = ".*" + bbString + "[\\s]*([\\d]*)";
            Pattern p = Pattern.compile(rex, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(UA);
            boolean rs = m.find();
            if (rs) {
                System.out.println("Mobil OS is" + " BLACKBERRY " + m.group(1));
                String version=m.group(1);
                osInfo.setVersion(version);
                osInfo.setOsTypeVersion(OSTYPE_BLACKBERRY+"_" + version);
            }else{
                System.out.println("BLACKBERRY");
                osInfo.setOsTypeVersion(OSTYPE_BLACKBERRY);
            }
            osInfo.setOsType(OSTYPE_BLACKBERRY);
            return osInfo;
        }
        if(UA.contains("LINUX")){//android
            if(isMatch(UA, "\\bMobi", Pattern.CASE_INSENSITIVE)){
                osInfo.setDeviceType(DEVICE_TYPE_PHONE);
            }else {
                osInfo.setDeviceType(DEVICE_TYPE_PAD);
            }

            Pattern p = Pattern.compile("U;\\s*(Adr[\\s]*)?(\\d[\\.\\d]*\\d)[\\s]*;",Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(UA);
            boolean result = m.find();
            String find_result = null;
            if (result)
            {
                find_result = m.group(2);
            }
            if(ValueWidget.isNullOrEmpty(find_result)){
                osInfo.setOsTypeVersion(OSTYPE_ANDROID);
                return osInfo;
            }else{
                osInfo.setVersion(find_result);
                osInfo.setOsTypeVersion(OSTYPE_ANDROID+"_"+find_result);
                return osInfo;
            }
        }

        //UCWEB/2.0 (iOS; U; iPh OS 4_3_2; zh-CN; iPh4)
        if(UA.matches(".*((IOS)|(iPAD)).*(IPH).*")){
            if(isMatch(UA, "[\\s]*iPh[\\s]*", Pattern.CASE_INSENSITIVE)){
                osInfo.setDeviceType(DEVICE_TYPE_PHONE);
            }else {
                osInfo.setDeviceType(DEVICE_TYPE_PAD);
            }
            Pattern p = Pattern.compile("U;\\s*(IPH[\\s]*)?(OS[\\s]*)?(\\d[\\._\\d]*\\d)[\\s]*;",Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(UA);
            boolean result = m.find();
            String find_result = null;
            if (result)
            {
                find_result = m.group(3);
            }
            if(ValueWidget.isNullOrEmpty(find_result)){
                osInfo.setOsTypeVersion(OSTYPE_IOS);
                osInfo.setOsType(OSTYPE_IOS);
                return osInfo;
            }else{
                String version=find_result.replace("_", ".");
                osInfo.setVersion(version);
                osInfo.setOsTypeVersion(OSTYPE_IOS+"_"+version);
                osInfo.setOsType(OSTYPE_IOS);
                return osInfo;
            }
        }
        return osInfo;
    }
    public static boolean isMatch(String source,String regx,int flags){
        Pattern p = Pattern.compile(regx,flags);
        Matcher m = p.matcher(source);
        boolean result = m.find();
        return result;
    }
    /***
     * 判断是否是手机(移动端)
     * @param userAgent
     * @return
     */
    public static boolean isMobile(String userAgent)
    {
        userAgent=userAgent.toLowerCase();
        String []mobile_agents = {"240x320","acer","acoon","acs-","abacho","ahong","airness","alcatel","amoi","android","anywhereyougo.com","applewebkit/525","applewebkit/532","asus","audio","au-mic","avantogo","becker","benq","bilbo","bird","blackberry","blazer","bleu","cdm-","compal","coolpad","danger","dbtel","dopod","elaine","eric","etouch","fly ","fly_","fly-","go.web","goodaccess","gradiente","grundig","haier","hedy","hitachi","htc","huawei","hutchison","inno","ipad","ipaq","ipod","jbrowser","kddi","kgt","kwc","lenovo","lg ","lg2","lg3","lg4","lg5","lg7","lg8","lg9","lg-","lge-","lge9","longcos","maemo","mercator","meridian","micromax","midp","mini","mitsu","mmm","mmp","mobi","mot-","moto","nec-","netfront","newgen","nexian","nf-browser","nintendo","nitro","nokia","nook","novarra","obigo","palm","panasonic","pantech","philips","phone","pg-","playstation","pocket","pt-","qc-","qtek","rover","sagem","sama","samu","sanyo","samsung","sch-","scooter","sec-","sendo","sgh-","sharp","siemens","sie-","softbank","sony","spice","sprint","spv","symbian","tablet","talkabout","tcl-","teleca","telit","tianyu","tim-","toshiba","tsm","up.browser","utec","utstar","verykool","virgin","vk-","voda","voxtel","vx","wap","wellco","wig browser","wii","windows ce","wireless","xda","xde","zte"};
        boolean is_mobile = false;
        for (String device : mobile_agents) {
            if(userAgent.contains(device)){
                is_mobile=true;
                break;
            }
        }
        return is_mobile;
    }


    public static void main(String[] args)
    {
        HeaderUtil headerUtil=new HeaderUtil();
        String userAgent="Mozilla/5.0 (Linux; U; Android 4.1.1; en-cn; HTC T528d) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 MicroMessenger/6.0.2.58_r984381.520 NetType/WIFI";
     //   System.out.println(headerUtil.isMobile(userAgent));
//        System.out.println(getMobModel(userAgent,"ANDROID"));
//        userAgent="Android 2.3.7 Linux; Opera Mobi/46154";
//        System.out.println(headerUtil.isMobile(userAgent));
        userAgent="Mozilla/5.0 (Linux; Android 4.4.4; Lenovo A768t Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrowser/6.8 TBS/036887 Safari/537.36 MicroMessenger/6.3.27.880 NetType/WIFI Language/zh_CN";
        //userAgent="Linux; Android 4.4.4; Lenovo A768t Build/KTU84P";
        //userAgent="Mozilla/5.0 (iPad; CPU OS 9_2 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13C75 Weibo (iPad5,4__weibo__6.11.1__ipad__os9.2)";

//        userAgent="Mozilla/5.0 (Linux; U; Android 4.2.2; zh-cn; 2014011 Build/HM2014011) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 MicroMessenger/6.2.2.54_rec1912d.581 NetType/3gnet Language/zh_CN";
       userAgent="Mozilla/5.0 (Linux; U; Android 4.3; zh-cn; R8007 Build/JLS36C) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30\n";
    //     userAgent="#Mozilla/5.0 (Linux; Android 5.1; HUAWEI TIT-AL00 Build/HUAWEITIT-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrowser/6.8 TBS/036872 Safari/537.36 MicroMessenger/6.3.27.880 NetType/WIFI Language/zh_CN";
      //  userAgent="#!@!#08/Dec/2016:15:17:35 +0800#!@!#192.168.8.53#!@!#GET /front/wx/user/index?token=baijinjuchang&wecha_id=o0GrGs8JioUJlruKhX1C4Hn1PC8c&openid=o0GrGs8JioUJlruKhX1C4Hn1PC8c HTTP/1.1#!@!#-#!@!#Mozilla/5.0 (Linux; Android 4.0.4; R811 Build/IMM76D) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrowser/6.8 TBS/036872 Safari/537.36 MicroMessenger/6.3.22.821 NetType/cmnet Language/zh_CN#!@!#117.136.37.249, 101.226.68.141, 60.216.52.3#!@!#-#!@!#200#!@!#8354#!@!#";
    userAgent="        message=\"#!@!#08/Dec/2016:15:17:35 +0800#!@!#192.168.8.53#!@!#GET /front/wx/user/index?token=baijinjuchang&wecha_id=o0GrGs8JioUJlruKhX1C4Hn1PC8c&openid=o0GrGs8JioUJlruKhX1C4Hn1PC8c HTTP/1.1#!@!#-#!@!#Mozilla/5.0 (Linux; Android 4.0.4; R811 Build/IMM76D) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrowser/6.8 TBS/036872 Safari/537.36 MicroMessenger/6.3.22.821 NetType/cmnet Language/zh_CN#!@!#117.136.37.249, 101.226.68.141, 60.216.52.3#!@!#-#!@!#200#!@!#8354#!@!#\";\n";
     userAgent="#!@!#11/Dec/2016:00:00:59 +0800#!@!#192.168.8.54#!@!#GET / HTTP/1.1#!@!#-#!@!#Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9b4) Gecko/2008030317 Firefox/3.0b4#!@!#140.207.63.102, 60.216.52.4#!@!#-#!@!#404#!@!#15127#!@!#\";\n";


        ClientOsInfo info= getMobilOS(userAgent);
        System.out.println(info.getDeviceType());
        System.out.println(info.getOsTypeVersion());
        System.out.println(info.getUserAgent());
        System.out.println(info.getOsType());
        System.out.println(info.getVersion());
        //获取手机型号
        getMobModel(userAgent,info.getOsType());
      //  userAgent="Meizu M9    Android 4.0.3   QQ 3.7  MQQBrowser/3.7/Mozilla/5.0 (Linux; U; Android 4.0.3; zh-cn; M9 Build/IML74K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30    Normal Mode";
//        Pattern pattern = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/");
//        Pattern pattern=Pattern.compile(";\\s?([^;]+?)\\s?(Build)?/");
//
//        Matcher matcher = pattern.matcher(userAgent);
//        String model = null;
//        if (matcher.find()) {
//            System.out.println("=="+matcher.group()+ "  ===========  "+matcher.group(0) +"  "+matcher.group(1));
//            model = matcher.group(1).trim();
//            System.out.println("通过userAgent解析出机型：" + model);
//        }
//



    }

}
