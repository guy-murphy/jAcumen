/**
 * User: Guy J. Murphy
 * Date: Jan 14, 2010
 * Time: 2:22:57 PM
 * File: StoreType.java
 */
package acumen.data;

/**
 * Denotes the basic type of the backing store; whethere
 * the store is client server based, requiring a connection,
 * or whether it is memory resident.
 * 
 * @author Guy J. Murphy
 */
public enum StoreType {
    Connecting,
    Resident
}
