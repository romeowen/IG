package com.tencent.optimization;

import java.util.ArrayList;

import org.apache.hadoop.io.Text;
/**
 *@Package: com.tencent.optimization
 *@Title: ThresholdDivide.java
 *@author: wenshifeng
 *@date: 2014-8-4ÉÏÎç9:37:23
 */
public class ThresholdDivide {
	
	private ArrayList<Double> sequence = new ArrayList<Double>();  //used for saving the attribute value and number,and the total number;
	private ArrayList<Double> Threshold = new ArrayList<Double>();  //used for storing the threshold;

	// initiate the ThresholdDivide class 
	public  ThresholdDivide(String ValuePair) {				
		
		double Allsample = 0;
		String [] TempValue=ValuePair.split(",");
		for(int i=0; i<TempValue.length/4; i++) {
			this.sequence.add(Double.parseDouble(TempValue[i*4]));
			this.sequence.add(Double.parseDouble(TempValue[i*4+3]));
			Allsample+= Double.parseDouble(TempValue[i*4+3]);
		}	
		this.sequence.add(Allsample);		//total number			
	}
	
	// used for divide 
	public ArrayList<Double> AutoDivide(int N) {
		
		int len = this.sequence.size();
		double mean =0;
		if (this.sequence.get(len-1)<N) {
			mean = 1;                       //if the total sample is less then the Divide number, save the source list;
		} else {
			mean = this.sequence.get(len-1)/N;
		}
		int start=0, Num=0;
		while(start < len-2) {
			int i = start;
			double value=0;
			Num=0;
			for(; i< len-2; i+=2) {
				value =  this.sequence.get(i+1);
				Num+= value;
				if(Num >= mean) break;
				
			}	
			if (i==len-1) 
				i=len-3;
			
			if (Num-value ==0) {
				
				Threshold.add((Double) this.sequence.get(i));
				start = i+2;
				continue;
				
			}
			if( Num-mean > mean-(Num-value) ) {
				
				Threshold.add((Double) this.sequence.get(i-2));
				start = i;
			}
			else if (Num-mean <= mean-(Num-value) ) {
				
				Threshold.add((Double) this.sequence.get(i));
				start = i+2;
			}
						
		}	
		this.sequence.clear();
        return Threshold;
   }
	

}