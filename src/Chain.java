import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.gson.GsonBuilder;
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
	public static Transaction genesisTransaction;

	public static Boolean isChainValid(){
		Block currentBlock,previousBlock;
		String hashTarget=new String(new char[difficulty]).replace('\0','0');
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>();//temp working list of unspent transactions at a given block state
		tempUTXOs.put(genesisTransaction.outputs.get(0).id,genesisTransaction.outputs.get(0));

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
			if(!currentBlock.hash.substring(0,difficulty).equals(hashTarget)){
				System.out.println("#This block hasn't been mined");
				return false;
			}
			
			TransactionOutput tempOutput;
			for(int t=0;t<currentBlock.transactions.size();t++){
				Transaction currentTransaction=currentBlock.transactions.get(t);
				if(!currentTransaction.verifySignature()){
					System.out.println("#Signature on transaction("+t+") is invalid");
					return false;
				}
				if(currentTransaction.getInputsValue()!=currentTransaction.getOutputsValue()){
					System.out.println("#Inputs are not equal to outputs on transaction("+t+")");
					return false;
				}
				for(TransactionInput input:currentTransaction.inputs){
					tempOutput=tempUTXOs.get(input.transactionOutputId);
					if(tempOutput==null){
						System.out.println("#Referenced input on transaction("+t+") is missing");
						return false;
					}
					if(input.UTXO.value!=tempOutput.value){
						System.out.println("#Referenced input transaction("+t+") value is invalid");
						return false;
					}
					tempUTXOs.remove(input.transactionOutputId);
				}
				for(TransactionOutput output:currentTransaction.outputs){
					tempUTXOs.put(output.id,output);
				}
				if(currentTransaction.outputs.get(0).recipient!=currentTransaction.recipient){
					System.out.println("#Transaction("+t+") output recipient is not who it should be");
					return false;
				}
				if(currentTransaction.outputs.get(1).recipient!=currentTransaction.sender){
					System.out.println("#Transaction("+t+") output 'change' is not sender");
					return false;
				}
			}
		}
		System.out.println("Blockchain is valid");
		return true;
	}
	
	public static void addBlock(Block newBlock){
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}

	public static void main(String[] args){//testing
		Security.addProvider(new BouncyCastleProvider());//bouncycastle as security provider
		
		walletA=new Wallet();
		walletB=new Wallet();
		Wallet coinbase=new Wallet();
		
		genesisTransaction=new Transaction(coinbase.publicKey,walletA.publicKey,100f,null);
		genesisTransaction.generateSignature(coinbase.privateKey);//manually sign genesis transaction
		genesisTransaction.transactionId="0";//manually set transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient,genesisTransaction.value,genesisTransaction.transactionId));//manually add the transactions output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));//important to store first transaction in UTXOs list
		
		System.out.println("Creating and mining genesis block...");
		Block genesis=new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		
		Block block1 = new Block(genesis.hash);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletA is attempting to send funds (75) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 75f));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block2 = new Block(block1.hash);
		System.out.println("\nWalletA attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block3 = new Block(block2.hash);
		System.out.println("\nWalletB is attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20f));
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		//h4ck3rm4n trying to change value to get rich
//		System.out.println("\nH4ck3rm4n is attempting to alter the last transaction");
//		blockchain.get(1).transactions.get(0).value=500000;
//		blockchain.get(1).mineBlock(difficulty);
//		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		isChainValid();
		
//		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
//		System.out.println("\nThe block chain: ");
//		System.out.println(blockchainJson);

	}
}
