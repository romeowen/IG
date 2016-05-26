package com.tencent.FilesRead;

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
 *@Package: com.tencent.FilesRead
 *@Title: TwoPartsSeg.java
 *@author: wenshifeng
 *@date: 2014-8-21ÏÂÎç9:24:08
 *@description: this class works for combine the FilesRead and CriteriaCombine class output together; 
 */
public class CombineAll {

	private Configuration conf = new Configuration();	

	// write the ranking result in the ranking text file.
    private FileSystem fswrite = null;    //feature
    private FSDataOutputStream fout = null;  //feature
    private BufferedWriter out = null;  //feature

   // initiate the class
    public CombineAll(Configuration conf) {
    	this.conf = conf;    	
       }  
    
    /*initiate the output handle for writing*/
    public void Initiate ()  {
    	
    	String location= Header.output + Header.Finalresult;

    	try {
        	fswrite = FileSystem.get(URI.create(location),conf);
        	fout = fswrite.create(new Path(location));
        	out = new BufferedWriter(new OutputStreamWriter(fout,"UTF-8"));

    	}catch(IOException e) {
    		e.printStackTrace();
    	}

    } 
    
    /* read file and write it in another file,index is used for universal use of multi-output or single-output*/
    public void ResultRead(String CurrentPath) {
    	
    	try{
    		FileSystem fs =FileSystem.get(URI.create(CurrentPath),conf);    
    		FSDataInputStream fin =fs.open(new Path(CurrentPath));
    		BufferedReader in = new BufferedReader(new InputStreamReader(fin,"UTF-8")); 
    		String tempString=null;
    	    while ((tempString = in.readLine()) != null) {
    	    	out.write(tempString);
    	    	out.newLine();
    	    }
    	    this.out.newLine();
    	    in.close();    		
    	}catch (IOException e) {
    		e.printStackTrace();
    	}   
    }
    
    public void CombineFinal() throws IOException{
    	
    	this.Initiate();
    	this.ResultRead(Header.output + Header.Tempresult);
    	this.out.newLine();
    	this.out.newLine();
    	for(String val : Header.kinds) {
    		this.ResultRead(Header.output + val); 	
    		this.out.newLine();    		
    	}
    	this.out.flush();
    	this.out.close();
    }
    

}
