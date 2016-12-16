package com.yd.controller.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2016/5/4 0004.
 */
public class GetProperties {
   public static Properties getPropertiesValue(){

       InputStream inputStream3 = GetProperties.class.getClassLoader().getResourceAsStream("properties.properties");
       Properties p = new Properties();
       try {
           p.load(inputStream3);
       } catch (IOException e1) {
           e1.printStackTrace();
       }finally {
           try {
               inputStream3.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       return p;
   }

}
