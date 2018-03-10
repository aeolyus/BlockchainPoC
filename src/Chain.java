import java.util.ArrayList;

import com.google.gson.GsonBuilder;

/**
 * @author aeolyus
 *
 */
public class Chain {

	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static int difficulty=5;

	public static Boolean isChainValid() {
		Block currentBlock, previousBlock;

		for (int i = 1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);
			if (!currentBlock.hash.equals(currentBlock.calculateHash())) {// compare registered and calculated hashes
				System.out.println("Current hashes not equal");
				return false;
			}
			if (!previousBlock.hash.equals(currentBlock.previousHash)) {// compare previous and registered hash
				System.out.println("Previous hashes not equal");
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {// testing
		blockchain.add(new Block("it me the genesis block", "0"));
		System.out.println("Mining block 1...");
		blockchain.get(0).mineBlock(difficulty);
		
		blockchain.add(new Block("second block the best block", blockchain.get(blockchain.size() - 1).hash));
		System.out.println("Mining block 2...");
		blockchain.get(1).mineBlock(difficulty);
		
		blockchain.add(new Block("third block is alright i guess", blockchain.get(blockchain.size() - 1).hash));
		System.out.println("Mining block 3...");
		blockchain.get(2).mineBlock(difficulty);
		
		System.out.println("\nBlockchain is valid: " + isChainValid());
		
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
		System.out.println("\nThe blockchain: ");
		System.out.println(blockchainJson);
	}
}
