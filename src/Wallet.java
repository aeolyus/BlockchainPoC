import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

/**
 * @author aeolyus
 *
 */
public class Wallet{

	public PrivateKey privateKey;
	public PublicKey publicKey;

	public Wallet(){
		generateKeyPair();
	}

	public void generateKeyPair(){
		try{
			KeyPairGenerator keyGen=KeyPairGenerator.getInstance("ECDSA","BC");//elliptic curve digital signature
																					//algorithm, bouncy castle
			SecureRandom random=SecureRandom.getInstance("SHA1PRNG");//faster than NativePRNG
			ECGenParameterSpec ecSpec=new ECGenParameterSpec("prime192v1");

			keyGen.initialize(ecSpec,random);//256 bytes
			KeyPair keyPair=keyGen.generateKeyPair();

			privateKey=keyPair.getPrivate();
			publicKey=keyPair.getPublic();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}