package GroupF2.gobang.pojo;

import java.io.Serializable;

/**
 * Represents an IP address record for a user account. This class is used to store
 * information about user IP addresses including whether the address should be remembered.
 * This class implements Serializable to allow its instances to be serialized for storage
 * or transmission.
 *
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class Address implements Serializable {
	private static final long serialVersionUID = 1L; // For ensuring version compatibility during serialization

	private int id;          // Unique identifier for the address record
	private String account;  // Account identifier associated with this address
	private String address;  // The IP address as a string
	private int remember;    // Flag to determine whether the address should be remembered (1) or not (0)

	/**
	 * Returns the 'remember' flag status.
	 * @return the remember status as an integer (1 for true, 0 for false)
	 */
	public int getRemember() {
		return remember;
	}

	/**
	 * Returns the account identifier associated with this IP address.
	 * @return the account ID as a String
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Sets the account identifier for this IP address.
	 * @param account the new account ID as a String
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * Returns the IP address.
	 * @return the IP address as a String
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the IP address.
	 * @param address the new IP address as a String
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Provides a string representation of this Address instance, useful for logging and debugging.
	 * @return a string representation of the Address object, detailing all fields.
	 */
	@Override
	public String toString() {
		return "Address [id=" + id + ", account=" + account + ", address=" + address + ", remember=" + remember + "]";
	}
}
