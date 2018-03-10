import java.util.ArrayList;

import com.google.gson.GsonBuilder;

/**
 * @author aeolyus
 *
 */
public class Chain {

	public static ArrayList<Block> blockchain = new ArrayList<Block>();

	public static void main(String[] args) {// testing
		blockchain.add(new Block("it me the first block", "0"));
		blockchain.add(new Block("second block the best block", blockchain.get(blockchain.size() - 1).hash));
		blockchain.add(new Block("third block is alright i guess", blockchain.get(blockchain.size() - 2).hash));

		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
		System.out.println(blockchainJson);
	}
}
