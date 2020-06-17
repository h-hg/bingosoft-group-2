import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import com.amazonaws.services.s3.model.PartETag;

public class ChunkFileLog implements Serializable {
	public String filePath = null;
	public String logPath = null;
	public String key = null;
	public String uploadId = null;
	public int chunkCnt = 0;
	
	public int successTimes = 0;
	private PartETag partETags[] = null;

	public boolean isPartETagNull(int partNumber) {
		return partETags[partNumber-1] == null;
	}
	public ArrayList<PartETag> getPartETags() {
		return new ArrayList<PartETag>(Arrays.asList(partETags));
	}
	public boolean hasFinished() {
		return successTimes == chunkCnt;
	}
	public void finish() {
		(new File(this.logPath)).delete();
	}
	public void writeLog(int partNumber, PartETag partETag) {
		--partNumber;
		if(partETags[partNumber] == null && partETag != null) {
			successTimes += 1;
		}
		partETags[partNumber] = partETag;
		this.write();
	}
	public ChunkFileLog(File file, String key, String logPath, String uploadId, long chunkSize) {
		//initialize properties
		long contentLength = file.length();
		chunkCnt = (int)(contentLength / chunkSize) + (contentLength % chunkSize != 0 ? 1 : 0);
		//special handling for empty file
		if(chunkCnt == 0)
			chunkCnt = 1;
		
		this.filePath = file.getAbsolutePath();
		this.logPath = logPath;
		this.key = key;
		this.uploadId = uploadId;
		this.partETags = new PartETag[chunkCnt];
		
		//create file
		this.write();
	}
	public static boolean write(ChunkFileLog log, String path) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));			
			out.writeObject(log);
			out.close();
		} catch(IOException e) {
			return false;
		}
        return true;
	}
	public boolean write() {
		return write(this, this.logPath);
	}
	public static boolean write(ChunkFileLog info, File file) {
		return write(info, file.getAbsolutePath());
	}
	public static ChunkFileLog read(String path) {
		ChunkFileLog obj = null;
		File file = new File(path);
		if(file.exists() == false) {
			return null;
		}
		try {
        	ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
        	obj = (ChunkFileLog) in.readObject();
        	in.close();
        } catch(IOException e) {
        	System.out.println(e.getMessage());
        	return null;
        } catch(ClassNotFoundException e) {
        	System.out.println(e.getMessage());
        	return null;
        }
        return obj;
	}
	public static ChunkFileLog read(File file) {
		return read(file.getAbsolutePath());
	}
	public static void main(String args[]) {
		//String path1 = "C:\\Users\\h-hg\\Desktop\\tianruo_x64.zip.bin_4";
		String path2 = "D:\\test_log\\navicat150_premium_cs_x64.exe.bin";
		ChunkFileLog log = ChunkFileLog.read(path2);
		if(log == null) {
			System.out.println("null ptr");
		}
		System.out.format("filePath = %s, logPath = %s, key = %s, uploadId = %s, chunkCnt = %d, times = %d\n", log.filePath, log.logPath, log.key, log.uploadId, log.chunkCnt, log.successTimes);
		for(int i = 1;i <= log.chunkCnt; ++i) {
			if(log.isPartETagNull(i) == false) {
				System.out.format("%d ", i);
			}
		}
	}
}
