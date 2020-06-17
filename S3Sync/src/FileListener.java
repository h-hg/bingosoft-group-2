import java.io.File;
//import java.io.IOException;
import org.apache.commons.io.monitor.FileAlterationListener;
//import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
//import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class FileListener implements FileAlterationListener {
	private String dirPath;
	private AWSS3Service s3 = null;
	public FileListener(String path, AWSS3Service s3Service) {
		this.dirPath = path;
		this.s3 = s3Service;
	}
	@Override
	public void onStart(FileAlterationObserver fileAlterationObserver) {}
	public void onStop(FileAlterationObserver fileAlterationObserver) {}
	@Override
	public void onDirectoryCreate(File file) {}
	@Override
	public void onDirectoryChange(File file) {
		//event is emitted When the subfiles or subdirectories are changed(including modification, creation, remove)
		//So there are two events
	}
	@Override
	public void onDirectoryDelete(File file) {
		//event is emitted when directory is deleted, so event of subfiles or subdirectories are also emitted.
	}
	@Override
	public void onFileCreate(File file) {
		String path = file.getAbsolutePath(), key = path.substring(dirPath.length() + 1, path.length()).replace("\\", "/");
		System.out.println("File create: " + key);
		s3.uploadFile(file, key);
	}

	@Override
	public void onFileChange(File file) {
		String path = file.getAbsolutePath(), key = path.substring(dirPath.length() + 1, path.length()).replace("\\", "/");
		System.out.println("File change: " + key);
		s3.uploadFile(file, key);
	}

	@Override
	public void onFileDelete(File file) {
		String path = file.getAbsolutePath(), key = path.substring(dirPath.length() + 1, path.length()).replace("\\", "/");
		System.out.println("File delete: " + key);
		s3.deleteFile(key);
	}
}