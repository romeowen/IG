package com.tencent.SecondRW;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import com.tencent.Header.Header;

/**
 *@Package: com.tencent.FeatureSelection
 *@Title: FirstMRToFile.java
 *@author: wenshifeng
 *@date: 2014-8-28����9:11:31
 */
public class MRToFile {
	
	private String filepath = null;                               // the path of file
	private Configuration conf = new Configuration();	
	private SortClass []  SortComb = null;
	private StringBuffer ValueAll = null;
	private int CorSNum=0;
	
	
	// write the ranking result in the ranking text file.    
	private FileSystem fswrite= null;
	private FSDataOutputStream fout = null;
	private BufferedWriter out = null;
	
	// initiate the class
    public MRToFile(String filePath, Configuration conf) throws IOException {    	
    	this.conf = conf;    		
    	this.filepath = filePath;
    } 
    
    // initiate the output IO
    public void initiate(String location) {
    	
    	try {
        	fswrite = FileSystem.get(URI.create(location),conf);
        	fout = fswrite.create(new Path(location));
        	out = new BufferedWriter(new OutputStreamWriter(fout,"UTF-8"));

    	}catch(IOException e) {
    		e.printStackTrace();
    	}    	
    }
    
    // read each line of a file
    public void ReadEachFile(String CurrentPath,int CurrentIndex) {
    	
    	try{
    		FileSystem fs =FileSystem.get(URI.create(CurrentPath),conf);    
    		FSDataInputStream fin =fs.open(new Path(CurrentPath));
    		BufferedReader in = new BufferedReader(new InputStreamReader(fin,"UTF-8")); 
    		String tempString=null;
    		String SegString [] =null;
    	    while ((tempString = in.readLine()) != null) {
    	    	SegString =tempString.split(",+");
    	    	if(SegString[0].trim().equals(Integer.toString(CurrentIndex))) {
    	    		for(int k=1; k<SegString.length;k++) {
        	    		ValueAll.append(SegString[k].trim() + ",");    	    			
    	    		}
    	    		ValueAll.deleteCharAt(ValueAll.length()-1);
    	    		ValueAll.append("\t");
    	    		this.CorSNum += Integer.parseInt(SegString[SegString.length-1].trim());
    	    	}
    	    }
    	    in.close();    		
    	}catch (IOException e) {
    		e.printStackTrace();
    	}    	
    	
    }
    
    
    public void Labeldealwith(String CurrentPath) {
    	
    	try{
    		FileSystem fs =FileSystem.get(URI.create(CurrentPath),conf);    
    		FSDataInputStream fin =fs.open(new Path(CurrentPath));
    		BufferedReader in = new BufferedReader(new InputStreamReader(fin,"UTF-8")); 
    		String tempString=null;
    		String SegString [] =null;
    	    while ((tempString = in.readLine()) != null) {
    	    	SegString =tempString.split(",+");
    	    	if(SegString[0].trim().equals(Integer.toString(Header.LableIndex))) {
    	    		if(Integer.parseInt(SegString[1].trim())==0) {
    	    			Header.PosNeg = Integer.parseInt(SegString[SegString.length-1]);
    	    		} else if (Integer.parseInt(SegString[1].trim())==1) {
    	    			Header.PosNum = Integer.parseInt(SegString[2].trim());
    	    		}
    	    		if(Header.PosNeg>0 && Header.PosNum>0)
    	    			break;
    	    	}
    	    }
    	    in.close();    		
    	}catch (IOException e) {
    		e.printStackTrace();
    	}      	
    	
    }
    
    // sort and write to a file;
    public void ReadAllWriteOne(String wirtePath) throws IOException{
    	
    	this.initiate(wirtePath);
    	int ActualNumberAttr=0;
    	
    	for(int i=0; i< Header.NumAttr; i++) {
    		
    		// clear the ValueAll and SortComb,each loop
    		ValueAll = new StringBuffer(); 
	   		SortComb=null;
	   		this.CorSNum=0;
	   		//get the attribute index
	   		int CurrentIndex = Header.AttributeIndex.get(i); 
	   		
	   		//save the filepath of a specific path
	   		FileSystem fsread =FileSystem.get(URI.create(this.filepath),conf);
	   		FileStatus [] resfiles = fsread.listStatus(new Path(this.filepath));  
	   		if (CurrentIndex==Header.LableIndex) {
	   			
		   		for(int j=0; j< resfiles.length; j++) {		   			
		   			this.Labeldealwith(resfiles[j].getPath().toString());
		   			if(Header.PosNeg>0 && Header.PosNum >0) 
		   				break;
		   		}
		   		Header.NegNum = Header.PosNeg - Header.PosNum;
		   		continue;	   			
	   		} 	  			
	   		
	   		// combine all the value pairs to a StringBuffer ValueAll
	   		for(int j=0; j< resfiles.length; j++) {
	   			this.ReadEachFile(resfiles[j].getPath().toString(),CurrentIndex);
	   		}
	   		// if the currentIndex has no value ,it will skip;
	   		if (ValueAll.length()==0)  {
	   			System.out.printf("the %d index has no value! Current AttrIndex is %d\n",i+1,CurrentIndex);
	   			continue;
	   		}
	   		// get the actual Attribute which has values
	   		ActualNumberAttr+=1;
	   		String CurrentClass = Header.AttributeClass.get(CurrentIndex-1);
	   		// if the class is C or S, don't need sorting or divided ,just write it down;
	   		if(CurrentClass.equals(Header.C) || 
	   				CurrentClass.equals(Header.S)) {
		   		out.write(Integer.toString(CurrentIndex));
		   		out.write("," + ValueAll.toString().trim().replace("\t", ","));
		   		out.write("," +Integer.toString(this.CorSNum));
		   		out.newLine();
		   		System.out.printf("the %d sorting and saving is completed ! Current AttrIndex is %d !",i+1,CurrentIndex);
		   		System.out.printf(" CurrentClass is %s ! And the total Number is %d !\n", CurrentClass,this.CorSNum);
		   		continue;
	   		}
	   		//segment ValueAll to ValuePair
	   		String [] ValuePair = ValueAll.toString().split("\\s+");
	   		// clear ValueAll;
	   		ValueAll=null;	   		
	   		//construct SortComb for sorting
	   		SortComb= new SortClass[ValuePair.length];
	   		int NumValue=0;   //has no means,just used for loop
	   		for(String eachpair: ValuePair) {
	   			SortComb[NumValue]=new SortClass(eachpair);
	   			NumValue+=1;
	   		}
	   		//clear the ValuePair
	   		ValuePair = null;
	   		//sorting
	   		Arrays.sort(SortComb,new ValueComparator());
	   		
	   		// read all the valuepair to a StringBuffer
	   		StringBuffer SortedValuePair = new StringBuffer();
	   		for( int Numpair=0; Numpair<NumValue;Numpair++) {
	   			SortedValuePair.append(SortComb[Numpair].getValuePair() + ",");
	   		}	   		
	   		SortedValuePair.deleteCharAt(SortedValuePair.length()-1);
	   		SortComb=null;
	   		//deal with ND N TD, dicrete them and write them down;
	   		DiffClassDeal differentClass = new DiffClassDeal(SortedValuePair.toString(),CurrentClass,CurrentIndex);	
	   		
	   		out.write(Integer.toString(CurrentIndex) + ",");
	   		out.write(differentClass.DealWithDiffClass());

	   		System.out.printf("the %d sorting and saving is completed ! Current AttrIndex is %d !",i+1,CurrentIndex);
	   		System.out.printf(" CurrentClass is %s ! And the total Number is %d !\n", CurrentClass,this.CorSNum);
	   		out.newLine();
	   		out.flush();
    	}
    	out.close();
    	Header.NumAttr = ActualNumberAttr;
    }
    


}
