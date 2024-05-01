package GroupF2.gobang.pojo;

import java.io.Serializable;

/**
 * Represents an online player's status and information. This class includes details about
 * the player's unique identifier, account name, network address, and their current status.
 * This class is designed to be serializable to allow easy storage and transmission of player
 * information in networked applications.
 *
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class Sinfo implements Serializable {
	private static final long serialVersionUID = 1L; // Ensures version compatibility during serialization.

	private int id;           // Unique identifier for the player.
	private String account;   // Account name or username of the player.
	private String address;   // Network address of the player, such as an IP address.
	private int status;       // Status of the player, possibly encoded as integers (e.g., 1 for online, 0 for offline).

	/**
	 * Returns the current status of the player.
	 * @return the status code as an integer.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Returns the account name of the player.
	 * @return the account name as a String.
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Sets the account name for this player.
	 * @param account the new account name as a String.
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * Returns the network address associated with the player.
	 * @return the address as a String.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the network address for this player.
	 * @param address the new address as a String, typically an IP address.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Provides a string representation of this Sinfo instance, useful for logging and debugging.
	 * This method outputs the id, account, address, and status of the player.
	 * @return a string representation of the Sinfo object, detailing all fields.
	 */
	@Override
	public String toString() {
		return "Sinfo [id=" + id + ", account=" + account + ", address=" + address + ", status=" + status + "]";
	}
}
