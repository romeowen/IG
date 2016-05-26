package com.tencent.SecondRW;

import java.util.ArrayList;

import com.tencent.Header.Header;
import com.tencent.optimization.ElementIntegration;
import com.tencent.optimization.ThresholdDivide;

/**
 *@Package: com.tencent.FeatureSelection
 *@Title: DiffClassDeal.java
 *@author: wenshifeng
 *@date: 2014-8-28ÏÂÎç2:51:44
 *@description: this class is used for deal with class type like N, ND, TD
 *the main function is to divide them and rearrange them to a new list;  
 */
public class DiffClassDeal {
	
	private String ValuePair = null;
	private String CurrentClass=null;
	private int CurrentIndex = 0;
	
	public DiffClassDeal(String ValuePair, String CurrentClass, int CurrentIndex) {
		
		this.ValuePair =  ValuePair;         // the sorted original value and statistic list
		this.CurrentClass = CurrentClass;   // the Class type of current list
		this.CurrentIndex = CurrentIndex;  // the Attribute column number of this list
	}
	// the entry of method for different class type
	public String DealWithDiffClass() {
		
		// deal with class belong to N
		if (this.CurrentClass.equals(Header.N)) {
			
			int Ndivide = Header.Numdivide;
			for (int i=0; i< Header.AttributeN.size()/2; i++) {
				if (Header.AttributeN.get(2*i)==CurrentIndex) {
					Ndivide = Header.AttributeN.get(2*i+1);
					break;
				}
			}
			return	this.DivideAndCombine(Ndivide);
		}
		
		// deal with class belong to ND
		if (this.CurrentClass.equals(Header.ND)) {
			int Ndivide = Header.Numdivide;  //default divide number
			for (int i=0; i<Header.AttributesND.size()/2; i++) {
				if (Header.AttributesND.get(2*i)==CurrentIndex) {
					Ndivide =  Header.AttributesND.get(2*i+1);
					break;
				}
			}
			return this.DivideAndCombine(Ndivide);			
		}
		
		// deal with class belong to TD
		if (this.CurrentClass.equals(Header.TD)) {
			String [] ThresholdAll = Header.AttributeThreshold.toString().split(Header.Dashsplit);
			for(String val: ThresholdAll) {
				String [] Threshold = val.split(",+");
				if(!Threshold[0].trim().equals(Integer.toString(CurrentIndex))) 
					continue;
				return this.ThresholdPartion(val);
			}			
		}
		//if none of above deal method ,something must wrong.so error and exit;
		System.exit(1);
		System.out.println("some thing is wrong");
		return "some thing is wrong";
	}
	
	// deal with class type like N and ND
	public String DivideAndCombine(int Ndivide) {
		
		//used for divide the source list and get the threshold
		ThresholdDivide Threshold = new ThresholdDivide(this.ValuePair);
		// used for rearrange the source list to give threshold region
		ElementIntegration Integration = new ElementIntegration( );
		//return the segmented list
		return Integration.ReIntegration(this.ValuePair, Threshold.AutoDivide(Ndivide));
	}
	
	
	public String ThresholdPartion(String ThresholdList) {
		
		// read the threshold List convert to ArrayList
		String [] Thresholdpair = ThresholdList.split(",");
		ArrayList<Double> Threshold = new ArrayList<Double>(); 
		for(int i=1; i<Thresholdpair.length; i++) {
			Threshold.add(Double.parseDouble(Thresholdpair[i]));
		}			
		//rearrange the source list 
		ElementIntegration Integration = new ElementIntegration();
		return Integration.ReIntegration(this.ValuePair,Threshold);
	}
	

}
