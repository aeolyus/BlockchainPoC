import java.security.PublicKey;

/**
 * @author aeolyus
 *
 */
public class TransactionOutputs{
	public String id;
	public PublicKey recipient;//new owner
	public float value;//amount of coins
	public String parentTransactionId;//id of transaction this output was created in
	
	public TransactionOutputs(PublicKey recipient,float value,String parentTransactionId){
		this.recipient=recipient;
		this.value=value;
		this.parentTransactionId=parentTransactionId;
		this.id=StringUtil.applySha256(StringUtil.getStringFromKey(recipient)+Float.toString(value)+parentTransactionId);
	}
	
	public boolean isMine(PublicKey publicKey){
		return(publicKey==recipient);
	}
}
