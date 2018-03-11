import java.security.PublicKey;
import java.util.ArrayList;

/**
 * @author aeolyus
 *
 */
public class Transaction {

	public String transactionId;// hash of transaction
	public PublicKey sender;
	public PublicKey recipient;
	public float value;
	public byte[] signature;

	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

	private static int sequence = 0;// count of how many transactions have been generated

	public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.recipient = to;
		this.value = value;
		this.inputs = inputs;
	}

	private String calculateHash() {
		sequence++;// increase sequence to avoid two identical transactions having the same hash
		return StringUtil.applySha256(StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)
				+ Float.toString(value) + sequence);
	}
}
