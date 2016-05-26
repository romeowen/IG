package com.tencent.FilesRead;

import java.util.Comparator;
/**
 *@Package: com.tencent.FilesRead
 *@Title: ValueComparator.java
 *@author: wenshifeng
 *@date: 2014-8-18ионГ10:15:01
 */
public class ValueComparator implements Comparator<ResultSortClass>{
	
	public int compare (ResultSortClass A, ResultSortClass B) {
		Double diff=A.getvalue()-B.getvalue();
		if (diff>0)
		   return   1;
	    if (diff<0)
		   return  -1;
	    else
	       return   0;
	}
	
	
	

}
