package com.tencent.FirstMR;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.tencent.Header.Header;


/**
 *@Package: com.tencent.FeatureSelection
 *@Title: FirstMap.java
 *@author: wenshifeng
 *@date: 2014-7-30����10:52:26
 *@discription: This file is map the text file to a <key,value> which is <attributeIndex,attributeValue> <posnum,negnum,pos+neg>
 */
public  class FirstMap
extends Mapper<Object, Text,FirstMapKeyTuples,FirstMapValueTuples> {
	
	private FirstMapKeyTuples feature=new FirstMapKeyTuples();
	private FirstMapValueTuples inivalue= new FirstMapValueTuples();
	
    public void map(Object key,Text value,Context context) 
     throws IOException,InterruptedException {
    	
    	/* this section is used for get the AttriuteClass*/
    	setup(context);    	
    	String AttributeClass = context.getConfiguration().get("AttributeClass");
    	ArrayList<String> TAttribute = new ArrayList<String>();
    	String [] Tem=AttributeClass.split("\\s+");
    	for(int i=0; i<Tem.length; i++) {
    		TAttribute.add(Tem[i]);
    	}
    	int LabelIndex = 0;
    	
    	/*the next section is the real programe*/
    	String [] TempValue=value.toString().split(",",-1);
    	//System.out.println(TempValue.length);
    	for(int i=0; i<TempValue.length; i++) {    		
    		if (TAttribute.get(i).equals(Header.L)) {    			
    			LabelIndex = i;
    			break;
    		}
    	}
 //   	System.out.printf("labelindex is %d\n", LabelIndex);
    	for(int i=0; i<TempValue.length; i++) {    		
    			
    		if( !TAttribute.get(i).equals(Header.I)) {
    			//空值表示为-1.
    			if(TempValue[i].trim().isEmpty() || TempValue[i].trim().toUpperCase().equals(Header.str_null)) {    				
        			feature.attributeIndex = i+1;
        			feature.attributeValue = "-1";    				
    			}
    			else {
        			feature.attributeIndex = i+1;
        			feature.attributeValue = TempValue[i].trim();
    			}	
        		
        		if( i == LabelIndex ) {
        			if (TempValue[LabelIndex].trim().equals(Header.Poslabel)) {
        				inivalue.postiveNum = 1;
        				inivalue.negativeNum = 0;    			
        			}
        		} else{
        			if (TempValue[LabelIndex].trim().equals(Header.Poslabel)) {
        				inivalue.postiveNum = 1;
        				inivalue.negativeNum = -1;  
        			}
        		}

        	   if (TempValue[LabelIndex].trim().equals(Header.Neglabel)) {
        			inivalue.postiveNum = 0;
        			inivalue.negativeNum = 1;
        		} 
        		inivalue.posAndneg=0;  
        		context.write(feature,inivalue);
    		}

    	}
    }
	
}