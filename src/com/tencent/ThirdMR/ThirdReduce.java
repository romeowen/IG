package com.tencent.ThirdMR;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.tencent.optimization.*;
import com.tencent.Header.Header;
/**
 *@Package: com.tencent.Sort
 *@Title: SortReduce.java
 *@author: wenshifeng
 *@date: 2014-8-1ÉÏÎç9:29:15
 */  
public class ThirdReduce extends 
Reducer<IntWritable,Text,IntWritable,Text> {		

	private Text tuples = new Text();
	
	public void reduce(IntWritable key,Iterable<Text> value,Context context)
	throws IOException,InterruptedException {
		
		ArrayList<String> AttributeClass = new ArrayList<String>();            // used for storage the CLass 
		/*get the global variables*/
		setup(context);
		String [] TempClass = context.getConfiguration().get("AttributeClass").split("\\s+");
		ArrayList<Double> LabelStatistic =  new ArrayList<Double>();
		LabelStatistic.add(Double.parseDouble(context.getConfiguration().get("PosNum")));
		LabelStatistic.add(Double.parseDouble(context.getConfiguration().get("NegNum")));
		LabelStatistic.add(Double.parseDouble(context.getConfiguration().get("PosNeg")));
		
		System.out.println(Integer.toString(key.get()));
		
		for(String temp: TempClass) {
			AttributeClass.add(temp);			
		}
		// get the user choice criteria
		int InputCriteria = Integer.parseInt(context.getConfiguration().get("InputCriteria"));		
		// get this reduce process and the Class		
		String CurrentClass = AttributeClass.get(key.get()-1);	
		// default N Class divide is 100
        for(Text val : value) {  
          /* this section is used for compute information ranking*/	
	       InforCompute information = new InforCompute(val,InputCriteria);
	       tuples.set(information.RankingComp(CurrentClass,LabelStatistic));
	    }
		context.write(key,tuples);
		
		}
			
	}
