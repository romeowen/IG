package com.tencent.FilesRead;

import java.util.ArrayList;

import com.tencent.Header.Header;

/**
 *@Package: com.tencent.FilesRead
 *@Title: RusultSortClass.java
 *@author: wenshifeng
 *@date: 2014-8-18ÉÏÎç9:38:09
 */
public class ResultSortClass {
	
	public ArrayList<String> IndexResult = new ArrayList<String>();
	public double value = 0;
	private String AttrName=null;
	private String AttrClass=null;
	private String AttrRest= null;
	
	public void ResultClass(String TextLine,int index) {   // used for sorting and get other information
		
		String [] TempValue = TextLine.split("\\s+");		
		
		this.AttrName=Header.AttributeName.get(Integer.parseInt(TempValue[0])-1);
		this.AttrClass=Header.AttributeClass.get(Integer.parseInt(TempValue[0])-1);
		IndexResult.add(TempValue[0]);
		String [] splitvalue = TempValue[1].split(",+");						
		this.value = Double.parseDouble(splitvalue[index]);
		if (this.AttrClass.equals(Header.N)) {
			IndexResult.add(TempValue[index+2]);
			this.AttrRest=TempValue[index+2];
		} else {
			IndexResult.add(TempValue[2]);
			this.AttrRest=TempValue[2];
		}
	}
	
	public double getvalue() {   // return the value
		
		return this.value;
		
	}
	public String getclass() {   // get the class
		
		return this.AttrClass;
	}

	public String getAtt() {        //get attribute and value
		
		StringBuffer TEMP = new StringBuffer();
		TEMP.append(this.AttrName + "\t");
		TEMP.append(Double.toString(this.value));
		return TEMP.toString();		
	}
	
	
	public String getRest() {   //get the remaining including threshold impression click ctr and so on
		return this.AttrRest;
		
	}

	public String getN() {   //return the N attribute results
		
		if(this.AttrClass.equals(Header.N)) {
			
			StringBuffer TEMP = new StringBuffer();
			TEMP.append(this.AttrName + "\t");
			TEMP.append(Double.toString(this.value));
			String [] TempString = this.AttrRest.split(",+");
			for(String val: TempString) {
				TEMP.append("\t" + val);			
			}
			return TEMP.toString();
		}else {
			System.exit(1);
			return "error";
			
		}

	}

	
}
