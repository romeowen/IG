package com.tencent.Delete;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.tencent.Header.Header;

/**
 *@Package: com.tencent.Delete
 *@Title: ClearFiles.java
 *@author: wenshifeng
 *@date: 2014-8-29ÏÂÎç5:28:40
 */
public class ClearFiles {
	
	public void ClearAll(Configuration conf) throws Exception{	
		// this section is used for delete useless files		
		FileSystem DeleteOthers = FileSystem.get(conf);
		for(String val : Header.Criterias) {			
			DeleteOthers.delete(new Path(Header.output + val),true);			
		}
		for (String val : Header.location) {
			DeleteOthers.delete(new Path(Header.output + val) , true);
		}
		DeleteOthers.delete(new Path(Header.output + Header.Tempresult),true);		
	}

}
