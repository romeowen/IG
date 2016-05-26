package com.tencent.FirstMR;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;


/**
 *@Package: com.tencent.FeatureSelection
 *@Title: FirstReduce.java
 *@author: wenshifeng
 *@date: 2014-7-30����10:53:22
 *@discription: This FirstReduce follows FirstMap.java which is used to count the postive and negtive num
 *under specific attribute and attribute value.<attributeIndex,attributeValue> <posnum,negnum,pos+neg>
 */

public  class FirstReduce extends
Reducer<FirstMapKeyTuples,FirstMapValueTuples,FirstMapKeyTuples,FirstMapValueTuples> {
		
	private FirstMapValueTuples tuples = new FirstMapValueTuples();
	
	public void reduce(FirstMapKeyTuples key,Iterable<FirstMapValueTuples> value,Context context)
	throws IOException,InterruptedException {
		
		int pos= 0, neg= 0;					
		for(FirstMapValueTuples val : value) {
			pos+= val.postiveNum;
			neg+= val.negativeNum;
		} 
		
		tuples.postiveNum = pos;
		tuples.negativeNum = neg;
		tuples.posAndneg = neg + pos;
		if (tuples.posAndneg >= tuples.postiveNum) 
		{
		   context.write(key,tuples);
		}
	}
}