/*
 * Problem 2 Sell My Pet Food
 * 
 * V1.0
 * 6/1/2019
 * Copyright(c) 2019 PLTW to present. All rights reserved
 */
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

/**
 * A DataCollector class to manage social media posts
 */
public class DataCollector
{
  private ArrayList<CSVRecord> socialMediaPosts = new ArrayList<CSVRecord>();
  private ArrayList<CSVRecord> targetWords = new ArrayList<CSVRecord>();
  private ArrayList<Double> sentiment = new ArrayList<Double>();
  private ArrayList<String> market = new ArrayList<String>();

  @SuppressWarnings("resource")
  public DataCollector(String smp, String tw) throws IOException
  {
    Reader in = new FileReader(smp);
    String[] socialMediaPostsHeaders = { "author", "title", "content"};
    CSVFormat csvFormatPosts = CSVFormat.DEFAULT.builder()
    .setHeader(socialMediaPostsHeaders)
    .setSkipHeaderRecord(true)
    .build();
    for (CSVRecord r : csvFormatPosts.parse(in)) {
      socialMediaPosts.add(r);
      sentiment.add(0.0);
    }

    in = new FileReader(tw);
    String[] targetWordsHeaders = {"word", "sentiment_rating"};
    CSVFormat csvFormatTarget = CSVFormat.DEFAULT.builder()
    .setHeader(targetWordsHeaders)
    .setSkipHeaderRecord(true)
    .build();
    for (CSVRecord r : csvFormatTarget.parse(in)) {
      targetWords.add(r);
    }
  }

  public void calculate_sentiment() {
    String content;
    for (int i = 0; i < socialMediaPosts.size(); i++) {
      CSVRecord key = socialMediaPosts.get(i);
      content = key.get("content").toLowerCase();
      for (CSVRecord w: targetWords) {
        String word = w.get("word");
        Double rating = Double.valueOf(w.get("sentiment_rating"));
        if (rating < 0.5) {
          rating *= -1.0;
        }
        if (content.indexOf(word) != -1) {
          sentiment.set(i, sentiment.get(i) + rating);
        }
      }
    }
  }

  public void write_market(String marketFile) {
    try {
      File myObj = new File(marketFile);
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (int i = 0; i < sentiment.size(); i++) {
      if (sentiment.get(i) >= 3.0 && !market.contains(socialMediaPosts.get(i).get("author"))) {
          try {
            FileWriter myWriter = new FileWriter(marketFile, true);
            myWriter.write(socialMediaPosts.get(i).get("author") + "\n");
            myWriter.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
          market.add(socialMediaPosts.get(i).get("author"));
        }
    }
  }

  public void write_advertisements() {
    try {
      File myObj = new File("advertisement1.txt");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      File myObj = new File("advertisement2.txt");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (int i = 0; i < market.size(); i++) {
      try {
        FileWriter myWriter = new FileWriter("advertisement1.txt", true);
        myWriter.write(market.get(i) + ", I'm sure you'll enjoy our NEW superComputer 2000. Buy it NOW. It's fast, sleek, and portable for all occasions." + "\n");
        myWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      try {
        FileWriter myWriter = new FileWriter("advertisement2.txt", true);
        myWriter.write(market.get(i) + ". NOW ON SALE: the NEW superComputer 2000. Buy now and get 30% off your order. While supplies last. " + "\n");
        myWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  public void print() {
    for (CSVRecord r : targetWords) {
      System.out.format("%s %s", r.get("word"), r.get("sentiment_rating"));
    }
    for (CSVRecord r : socialMediaPosts) {
      System.out.format("%s %s %s", r.get("author"), r.get("title"), r.get("content"));
    }
  }

}