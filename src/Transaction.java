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
	public ArrayList<TransactionOutput> outputs=new ArrayList<TransactionOutput>();

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
	
	public float getInputsValue(){
		float total=0;
		for(TransactionInput i:inputs){
			if(i.UTXO==null)
				continue;//if transaction can't be found, skip
			total+=i.UTXO.value;
		}
		return total;
	}
	
	public float getOutputsValue(){
		float total=0;
		for(TransactionOutput o:outputs){
			total+=o.value;
		}
		return total;
	}
	
	public boolean processTransaction(){
		if(verifySignature()==false){
			System.out.println("#Transaction signature failed to verify");
			return false;
		}
		for(TransactionInput i:inputs){
			i.UTXO=Chain.UTXOs.get(i.transactionOutputId);
		}
		if(getInputsValue()<Chain.minimumTransaction){
			System.out.println("#Transaction inputs too smol"+getInputsValue());
			return false;
		}
		float leftOver=getInputsValue()-value;
		transactionId=calculateHash();
		outputs.add(new TransactionOutput(this.recipient,value,transactionId));//send value to recipient
		outputs.add(new TransactionOutput(this.sender,leftOver,transactionId));//send left over change back to sender
		for(TransactionOutput o:outputs){
			Chain.UTXOs.put(o.id,o);
		}
		for(TransactionInput i:inputs){
			if(i.UTXO==null)
				continue;//if transactions can't be found, skip
			Chain.UTXOs.remove(i.UTXO.id);
		}
		return true;
	}
}
