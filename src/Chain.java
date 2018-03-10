
/**
 * @author aeolyus
 *
 */
public class Chain {
	
	public static void main(String[] args) {
		Block genesisBlock=new Block("it me the first block","0");
		System.out.println("Hash for block 1:"+genesisBlock.hash);
		Block secondBlock=new Block("second block the best block",genesisBlock.hash);
		System.out.println("Hash for block 2:"+secondBlock.hash);
		Block thirdBlock=new Block("third block is alright i guess",secondBlock.hash);
		System.out.println("Hash for block 3:"+thirdBlock.hash);
	}
}
