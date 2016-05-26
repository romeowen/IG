package com.tencent.FilesRead;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.tencent.Header.Header;

/**
 *@Package: com.tencent.FilesRead
 *@Title: CriteriaCombine.java
 *@author: wenshifeng
 *@date: 2014-8-21ÏÂÎç3:59:44
 *@discription: this class is designed for combine all of the criteria values to a file;
 */
public class CriteriaCombine {
	
	private String filepath = null;                               // the path of file
	private Configuration conf = new Configuration();	
	private ResultSortClass []  SortCombIG = new ResultSortClass[Header.NumAttr];
	private ResultSortClass []  SortCombGI = new ResultSortClass[Header.NumAttr];
	private ResultSortClass []  SortCombMU = new ResultSortClass[Header.NumAttr];
	private ResultSortClass []  SortCombSU = new ResultSortClass[Header.NumAttr];
	
	
	private int eachAttr= 0;
	// write the ranking result in the ranking text file.
    private FileSystem fswrite = null;    //feature
    private FSDataOutputStream fout = null;  //feature
    private BufferedWriter out = null;  //feature

    
	// initiate the class
    public CriteriaCombine(String filePath, Configuration conf) {
    	
    	this.conf = conf;    		
    	this.filepath = filePath;
    }  
    
    /*initiate the output handle for writing*/
    public void Initiate (String location)  {

    	try {
        	fswrite = FileSystem.get(URI.create(location),conf);
        	fout = fswrite.create(new Path(location));
        	out = new BufferedWriter(new OutputStreamWriter(fout,"UTF-8"));
        	out.write("Summary Information");
        	out.newLine();
        	out.write("Rank" + "\t");
        	out.write("Feature" + "\t");
        	out.write("InfoGainRatio" + "\t");
        	out.write("Feature" + "\t");
        	out.write("GiniIndex" + "\t");
        	out.write("Feature" + "\t");
        	out.write("MutualInformation" + "\t");
        	out.write("Feature" + "\t");
        	out.write("SymmetryUncertainty");
        	out.newLine();
        	
    	}catch(IOException e) {
    		e.printStackTrace();
    	}

    } 
    
    /* read file and write it in another file,index is used for universal use of multi-output or single-output*/
    public void SingleResultRead(String CurrentPath) {
    	
    	try{
    		FileSystem fs =FileSystem.get(URI.create(CurrentPath),conf);    
    		FSDataInputStream fin =fs.open(new Path(CurrentPath));
    		BufferedReader in = new BufferedReader(new InputStreamReader(fin,"UTF-8")); 
    		String tempString=null;
    	    while ((tempString = in.readLine()) != null) {
    	    	SortCombIG[eachAttr] = new ResultSortClass();
    	    	SortCombIG[eachAttr].ResultClass(tempString,0);
    	    	SortCombGI[eachAttr] = new ResultSortClass();
    	    	SortCombGI[eachAttr].ResultClass(tempString,1);
    	    	SortCombMU[eachAttr] = new ResultSortClass();
    	    	SortCombMU[eachAttr].ResultClass(tempString,2);
    	    	SortCombSU[eachAttr] = new ResultSortClass();
    	    	SortCombSU[eachAttr].ResultClass(tempString,3);
    	    	eachAttr +=1;
    	    }
    	    in.close();    		
    	}catch (IOException e) {
    		e.printStackTrace();
    	}   
    }
    
    /* read the files under certain path,and partition them in only three files*/   
   public void MultiResultCom() {
	   
	   this.Initiate(Header.output + Header.Tempresult);
	   	try{
	   		FileSystem fsread =FileSystem.get(URI.create(this.filepath),conf);
	   		FileStatus [] resfiles = fsread.listStatus(new Path(this.filepath));
	   		
	   		for(int i=0; i< resfiles.length; i++) {
	   			this.SingleResultRead(resfiles[i].getPath().toString());
	   		}
	   		//FileDivision Division = new FileDivision(conf);
	   				   		
	   		Arrays.sort(SortCombIG,new ValueComparator()); //sort the combed result
	   		Arrays.sort(SortCombGI,new ValueComparator()); //sort the combed result 
	   		Arrays.sort(SortCombMU,new ValueComparator()); //sort the combed result 
	   		Arrays.sort(SortCombSU,new ValueComparator()); //sort the combed result 
       		for(int LL=Header.NumAttr-1; LL>=0;LL--) {
    			this.out.write(Integer.toString(Header.NumAttr-LL));
    			this.out.write("\t");	        			
       			this.out.write(SortCombIG[LL].getAtt());
    			this.out.write("\t");	
       			this.out.write(SortCombGI[Header.NumAttr-LL-1].getAtt());
    			this.out.write("\t");
       			this.out.write(SortCombMU[LL].getAtt());
    			this.out.write("\t");
       			this.out.write(SortCombSU[LL].getAtt());		       			
       			this.out.newLine();       			      			
       		}
       		this.out.flush();
	   		this.out.close();

	   	}catch (IOException e) {
	   		e.printStackTrace();
	   	}
	   	   	
	}

}
