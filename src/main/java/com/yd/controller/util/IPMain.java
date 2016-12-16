package com.yd.controller.util;

import java.io.IOException;
import java.net.InetAddress;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
/**
 * Created by Administrator on 2016/11/22.
 */
public class IPMain {
        private static String version2 = "/usr/local/soft/GeoLite2-City.mmdb";

        public static void main(String[] args) {
            IPMain main = new IPMain();
           // System.out.println(main.getCityTest("58.247.206.145", new File(version2)));
            try {
                DatabaseReader reader = new DatabaseReader.Builder(new File(version2)).build();
                System.out.println(main.getCity("58.251.80.45",reader));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    public Map<String,String> getCity(String ipStr, DatabaseReader reader) {
        Map<String,String> map=new HashMap<String, String>();
        try {
            InetAddress ip = InetAddress.getByName(ipStr);
            CityResponse response = reader.city(ip);
            // 获取所在省份
            String shengStr = response.getSubdivisions().get(0).getNames().get("zh-CN");
            // 获取所在城市(由于是免费版的数据库,查找城市的时候是不准确的)
            String shiStr = response.getCity().getNames().get("zh-CN");
            map.put("province",shengStr);
            map.put("city",shiStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


}
