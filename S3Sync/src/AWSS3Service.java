import java.io.File;
//import java.util.ArrayList;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
//import com.amazonaws.services.s3.model.S3Object;

public class AWSS3Service {

	private AmazonS3 s3 = null;
	private String bucketName = null;
	private final long chunkSize = Config.chunkSize;
	private final long singleFileMaxSize = Config.singleFileMaxSize;
	private String logDirPath = null;

	private enum UploadState {
		NOT_UPLOADED, // not unloaded
		NOT_UPDATED, // not updated
		PARTLY_UPLOADED, // partly uploaded
		DONE, // done
		UNKNOWN// unknown
	}

	public AWSS3Service(String[] args, String logDirPath) {

		bucketName = args[1];// "huanghaogan";
		String accessKey = args[2]; // "B0B0B545EC6C122C4498";
		String secretKey = args[3];// "WzE4ODJBRkQyQzVDNjVCREE2NTcxNUFBQjkxOUVBRDQzRjI4NEJGQkNd";
		String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
		String signingRegion = "";
		try {
			BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			ClientConfiguration ccfg = new ClientConfiguration().withUseExpectContinue(false);
			EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);
			s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
					.withClientConfiguration(ccfg).withEndpointConfiguration(endpoint).withPathStyleAccessEnabled(true)
					.build();			
		} catch(Exception e) {
			System.err.println("something error in bucket name or access key or secret key\n");
			System.exit(-1);
		}
		this.logDirPath = logDirPath;
	}

	private String getLogPath(String key) {
		return logDirPath + key.replace(File.separatorChar, '_') + ".bin";
	}

	public String getObjMD5(String key) {
		String cloudMD5 = null;
		try {
			ObjectMetadata objMeta = s3.getObjectMetadata(bucketName, key);
			cloudMD5 = objMeta.getETag();
		} catch (AmazonServiceException e) {
			return null;
		} catch (SdkClientException e) {
			return null;
		}
		return cloudMD5;
	}

	// return state of file
	private UploadState getFileState(File file, String key) {
		// check md5
		String cloudMD5 = getObjMD5(key);
		boolean hasLog = false;
		ChunkFileLog log = ChunkFileLog.read(getLogPath(key));
		if (log != null) {
			if (log.hasFinished()) {
				log.finish();// delete the log file
			} else {
				hasLog = true;
			}
		}
		if (cloudMD5 == null) {
			return hasLog == true ? UploadState.PARTLY_UPLOADED : UploadState.NOT_UPLOADED;
		}
		String localMD5 = MD5Utils.getS3MD5(file);
		boolean isSame = localMD5.equals(cloudMD5);
		if (isSame == true) {
			if (hasLog) {
				log.finish(); // delete the log
				return UploadState.UNKNOWN;
			} else {
				return UploadState.DONE;
			}
		} else {
			return hasLog == true ? UploadState.PARTLY_UPLOADED : UploadState.NOT_UPDATED;
		}
	}

	private ChunkFileLog uploadChunkFileInit(File file, String key) {
		String uploadId = null;
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, key);
		try {
			uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
		} catch (AmazonServiceException e) {
			System.out.println(e.getErrorMessage());
		} catch (SdkClientException e) {
			System.out.print(e.getMessage());
		}
		if (uploadId == null) {
			System.out.println("Test uploaId == null");
			return null;
		}

		ChunkFileLog log = new ChunkFileLog(file, key, getLogPath(key), uploadId, chunkSize);
		return log;
	}

	// serial upload file
	private void uploadChunkFiles(ChunkFileLog log) {
		// System.out.format("Test: uploadChunkFiles cnt = %d: %s\n", log.chunkCnt,
		// log.key);
		File file = new File(log.filePath);
		for (int partNumber = 1; partNumber <= log.chunkCnt;) {
			if (log.isPartETagNull(partNumber)) /* judge whether this chunk needs to be uploaded */ {
				if (uploadSimgleChunkFile(file, partNumber, log)) {
					System.out.format("%s: %d/%d has been uploaded successfully\n", log.key, partNumber, log.chunkCnt);
					++partNumber;
				} else {
					System.out.format("%s: %d/%d failed to upload\n", partNumber, log.chunkCnt, log.key);
				}
			} else {
				System.out.format("%s: %d/%d has been uploaded before\n", log.key, partNumber, log.chunkCnt);
				++partNumber;
			}
		}
		// complete the upload
		CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, log.key,
				log.uploadId, log.getPartETags());
		try {
			s3.completeMultipartUpload(compRequest);
		} catch (AmazonServiceException e) {
			System.out.println(e.getErrorMessage());
		} catch (SdkClientException e) {
			System.out.println(e.getMessage());
		}
		log.finish();
		System.out.format("File upload completely: %s\n", log.key);
	}

	private boolean uploadSimgleChunkFile(File file, int partNumber, ChunkFileLog log) {
		// calculate basic information
		long filePosition = chunkSize * (partNumber - 1), partSize = Math.min(chunkSize, file.length() - filePosition);
		// Create request to upload a part.
		UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(bucketName).withKey(log.key)
				.withUploadId(log.uploadId).withPartNumber(partNumber).withFileOffset(filePosition).withFile(file)
				.withPartSize(partSize);
		// Upload part and add response to our list.
		try {
			UploadPartResult uploadPartResult = s3.uploadPart(uploadRequest);
			log.writeLog(partNumber, uploadPartResult.getPartETag());
		} catch (AmazonServiceException e) {
			return false;
		} catch (SdkClientException e) {
			return false;
		}
		return true;
	}

	private void uploadFileSingle(File file, String key) {
		boolean fail = true;
		while (fail) {
			fail = false;
			try {
				s3.putObject(bucketName, key, file);
			} catch (AmazonServiceException e) {
				fail = true;
			} catch (SdkClientException e) {
				fail = true;
			}
			if (fail) {
				System.out.format("%s: failed to upload\n", key);
			}
		}
		System.out.format("File upload completely: %s\n", key);
	}
	// private void uploadFileMulti(File file, String key, FileChunkLog log) {

	// }
	public void uploadFile(File file, String key) {
		ChunkFileLog log = null;
		long len = file.length();
		switch (getFileState(file, key)) {
		case DONE:
			System.out.format("File has been uploaded: %s\n", key);
			break;
		case NOT_UPLOADED:
			// upload the entire file
			System.out.format("File hasn't been upload: %s\n", key);
			if (len < singleFileMaxSize) {
				uploadFileSingle(file, key);
			} else {
				log = uploadChunkFileInit(file, key); // new log file
				uploadChunkFiles(log);
			}
			break;
		case NOT_UPDATED:
			// upload the entire file
			System.out.format("File need to be updated: %s\n", key);
			if (len < singleFileMaxSize) {
				uploadFileSingle(file, key);
			} else {
				log = uploadChunkFileInit(file, key); // new log file
				uploadChunkFiles(log);
			}
			break;
		case PARTLY_UPLOADED:
			// upload part file
			System.out.format("File has uploaded partly: %s\n", key);
			log = ChunkFileLog.read(getLogPath(key));
			uploadChunkFiles(log);
			break;
		case UNKNOWN:
			System.out.println("Unknown file state: " + key);
			break;
		}
	}

	public void deleteFile(String key) {
		try {
			s3.deleteObject(bucketName, key);
		} catch (AmazonServiceException e) {
			System.out.println(e.getErrorMessage());
		} catch (SdkClientException e) {
			System.out.println(e.getMessage());
		}
		System.out.format("File delete sucessfully: %s\n", key);
	}
}
