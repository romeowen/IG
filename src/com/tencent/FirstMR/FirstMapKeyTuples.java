
package com.tencent.FirstMR;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 *@Package: com.tencent.FeatureSelection
 *@Title: FirstMap.java
 *@author: wenshifeng
 *@date: 2014-7-30ÉÏÎç10:59:26
 *@discription: construct the MapKey, which contains index and value.
 */


public class FirstMapKeyTuples implements Writable, WritableComparable<FirstMapKeyTuples>{
			
	public int attributeIndex;
	
	public String attributeValue;	// here didn't adopt double ,because it may be a string.
	
	//@Override  copy is not a override method.
	public FirstMapKeyTuples copy() {
		FirstMapKeyTuples tuples = new FirstMapKeyTuples();
		
		tuples.attributeIndex = attributeIndex;
		tuples.attributeValue = attributeValue;
		return tuples;
	}
	
	//  the methods below are all belonged to WritableComparable. among the first two belong to writable,too.
	@Override
	public void write(DataOutput out) throws IOException {		
		out.writeInt(attributeIndex);
		out.writeUTF(attributeValue);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		attributeIndex = in.readInt();
		attributeValue = in.readUTF();
	}
	
	@Override
	public int compareTo(FirstMapKeyTuples arg0) {

		if (this.attributeIndex > arg0.attributeIndex) {
			return 1;
		} else if (this.attributeIndex < arg0.attributeIndex) {
			return -1;
		}

		int result = this.attributeValue.compareTo(arg0.attributeValue);
		if (result>0) {
			return 1;
		}
		else if(result<0)  {
			return -1;
		}
		
		return 0;
	}
	
	@Override  // maybe yi chu ,please be carefule
	public int hashCode() {

		int result = 17;	
		long toint=0;
		result = 37 * result + (int) attributeIndex;
		result = 37 * result + (int) (toint ^ (toint >>> 32));
		result = 37 * result + attributeValue.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return attributeIndex + "," + attributeValue+",";
	}


	@Override
	public boolean equals(Object o) {

		if (!(o instanceof FirstMapKeyTuples))
			return false;

		FirstMapKeyTuples tuples = (FirstMapKeyTuples) o;

		return 
				tuples.attributeIndex == attributeIndex		
				&& tuples.attributeValue.equals(attributeValue);
	}
}
	
