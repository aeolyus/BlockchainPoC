import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * @author aeolyus
 *
 */
public class Transaction {

	public String transactionId;//hash of transaction
	public PublicKey sender;
	public PublicKey recipient;
	public float value;
	public byte[] signature;

	public ArrayList<TransactionInput> inputs=new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutputs> outputs=new ArrayList<TransactionOutputs>();

	private static int sequence=0;//count of how many transactions have been generated

	public Transaction(PublicKey from,PublicKey to,float value,ArrayList<TransactionInput> inputs){
		this.sender=from;
		this.recipient=to;
		this.value=value;
		this.inputs=inputs;
	}

	private String calculateHash(){
		sequence++;//increase sequence to avoid two identical transactions having the same hash
		return StringUtil.applySha256(StringUtil.getStringFromKey(sender)+StringUtil.getStringFromKey(recipient)+Float.toString(value)+sequence);
	}
	
	public void generateSignature(PrivateKey privateKey){
		String data=StringUtil.getStringFromKey(sender)+StringUtil.getStringFromKey(recipient)+Float.toString(value);
		signature=StringUtil.applyECDSASig(privateKey,data);
	}
	
	public boolean verifySignature(){
		String data=StringUtil.getStringFromKey(sender)+StringUtil.getStringFromKey(recipient)+Float.toString(value);
		return StringUtil.verifyECDSASig(sender,data,signature);
	}
}
