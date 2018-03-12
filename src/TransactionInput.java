
/**
 * @author aeolyus
 *
 */
public class TransactionInput{
	
	public String transactionOutputId;//reference to TransactionOutputs transactionId
	public TransactionOutputs UTXO;//contains the unspent transaction output
	
	public TransactionInput(String transactionOutputId){
		this.transactionOutputId=transactionOutputId;
	}
}
