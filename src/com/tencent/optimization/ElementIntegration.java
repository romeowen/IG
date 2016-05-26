package com.tencent.optimization;

import java.util.ArrayList;
import org.apache.hadoop.io.Text;
/**
 *@Package: com.tencent.optimization
 *@Title: ElementIntegration.java
 *@author: wenshifeng
 *@date: 2014-8-4ионГ11:25:15
 */
public class ElementIntegration {	
	 
	//according to the set threshold, divide the source list;
	 public String ReIntegration(String original,ArrayList<Double> Integration ) {
		 		 
		 StringBuffer temp = new StringBuffer();
		 String [] TempValue = original.split(",");
		 int len =  TempValue.length;
		 
		 double threshold = 0;
		 int start=0, AllSample=0;

		 if (Integration.get(Integration.size()-1) < Double.parseDouble(TempValue[len-4])) {
			 
			 Integration.add(Double.parseDouble(TempValue[len-4]));
		 }
		 
		 for(int i=0; i< Integration.size(); i++) {
			 threshold = Integration.get(i);
			 int pos=0, neg=0,j=start;
			 for(; j< len-3; j+=4) {
				 if( Double.parseDouble(TempValue[j]) <=threshold ) {
					 pos+=Integer.parseInt(TempValue[j+1]);
					 neg+=Integer.parseInt(TempValue[j+2]);					 
				 }
				 else break;	
				 
			 }
			 start = j;
			 if (pos == 0 && neg == 0) continue;   //this condition is used to avoid the first threshold is too low to be zeros;
			 temp.append(Double.toString(threshold) + "," + Integer.toString(pos) + "," + 
			 Integer.toString(neg) + "," + Integer.toString(pos+neg) + ",");
			 AllSample+= pos+neg;
		 }

		 temp.append(Integer.toString(AllSample));		 
		 return temp.toString();
	 }
	

}
