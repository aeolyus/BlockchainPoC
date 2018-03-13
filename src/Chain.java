import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
/**
 * @author aeolyus
 *
 */
public class Chain{
	
	public static ArrayList<Block> blockchain=new ArrayList<Block>();
	public static HashMap<String,TransactionOutput> UTXOs=new HashMap<String,TransactionOutput>();//list of all unspent transactions
	public static int difficulty=5;
	public static float minimumTransaction=0.1f;
	public static Wallet walletA,walletB;

	public static Boolean isChainValid(){
		Block currentBlock,previousBlock;

		for(int i=1;i<blockchain.size();i++){
			currentBlock=blockchain.get(i);
			previousBlock=blockchain.get(i-1);
			if(!currentBlock.hash.equals(currentBlock.calculateHash())){//compare registered and calculated hashes
				System.out.println("Current hashes not equal");
				return false;
			}
			if(!previousBlock.hash.equals(currentBlock.previousHash)){//compare previous and registered hash
				System.out.println("Previous hashes not equal");
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args){//testing
		Security.addProvider(new BouncyCastleProvider());//bouncycastle as security provider
		walletA=new Wallet();
		walletB=new Wallet();
		
		System.out.println("Private and public keys:");
		System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
		System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
		
		Transaction transaction=new Transaction(walletA.publicKey,walletB.publicKey,5,null);
		transaction.generateSignature(walletA.privateKey);
		
		System.out.println("Is signature verified");
		System.out.println(transaction.verifySignature());
	}
}
