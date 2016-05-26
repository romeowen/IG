package com.tencent.FeatureSelection;

import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.tencent.ThirdMR.*;
import com.tencent.Header.Header;
import com.tencent.FilesRead.AttributesGet;
import com.tencent.FirstMR.FirstMap;
import com.tencent.FirstMR.FirstMapKeyTuples;
import com.tencent.FirstMR.FirstMapValueTuples;
import com.tencent.FirstMR.FirstReduce;

/**
 *@Package: com.tencent.FeatureSelection
 *@Title: FirstMapReduceConf.java
 *@author: wenshifeng
 *@date: 2014-8-7����3:35:12
 */
public class MapReduceConf {
	
	private Configuration conf =new Configuration();
	private ArrayList<String> args = new ArrayList<String>() ;
	private int NumReduce = 1;
	
	public  MapReduceConf(String [] args, Configuration conf )  throws Exception {
		
		this.conf = conf;	
		for(String arg: args) {
			this.args.add(arg);
		}		
		//get the output and Attrpath file path and add a "/" if it didn't has;
		Header.output = args[args.length-4];            // the location of output and result;
		Header.AttrPath  = args[args.length-3];         // the location of Attributes information; 		
		if (!Header.output .endsWith("/"))
			Header.output  = Header.output  + "/";
		
		//get the Attribute Class, Name, Attribute Threshold and Number for divided;
		AttributesGet FilesAttr = new AttributesGet(Header.AttrPath, this.conf); // should be added .txt in hadooop clusters;
		FilesAttr.getAttributes();
		
		conf.set("AttributeClass", FilesAttr.getAttributeClass());	
		conf.set("InputCriteria",args[args.length-2]);		
		NumReduce = Integer.parseInt(args[args.length-1]);		
		Header.InputCriteria = Integer.parseInt(args[args.length-2]);

	}	

	public void FirstMapReduceConf() throws Exception {
		
	     	Job job1 = new Job(conf,"Job1");
			job1.setJarByClass(FeatureSelection.class);		
			job1.setMapperClass(FirstMap.class);
			//job1.setCombinerClass(FirstReduce.class);
			job1.setReducerClass(FirstReduce.class);		
			job1.setOutputKeyClass(FirstMapKeyTuples.class);
			job1.setOutputValueClass(FirstMapValueTuples.class);
			job1.setNumReduceTasks(NumReduce);
			// the location of data sets
			String InputPath [] = args.get(0).split(",");
		    for(int i=0; i< InputPath.length ; i++ ) {	
				FileInputFormat.addInputPath(job1, new Path( InputPath[i]));			
		    }				
			FileOutputFormat.setOutputPath(job1,new Path(Header.output + Header.location[0]));
			job1.waitForCompletion(true);	
	}
	

	public void ThirdMapReduceConf() throws Exception {		

			// the third mapreduce is for sorting of attribute value.		
	     	Job job3=new Job(conf,"Job3");
			job3.setJarByClass(FeatureSelection.class);		
			job3.setMapperClass(ThirdMap.class);		
			job3.setReducerClass(ThirdReduce.class);		
			job3.setOutputKeyClass(IntWritable.class);
			job3.setOutputValueClass(Text.class);
			job3.setNumReduceTasks(NumReduce);
			FileInputFormat.setInputPaths(job3, new Path(Header.output + Header.location[1]));
			FileOutputFormat.setOutputPath(job3,new Path(Header.output + Header.location[2]));
			job3.waitForCompletion(true);	
	}

}
