import java.util.ArrayList;
import java.util.Date;

/**
 * @author aeolyus
 *
 */
public class Block{

	public String hash,previousHash,merkleRoot;
	public ArrayList<Transaction> transactions=new ArrayList<Transaction>();
	private String data;
	private long timeStamp;//milliseconds since 1/1/1970
	private int nonce;

	public Block(String data,String previousHash){
		this.data=data;
		this.previousHash=previousHash;
		this.timeStamp=new Date().getTime();
		this.hash=calculateHash();
	}

	public String calculateHash(){
		String calculatedhash=StringUtil.applySha256(previousHash+Long.toString(timeStamp)+Integer.toString(nonce)+merkleRoot+data);
		return calculatedhash;
	}

	public void mineBlock(int difficulty){
		merkleRoot=StringUtil.getMerkleRoot(transactions);
		String target=new String(new char[difficulty]).replace('\0','0');
		while(!hash.substring(0, difficulty).equals(target)){
			nonce++;
			hash=calculateHash();
		}
		System.out.println("Block mined!:"+hash);
	}
}
