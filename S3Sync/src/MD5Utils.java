import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;

public class MD5Utils {
	private static final long chunkSize = Config.chunkSize;
	private static final long singleFileMaxSize = Config.singleFileMaxSize;
	//more detail, please see http://www.imooc.com/wenda/detail/587053
	public static String getS3MD5(byte[] input) {
		int len = input.length;
		if(len < singleFileMaxSize) {
			return getMD5(input);
		}
		int chunkCnt = len / (int)chunkSize + (len % chunkSize == 0 ? 0 : 1); 
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// For specifying wrong message digest algorithms
			throw new RuntimeException(e);
		}
		byte[][] bufs = new byte[chunkCnt][];
		int bufSize = 0;
		for(int pos = 0, i = 0; i < chunkCnt; ++i) {
			int nextPos  =  Math.min(pos + (int)chunkSize, len);
			bufs[i] = md5.digest(Arrays.copyOfRange(input, pos, nextPos));
			bufSize += bufs[i].length;
			pos = nextPos;
		}
		//merge buffers, maybe there is a better implementation.
		byte[] buf = new byte[bufSize];
		for(int pos = 0, i = 0; i < bufs.length; ++i) {
			System.arraycopy(bufs[i], 0, buf, pos, bufs[i].length);
			pos += bufs[i].length;
		}
		return getMD5(buf) + "-" + String.valueOf(chunkCnt);
	}
	public static String getMD5(byte[] input) {
		try {
			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			// of an input digest() return array of byte
			byte[] messageDigest = md.digest(input);
			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			// For specifying wrong message digest algorithms
			throw new RuntimeException(e);
		}
	}
	public static String getS3MD5(String input) {
		return getS3MD5(input.getBytes());
	}
	public static String getS3MD5(File file) {
		byte[] buf = null;
		try {
			buf = Files.readAllBytes(file.toPath());
		} catch(IOException e) {
			return null;
		}
		return getS3MD5(buf);
	}
	//for test
	public static void main(String[] args) {
		System.out.println(getS3MD5(new File("C:\\Users\\h-hg\\Desktop\\1702720115.pdf")));
		System.out.println(getS3MD5("123456789"));
		System.out.println(getS3MD5(new File("D:\\Downloads\\Multrin-1.2.2-win.zip")));
		System.out.println(getS3MD5(new File("D:\\test\\³É¼¨µ¥.docx")));
	}
}
