import java.security.*;
import javax.crypto.*;
import java.util.*;
class Test
{
	public static void main(String args[])throws Exception
	{
		KeyPairGenerator keyGen=KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
		KeyPair keyPair=keyGen.genKeyPair();

        String content="a";

        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,keyPair.getPublic());
        content=Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes()));

        System.out.println(content);

        cipher=Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE,keyPair.getPrivate());
		content=new String(cipher.doFinal(Base64.getDecoder().decode(content)));

		System.out.println(content);
	}
}