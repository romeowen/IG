package com.tencent.SecondRW;
/**
 *@Package: com.tencent.FeatureSelection
 *@Title: SortClass.java
 *@author: wenshifeng
 *@date: 2014-8-28ионГ9:56:06
 */
public class SortClass {
	
	private double attrValue =0;
	private String count =null;
	
	public  SortClass(String ValuePair) {
		
		StringBuffer remains = new StringBuffer();
		String tempvalue [] = ValuePair.split(",+");
		this.attrValue=Double.parseDouble(tempvalue[0]);
		for(int i=1; i<tempvalue.length;i++) {
			remains.append(tempvalue[i] + ",");
		}
		remains.deleteCharAt(remains.length()-1);
		this.count=remains.toString();
	}
	
	public double getValue() {
		return this.attrValue;
	}
	
	public String getCount() {
		return this.count;
	}
	
	public String getValuePair() {
		
		StringBuffer ValuePair = new StringBuffer();
		ValuePair.append(Double.toString(this.attrValue) + ",");
		ValuePair.append(this.count);
		return ValuePair.toString();
	}

}
