import java.util.ArrayList;
import java.util.Date;

/**
 * @author aeolyus
 *
 */
public class Block{

	public String hash,previousHash,merkleRoot;
	public ArrayList<Transaction> transactions=new ArrayList<Transaction>();
	private long timeStamp;//milliseconds since 1/1/1970
	private int nonce;

	public Block(String previousHash){
		this.previousHash=previousHash;
		this.timeStamp=new Date().getTime();
		this.hash=calculateHash();
	}

	public String calculateHash(){
		String calculatedhash=StringUtil.applySha256(previousHash+Long.toString(timeStamp)+Integer.toString(nonce)+merkleRoot);
		return calculatedhash;
	}

	public void mineBlock(int difficulty){
		System.out.println("Mining block...");
		merkleRoot=StringUtil.getMerkleRoot(transactions);
		String target=new String(new char[difficulty]).replace('\0','0');
		while(!hash.substring(0, difficulty).equals(target)){
			nonce++;
			hash=calculateHash();
		}
		System.out.println("Block mined!: "+hash);
	}
	
	public boolean addTransaction(Transaction transaction){
		if(transaction==null)
			return false;
		if(previousHash!="0"){
			if(transaction.processTransaction()!=true){
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction successfully added to block");
		return true;
	}
}
