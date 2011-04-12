/**
 * User: Guy J. Murphy
 * Date: Mar 23, 2010
 * Time: 4:08:49 PM
 * File: TopicTimestampComparer.java
 */
package acumen.map.model;

import java.util.Comparator;

import acumen.util.Is;

/**
 * @author Guy J. Murphy
 */
public class NodeTimestampComparator implements Comparator<INode> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(INode n1, INode n2) {
		String n1_timestamp, n2_timestamp;
		n1_timestamp = n1.getMeta().get("timestamp");
		n2_timestamp = n2.getMeta().get("timestamp");
				
		if (Is.NotNullOrEmpty(n1_timestamp)) {
			if (Is.NullOrEmpty(n2_timestamp)) {
				return -1; // reverse order
			}
		} else {
			if (Is.NotNullOrEmpty(n2_timestamp)) {
				return 1; // reverse order
			} else {
				return 0;
			}
		}
		// we know if we get here both have a value
		Long ts1 = Long.parseLong(n1_timestamp);
		Long ts2 = Long.parseLong(n2_timestamp);
		
		return ts2.compareTo(ts1); // this is reverse order
	}

	

}
