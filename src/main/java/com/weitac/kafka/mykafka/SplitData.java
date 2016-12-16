package com.weitac.kafka.mykafka;

import com.maxmind.geoip2.DatabaseReader;
import com.yd.controller.util.ClientOsInfo;
import com.yd.controller.util.HeaderUtil;
import com.yd.controller.util.IPMain;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.yd.controller.util.HeaderUtil.getMobModel;
import static com.yd.controller.util.HeaderUtil.getMobilOS;
import static com.yd.controller.util.HeaderUtil.isMobile;

/**
 * Created by Administrator on 2016/11/23.
 */

public class SplitData {
    public String fileinput(String message, DatabaseReader reader, IPMain main, Map<String,String> usermessageMap) throws Exception{
        Map<String,String> map=new HashMap<String, String>();

        String time="unknown"; //时间
        String localip="unknown";//本地ip
        String openid="unknown"; //openid
        String token="unknown";//token
        String url1="unknown";//请求url
        String url2="unknown";//来源url
        String netip="unknown";//网络ip
        String province="unknown";//省份
        String city="unknown";//地市
        String userpic="unknown";//用户微信图片
        String usernickname="unknown";//用户微信昵称
        String phone_term="unknown";//手机终端
        String phone_brand="unknown";//手机品牌

//        System.out.println("新的字符串解析 解析的字符串:"+message);
        if (message != null && message != "") {
            String[] column1 = StringUtils.splitByWholeSeparatorPreserveAllTokens(message, "#!@!#");
//            System.out.println("将字符串拆分成数组长度:" + column1.length);
            if(column1.length==11){
                    time = column1[1];    //访问时间和时区  18/Jul/2012:17:00:01 +0800
                    localip = column1[2]; //客户端地址      113.140.15.90
                    url1 = column1[3];//请求的URI和HTTP协议     “GET /pa/img/home/logo-alipay-t.png HTTP/1.1″
                    url2 = column1[4];// 跳转来源         “https://cashier.alipay.com…/”
                    String http_user_agent = column1[5];//用户终端代理    “Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; SV1; GTB7.0; .NET4.0C;
                    String http_x_forwarded_for = column1[6];//网路ip
                    String remote_user = column1[7];//客户端用户名称
                    String status = column1[8];//HTTP请求状态    200
                    String body_bytes_sent = column1[9];//发送给客户端文件内容大小        547
//                    System.out.println(time + "====" + localip + "====" + url1 + "====" + url2 + "====" + http_user_agent + "====" + http_x_forwarded_for
//                            + "====" + remote_user + "====" + status + "====" + body_bytes_sent);
                    
                    //处理用户终端
                    //验证是否手机端还是电脑端
                    if(isMobile(http_user_agent)){
                        ClientOsInfo info= getMobilOS(http_user_agent);
                        phone_term=info.getOsTypeVersion();
                        phone_term=phone_term==null?"unknown":phone_term;
                        //获取手机型号
                        phone_brand= getMobModel(http_user_agent,info.getOsType());
                        phone_brand=phone_brand==null?"unknown":phone_brand;
                    }else{
                        phone_brand="windows";
                        phone_term="windows";
                    }

                    //格式化时间
                    SimpleDateFormat sf = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss", Locale.ENGLISH);
                    Date d = sf.parse(time);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    time = formatter.format(d);
                    //处理url 将url后面的参数 按键值对的形式存放到map中

                    Map<String, String> mapqu = new HashMap<String, String>();
                    //跳转来源
                    String[] request_url = StringUtils.splitByWholeSeparatorPreserveAllTokens(url1, " ");
                    if (request_url.length >= 2) {
                        url1=request_url[1];
                        String[] request = StringUtils.splitByWholeSeparatorPreserveAllTokens(request_url[1], "?");
                        if (request.length >= 2) {
                            url1=request[0];
                            String[] messagerequest = StringUtils.splitByWholeSeparatorPreserveAllTokens(request[1], "&");
                            mapqu=getUserMessag(messagerequest);
                            openid = mapqu.get("openid") == null ? "unknown" : mapqu.get("openid");
                            if("unknown".equals(openid)){
                                openid = mapqu.get("wecha_id") == null ? "unknown" : mapqu.get("wecha_id");
                            }
                            if("unknown".equals(openid)){
                                openid = mapqu.get("wechaid") == null ? "unknown" : mapqu.get("wechaid");
                            }
                            token = mapqu.get("token") == null ? "unknown" : mapqu.get("token");
                        }
                    }

               // System.out.println(openid+"======"+token+"------"+url2.contains("?"));
                //请求来源
                String[] refer = StringUtils.splitByWholeSeparatorPreserveAllTokens(url2, "?");
                if (refer.length >= 2) {
                    url2=refer[0];
                    String[] messagerefer = StringUtils.splitByWholeSeparatorPreserveAllTokens(refer[1], "&");
                    mapqu=getUserMessag(messagerefer);
                    if ("unknown".equals(openid)) {
                        openid = mapqu.get("openid") == null ? "unknown" : mapqu.get("openid");
                        if("unknown".equals(openid)){
                            openid = mapqu.get("wecha_id") == null ? "unknown" : mapqu.get("wecha_id");
                        }
                        if("unknown".equals(openid)){
                            openid = mapqu.get("wechaid") == null ? "unknown" : mapqu.get("wechaid");
                        }
                    }
                    if ("unknown".equals(token)) {
                        token = mapqu.get("token") == null ? "unknown" : mapqu.get("token");
                    }
                }

                String[] ip_arr= StringUtils.splitByWholeSeparatorPreserveAllTokens(http_x_forwarded_for, ",");
                   if(ip_arr.length==3){
                       netip=ip_arr[0].trim();
                   }else if(ip_arr.length==1){
                       netip=ip_arr[0].trim();
                   }
                  // System.out.println(netip);
                  if(!"unknown".equals(netip))  {
                      //通过网路ip获取用户省 市
                      Map<String, String> map_pv = main.getCity(netip, reader);
                      province = map_pv.get("province") == null ? "unknown" : map_pv.get("province");
                      city = map_pv.get("city") == null ? "unknown" : map_pv.get("city");
                  }

                    //通过openid　获取用户头像和昵称
                    //获取数据
                     
                    message = time + "," + localip + "," + openid + "," + token + "," + url1 + "," + url2 + "," + netip + "," + province + "," + city + ","+phone_term+","+phone_brand;
            }else{
                message = "";
            }
        }else {
            message = "";
        }
//        System.out.println("最后的message:"+message);
        return message;
    }
    public boolean ifcan(String[] string,int fazhi ){
        int length = string.length;
        boolean flag = false;
        if(length>fazhi){
            flag = true ;
        }
        return flag;
    }
    public Map<String,String> getUserMessag(String[] message){
        Map<String,String> map=new HashMap<String, String>();
        for (String s : message) {
            String[] ss = StringUtils.splitByWholeSeparatorPreserveAllTokens(s, "=");
            if (ifcan(ss, 1)) {
                // System.out.println(ss[0]+"=="+ss[1]);
                String key = ss[0];
                String value = ss[1];
                map.put(key, value);
            }
        }
        return map;
    }

   
}
