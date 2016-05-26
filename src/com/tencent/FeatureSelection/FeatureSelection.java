package com.tencent.FeatureSelection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.tencent.Delete.ClearFiles;
import com.tencent.FilesRead.*;
import com.tencent.Header.Header;
import com.tencent.SecondRW.MRToFile;
/**
 *@Package: com.tencent.FeatureSelection
 *@Title: selection.java
 *@author: wenshifeng
 *@date: 2014-7-29����5:55:40
 *@description: This is the main function
 */

public class FeatureSelection extends Configured implements Tool {	
	
	public int run(String[] args) throws Exception {
		
		Configuration conf = getConf();		
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs(); 
		//the configuration of mapreduce and three different*/
		MapReduceConf MapReduce =new MapReduceConf(otherArgs, conf);			
		//first MR
	    MapReduce.FirstMapReduceConf();			    
	    // sort and divide the first MR result and wirte to a file
		MRToFile SortedToFile = new MRToFile(Header.output + Header.location[0],conf);
		SortedToFile.ReadAllWriteOne(Header.output + Header.location[1] + Header.SecondCombName);
		
		conf.set("PosNum",Integer.toString(Header.PosNum));
		conf.set("NegNum",Integer.toString(Header.NegNum));
		conf.set("PosNeg",Integer.toString(Header.PosNeg));
		// compute every criteria for every attribute
	    MapReduce.ThirdMapReduceConf();		    
	    
	    //Multi-read the mapreduce result files and sorted them and finlly write them to diferent folds and files
		FilesRead Attribute = new FilesRead(Header.output + Header.location[2], conf);
		Attribute.MultiResultComPart();
			
		//generate a file which write all the split criteria value to Header.Tempfeature
		CriteriaCombine combine = new CriteriaCombine(Header.output + Header.location[2], conf);
		combine.MultiResultCom();
		
		// combine all the internal result to the final file : Header.Finalfeature
		CombineAll FinalFeature = new CombineAll(conf);
		FinalFeature.CombineFinal();		
		
		// clear the other files
		ClearFiles FileDelete = new ClearFiles();
		FileDelete.ClearAll(conf);		
				
		return 0;
	}		

	public static void main(String [] args) throws Exception {		
		
		int res = ToolRunner.run(new Configuration(),new FeatureSelection(),args);
		System.exit(res);
	}
}