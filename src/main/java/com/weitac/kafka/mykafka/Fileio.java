package com.weitac.kafka.mykafka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.maxmind.geoip2.DatabaseReader;
import com.yd.controller.util.IPMain;


public class Fileio {
	 private String message;
	 private static String version2 = "/usr/local/soft/GeoLite2-City.mmdb";
//	 private static String version2 = "E:\\GeoLite2-City.mmdb";

	 public static void main(String[] args) throws Exception{
		 BufferedReader buf = null;
	     BufferedWriter bufferedWriter = null;
	     Map<String,String> map=new HashMap<String, String>();
	     
	     Date now = new Date();
	     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	     String today = dateFormat.format(now);
	     
	     String today1 = args[0];
	     
	     
	     String inputPath = "/usr/local/soft/flume/conf/log/lognginx.log";
         String outputPath = "/usr/local/soft/flume/conf/log/logoutput_" + today1 + ".log";
//       String inputPath = "C:\\Users\\Administrator\\Desktop\\data\\nginx.log";
//       String outputPath = "C:\\Users\\Administrator\\Desktop\\data\\xinlog.log";
         
         System.out.println(inputPath);
         System.out.println(outputPath);
         
         Fileio log = new Fileio();
         
         buf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath))));
         bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputPath))));
         String line;
         SplitData con = new SplitData();
         DatabaseReader reader = null;
         IPMain main = null;
         main = new IPMain();
         reader = new DatabaseReader.Builder(new File(version2)).build();
         while ((line = buf.readLine()) != null) {
        	 String message = line;
             //System.out.println("接收到11: " + message);
        	 message = con.fileinput(message, reader, main,map);
        	 //System.out.println("接收到22: " + message);
        	 bufferedWriter.write(message + "\n");
        	 bufferedWriter.flush();       	 
         }
		
	}
}
