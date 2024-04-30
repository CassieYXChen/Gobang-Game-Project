package GroupF2.gobang.base;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for generating MD5 hashes. This class provides methods to
 * compute MD5 hashes of input strings and return the result either as a 16-character
 * or 32-character hexadecimal number.
 */
public class EncryptionMD5 {
	/**
	 * Generates a 16-character hexadecimal MD5 hash of the input string.
	 *
	 * @param input The string to hash.
	 * @return A 16-character hexadecimal hash, or an empty string if input is null or empty.
	 */
	public static String digest16(String input) {
		return digest(input, 16);
	}

	/**
	 * Generates a 32-character hexadecimal MD5 hash of the input string.
	 *
	 * @param input The string to hash.
	 * @return A 32-character hexadecimal hash, or an empty string if input is null or empty.
	 */
	public static String digest32(String input) {
		return digest(input, 32);
	}

	/**
	 * Computes an MD5 hash of the provided input string and returns a part or full
	 * hash based on the specified range. The function returns either the full 32-character
	 * hash or a substring of it forming a 16-character hash.
	 *
	 * @param input The string to hash.
	 * @param range Specifies the length of the hash to return: 16 or 32 characters.
	 * @return The specified range of MD5 hash as a hexadecimal string, or an empty string on error.
	 */
	private static String digest(String input, int range) {
		if (input == null || input.isEmpty()) {
			return "";
		}

		byte[] md5Bytes;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(input.getBytes(StandardCharsets.UTF_8));
			md5Bytes = md5.digest();
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Failed to get MD5 MessageDigest instance: " + e.getMessage());
			return "";
		}

		StringBuilder hexValue = new StringBuilder();
		for (byte md5Byte : md5Bytes) {
			String hex = Integer.toHexString(0xff & md5Byte);
			if (hex.length() == 1) {
				hexValue.append('0');
			}
			hexValue.append(hex);
		}

		if (range == 32) {
			return hexValue.toString();
		} else {
			return hexValue.substring(8, 24); // Returns the middle part of the hash for a 16-character result.
		}
	}
}
