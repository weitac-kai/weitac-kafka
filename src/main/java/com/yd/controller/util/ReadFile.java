package com.yd.controller.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2016/11/30.
 */
public class ReadFile {
        public Map<String,String> GetUserMessage(){
            Map<String,String> map=new HashMap<String, String>();
            GetProperties getProperties=new GetProperties();
            Properties p=getProperties.getPropertiesValue();
            String fileName=p.get("user_name_file")+"";
            File file = new File(fileName);
            BufferedReader reader = null;
            try {
                System.out.println("以行为单位读取文件内容，一次读一整行：");
                reader = new BufferedReader(new FileReader(file));
                String tempString = null;
                int line = 1;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    // 显示行号
                    String[] arr=StringUtils.splitByWholeSeparatorPreserveAllTokens(tempString, ",");
                    System.out.println("line " + line + ": " + tempString+"=="+arr.length);
                    if(arr.length==3){
                        map.put(arr[0],arr[1]+arr[2]);
                    }
                    line++;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
            System.out.println("初始化hashmap的大小："+map.size());
            return map;
        }

    public static void main(String[] args){

        ReadFile readFile=new ReadFile();
        Map<String,String> map=readFile.GetUserMessage();
    }
}
