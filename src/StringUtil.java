import java.security.MessageDigest;

/**
 * @author aeolyus
 *
 */
public class StringUtil {

	/**
	 * applies sha256 to input
	 * @param input data to hash
	 * @return sha256 generated signature of input
	 */
	public static String applySha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
