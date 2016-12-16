package com.weitac.kafka.mykafka;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.DatabaseReader.Builder;
import com.yd.controller.util.IPMain;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.apache.commons.lang3.StringUtils;

public class CosumerSample extends Thread
{
  private String topic;
  private String outputpath;
  private String message;
  private static String version2 = "/usr/local/soft/GeoLite2-City.mmdb";
  private Map<String,String> map=new HashMap<String, String>();

  public CosumerSample(String topic)
  {
    this.topic = topic;
  }

  public CosumerSample()
  {
  }

  public void run()
  {
    Date now = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String today = dateFormat.format(now);
    this.outputpath = ("/usr/local/soft/out_" + today + ".log");
    BufferedWriter bufferwriter = null;
    DatabaseReader reader = null;
    IPMain main = null;
    try
    {
      try {
        main = new IPMain();
        reader = new DatabaseReader.Builder(new File(version2)).build();
      } catch (Exception e) {
        e.printStackTrace();
      }

      bufferwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(this.outputpath))));
      ConsumerConnector consumer = createConsumer();
      Map topicCountMap = new HashMap();
      topicCountMap.put(this.topic, Integer.valueOf(1));
      Map messageStreams = consumer.createMessageStreams(topicCountMap);
      KafkaStream stream = (KafkaStream)((List)messageStreams.get(this.topic)).get(0);
      ConsumerIterator iterator = stream.iterator();
      
      SplitData con=new SplitData();
      while (iterator.hasNext()) {
        String message = new String((byte[])iterator.next().message());
        System.out.println("接收到11: " + message);
        try {
          message = con.fileinput(message, reader, main,map);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        System.out.println("接收到22: " + message);
        bufferwriter.write(message + "\n");
        bufferwriter.flush();
      }
    }
    catch (IOException e1) {
      e1.printStackTrace();
    } finally {
      if (bufferwriter != null) {
        try {
          bufferwriter.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
      System.exit(0);
    }
  }

  private ConsumerConnector createConsumer()
  {
    Properties properties = new Properties();
    properties.put("zookeeper.connect", "localhost:2181");
    properties.put("group.id", "group11");
    return Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
  }

  public static void main(String[] args)
  {
    new CosumerSample("topictest").start();
  }
}