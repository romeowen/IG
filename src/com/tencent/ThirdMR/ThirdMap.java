package com.tencent.ThirdMR;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;


/**
 *@Package: com.tencent.Sort
 *@Title: SortMap.java
 *@author: wenshifeng
 *@date: 2014-8-1ÉÏÎç9:15:00
 */
public class ThirdMap extends Mapper<Object, Text,IntWritable,Text> {
	
	private IntWritable index = new IntWritable();
	private Text result= new Text();
	
    public void map(Object key,Text value,Context context) 
     throws IOException,InterruptedException {    		    	

    	StringBuffer Temp= new StringBuffer();    	
    	String [] itr = value.toString().split(",+");
    	int len= itr.length;
    	for(int i=0;i<len;i++) {    		
    		if(i==0) {
    		    index.set(Integer.parseInt(itr[i].trim()));
    			continue;
    		}		
    		Temp.append(itr[i].trim() + ",");
    	} 
    	Temp.deleteCharAt(Temp.length() - 1);  
    	result.set(Temp.toString());

    	context.write(index,result);   	

    	}
    }
	
