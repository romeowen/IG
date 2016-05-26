package com.tencent.SecondRW;

import java.util.Comparator;



/**
 *@Package: com.tencent.FeatureSelection
 *@Title: ValueComparator.java
 *@author: wenshifeng
 *@date: 2014-8-28ионГ10:09:33
 */
public class ValueComparator implements Comparator<SortClass>{
	
	public int compare (SortClass A, SortClass B) {
		double diff=A.getValue()-B.getValue();
		if (diff>0)
		   return   1;
	    if (diff<0)
		   return  -1;
	    else
	       return   0;
	}
	
	
	

}
