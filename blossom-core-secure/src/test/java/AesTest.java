

import org.apache.shiro.codec.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class AesTest {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		SecretKey deskey = keygen.generateKey();
		System.out.println(Base64.encodeToString(deskey.getEncoded()));
	}
}
