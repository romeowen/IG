package com.tencent.FilesRead;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.tencent.Header.Header;

/**
 *@Package: com.tencent.FilesRead
 *@Title: AttributesGet.java
 *@author: wenshifeng
 *@date: 2014-8-18����3:35:19
 */
public class AttributesGet {
	
	private Configuration conf =new Configuration();
	private String filepath =null;
	private int Len = 0;
	private StringBuffer AttributeClass = new StringBuffer();
	
    public AttributesGet(String filePath, Configuration conf) {
    	
    	this.conf = conf;    		
    	this.filepath = filePath;
    } 
    
    // return the class attribute
    public String getAttributeClass() {
    	return AttributeClass.toString();
    }
    
    
    // read from the AttributeName file and store them in an ArrayList.
    public void getAttributes()  {
            	
    	try {
    		
    		FileSystem fs =FileSystem.get(URI.create(this.filepath),conf);    
    		FSDataInputStream fin =fs.open(new Path(this.filepath));
    		BufferedReader in = new BufferedReader(new InputStreamReader(fin,"UTF-8"));
    		String tempString=null;
    		int NUMAttribute = 0;
			while ((tempString = in.readLine()) != null) {

				if (tempString.length() != 0) {
					if (this.Len == 0) {
						this.Len += 1;
						continue;
					}
					// segment the Text String to String array;
					String[] List = tempString.split("\\s+");
					// as for the L and I class attribute, we didn't count them;
					if (!List[2].trim().equals(Header.I)) {
						NUMAttribute += 1;
						Header.AttributeIndex.add(this.Len); 
					}
					// get the column number of the label;
					if (List[2].trim().equals(Header.L)) {
						Header.LableIndex = this.Len;
					}
					// add the attr name to
					Header.AttributeName.add(List[1].trim());
					Header.AttributeClass.add(List[2].trim());
					AttributeClass.append(List[2].trim() + "\t"); // String not ArrayList

					if (List[2].trim().equals(Header.ND)) {

						Header.AttributesND.add(this.Len);
						Header.AttributesND
								.add(Integer.parseInt(List[3].trim()));

					} else if (List[2].trim().equals(Header.TD)) {
						Header.AttributeThreshold.append(Integer
								.toString(this.Len)
								+ ","
								+ List[3].trim()
								+ "_");
					} else if (List[2].trim().equals(Header.N)) {
						if (List.length == 4) {
							Header.AttributeN.add(this.Len);
							Header.AttributeN.add(Integer.parseInt(List[3]
									.trim()));
						} else if (List.length == 3) {
							Header.AttributeN.add(this.Len);
							Header.AttributeN.add(Header.Numdivide);
						} else {
							System.out
									.print("attribute N length is not right.");
							System.exit(1);
						}
					}
					this.Len += 1;
				}
			}
			if (Header.AttributeThreshold.length() != 0) {
				Header.AttributeThreshold
						.deleteCharAt(Header.AttributeThreshold.length() - 1);
			}
			Header.NumAttr = NUMAttribute;
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
