package com.tencent.FilesRead;

import java.util.ArrayList;
import java.net.URI;
import java.util.Arrays;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.tencent.Header.Header;
/**
 *@Package: com.tencent.FilesRead
 *@Title: FilesRead.java
 *@author: wenshifeng
 *@date: 2014-8-6ÏÂÎç4:25:38
 *@discription: this class is used for read all of the third mapreduce results to four different folds and files;
 */
public class FilesRead {
	
	private String filepath = null;                               // the path of file
	private Configuration conf = new Configuration();	
	private ResultSortClass []  SortComb = null;
	private int eachAttr;
	
	// write the ranking result in the ranking text file.    
	private FileSystem [] fswrite= new FileSystem[2];
	private FSDataOutputStream [] fout = new FSDataOutputStream[2];
	private BufferedWriter [] out = new BufferedWriter[2];

	
	// initiate the class
    public FilesRead(String filePath, Configuration conf) throws IOException {
    	
    	this.conf = conf;    		
    	this.filepath = filePath;
   		SortComb=new ResultSortClass[Header.NumAttr];
   		
    }  
    
    /*initiate the output handle for writing*/
    public void Initiate (int index)  {

    	try {
    		
        	for(int i=0; i<2;i++) {        		
            	fswrite[i] = FileSystem.get(URI.create(Header.output+Header.kinds[2*index+i]),conf); 
            	fout[i] = fswrite[i].create(new Path((Header.output+Header.kinds[2*index+i])));
            	out[i] = new BufferedWriter(new OutputStreamWriter(fout[i] ,"UTF-8"));        		
        	}
        	        	
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }    	

    /* read file and write it in another file,index is used for universal use of multi-output or single-output*/
    public void SingleResultRead(String CurrentPath,int index) {
    	
    	try{
    		FileSystem fs =FileSystem.get(URI.create(CurrentPath),conf);    
    		FSDataInputStream fin =fs.open(new Path(CurrentPath));
    		BufferedReader in = new BufferedReader(new InputStreamReader(fin,"UTF-8")); 
    		String tempString=null;
    	    while ((tempString = in.readLine()) != null) {
    	    	//out.write(tempString);
    	    	//out.newLine();
    	    	SortComb[eachAttr] = new ResultSortClass();
    	    	SortComb[eachAttr].ResultClass(tempString,index);
    	    	eachAttr +=1;
    	    }
    	    in.close();    		
    	}catch (IOException e) {
    		e.printStackTrace();
    	}   
    }
    
    /* read the files under certain path,and partition them in only four files*/   
   public void MultiResultComPart() {
	   

	   for(int k=0; k<Header.NumCriteria; k++) {		   

		   	this.Initiate(k);
		   	this.eachAttr=0;

		   	try{
			   	out[0].write("Detailed Information");
			   	out[0].newLine();
			   	out[0].write("Rank"+"\t");
			   	out[0].write("Feature"+"\t");
			   	out[0].write(Header.Criterias[k] + "\t");
			   	out[0].write("SplitValue" +  "\t");
			   	out[0].write("Impression(L)" + "\t");
			   	out[0].write("Click(L)" + "\t");
			   	out[0].write("CTR(L)" + "\t");
			   	out[0].write("Impression(R)" + "\t");
			   	out[0].write("Click(R)" + "\t");
			   	out[0].write("CTR(R)");
			   	out[0].newLine();
			   	
		   		FileSystem fsread =FileSystem.get(URI.create(this.filepath),conf);
		   		FileStatus [] resfiles = fsread.listStatus(new Path(this.filepath));
		   		
		   		for(int i=0; i< resfiles.length; i++) {
		   			this.SingleResultRead(resfiles[i].getPath().toString(),k);
		   		}
		   		Arrays.sort(SortComb,new ValueComparator()); //sort the combed result  
		   		
		   		if( Header.Criterias[k].equals("GINI") ) {
		       		for(int LL=0; LL<SortComb.length;LL++) {
		       			if(SortComb[LL].getclass().equals(Header.N)) {
		       				out[0].write(Integer.toString(LL+1)+ "\t");
		       				out[0].write(SortComb[LL].getN());
		       				out[0].newLine();	
		       			} else {
		       				out[1].write("Rank" + "\t");
		       				out[1].write("Feature" + "\t");
		       				out[1].write(Header.Criterias[k] + "\t");
		       				out[1].newLine();
		       				out[1].write(Integer.toString(LL+1) + "\t");
		       				out[1].write(SortComb[LL].getAtt() + "\t");
		       				out[1].newLine();
		       				out[1].write("Value" + "\t");
		       				out[1].write("Impression" + "\t");
		       				out[1].write("Click" + "\t");
		       				out[1].write("CTR");
		       				out[1].newLine();
		       				String EachThreshold [] = SortComb[LL].getRest().split(",+");
		       				for(int val=0; val < EachThreshold.length/4; val++) {
		       					out[1].write(EachThreshold[4*val] + "\t");
		       					out[1].write(EachThreshold[4*val+1] + "\t");
		       					out[1].write(EachThreshold[4*val+2] + "\t");
		       					out[1].write(EachThreshold[4*val+3] );
		       					out[1].newLine();
		       				}
		       				
		       			}
		       		}
		   		} else {
		       		for(int LL=SortComb.length-1; LL>=0;LL--) {
		       			if(SortComb[LL].getclass().equals(Header.N)) {
		       				out[0].write(Integer.toString(SortComb.length-LL)+ "\t");
		       				out[0].write(SortComb[LL].getN());
		       				out[0].newLine();	
		       			} else {
		       				out[1].write("Rank" + "\t");
		       				out[1].write("Feature" + "\t");
		       				out[1].write(Header.Criterias[k] + "\t");
		       				out[1].newLine();
		       				out[1].write(Integer.toString(SortComb.length-LL) + "\t");
		       				out[1].write(SortComb[LL].getAtt() + "\t");
		       				out[1].newLine();
		       				out[1].write("Value" + "\t");
		       				out[1].write("Impression" + "\t");
		       				out[1].write("Click" + "\t");
		       				out[1].write("CTR");
		       				out[1].newLine();
		       				String EachThreshold [] = SortComb[LL].getRest().split(",+");
		       				for(int val=0; val < EachThreshold.length/4; val++) {
		       					out[1].write(EachThreshold[4*val] + "\t");
		       					out[1].write(EachThreshold[4*val+1] + "\t");
		       					out[1].write(EachThreshold[4*val+2] + "\t");
		       					out[1].write(EachThreshold[4*val+3] );
		       					out[1].newLine();
		       				}
		       				
		       			}

		       		}    			
		   		}
		   		this.out[0].flush();
		   		this.out[1].flush();
		   		this.out[0].close();
		   		this.out[1].close();
		   	}catch (IOException e) {
		   		e.printStackTrace();
		   	}
			
		}
   }
	   
	      
	   	
	
}


        	

   

   
    	      