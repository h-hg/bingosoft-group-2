import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.monitor.FileAlterationListener;
//import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

//import com.amazonaws.util.Md5Utils;

public class S3Sync {
	private String dirPath = null;
	private String logDirPath = null; 
	private File dir = null;
	private long interval; // unit second
	private AWSS3Service s3 = null;

	public S3Sync(String[] args, long intervalSecond) {
		this.dir = new File(args[0]);
		try {
			this.dirPath = dir.getCanonicalPath();
		} catch (IOException e) {
			System.err.format("The folder(%s) doesn't exist.\n", dirPath);
			System.exit(-1);
		}
		if (dir.exists() == false) {
			System.err.format("The folder(%s) doesn't exist.\n", dirPath);
			System.exit(-1);
		}
		this.interval = TimeUnit.SECONDS.toMillis(intervalSecond);

		String foo =  this.dirPath + File.separator + ".." + File.separator + this.dir.getName() + "_log";
		try {
			File file = new File(foo);
			if(file.exists() == false) {
				file.mkdir();
			}
			this.logDirPath = file.getCanonicalPath() + File.separator;
		} catch(IOException e) {
			System.err.format("Can't create the log(%s) folder\n", foo);
			System.exit(-1);
		}
		this.s3 = new AWSS3Service(args, this.logDirPath);
	}

	private void initDirHelper(File file) {
		for (File subFile : file.listFiles()) {
			String path = subFile.getAbsolutePath(), key = path.substring(this.dirPath.length() + 1, path.length()).replace("\\", "/");
			if (subFile.isFile()) {
				//System.out.format("Init File: %s\n", key);
				s3.uploadFile(subFile, key);
			} else if (subFile.isDirectory()) {
				//System.out.format("Init Folder: %s\n", key);
				initDirHelper(subFile);
			}
		}
	}
	private void initDir(File file) {
		System.out.println("Init start...");
		initDirHelper(dir);
		System.out.println("init done!");
	}
	public void monitorDir() throws Exception {
		initDir(dir);
		FileAlterationObserver observer = new FileAlterationObserver(this.dir);
		FileAlterationMonitor monitor = new FileAlterationMonitor(this.interval);
		FileListener listener = new FileListener(this.dirPath, s3);
		observer.addListener(listener);
		monitor.addObserver(observer);
		monitor.start();
		System.out.println("monitoring start");
	}

	public static void main(String[] args) {
		if(args.length != 4) {
			System.err.format("error number of args, args length = %d\n", args.length);
			System.err.print("args[0] = dir\n" + "args[1] = bucketName\n" + "args[2] = accessKey\n" + "args[3] = secretKey\n");
			System.exit(-1);
		}
		//args[0] = dir
		//args[1] = bucketName
		//args[2] = accessKey
		//args[3] = secretKey 
		long interval = 5;
		try {
			(new S3Sync(args, interval)).monitorDir();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
