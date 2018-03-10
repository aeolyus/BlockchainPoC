import java.util.Date;

/**
 * @author aeolyus
 *
 */
public class Block {

	public String hash, previousHash;
	private String data;
	private long timeStamp;//milliseconds since 1/1/1970

	public Block(String data, String previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
	}
}
