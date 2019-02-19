package com.pshc.util.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AwsService {
	@Autowired
	private AmazonS3 amazonS3;

	public void fileUpload(String bucketName, File file, String fileName) {
		if (amazonS3 != null) {
			try {
				PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);

				// file permission
				putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

				// upload file
				PutObjectResult ret = amazonS3.putObject(putObjectRequest);
				log.info("ret: " + ret.getETag());
				// System.out.println("ret: " + ret.getETag());

			} catch (AmazonServiceException ase) {
				ase.printStackTrace();
			}
		}
	}

	// download object
	public void downloadFile(String bucketName, String fileName, OutputStream outputStream) throws IOException {
		if (amazonS3 == null) {
			throw new NullPointerException();
		}
		log.info("download File Start " + fileName);
		S3ObjectInputStream s3objectInputStream = null;

		try {
			S3Object s3Object = amazonS3.getObject(bucketName, fileName);
			s3objectInputStream = s3Object.getObjectContent();

			byte[] bytesArray = new byte[4096];
			int bytesRead = -1;

			while ((bytesRead = s3objectInputStream.read(bytesArray)) != -1) {
				outputStream.write(bytesArray, 0, bytesRead);
			}
			outputStream.flush();

			log.debug("Object %s has been downloaded.\n", fileName);
			// System.out.format("Object %s has been downloaded.\n", objectName);
		} catch (AmazonS3Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (s3objectInputStream != null)
					s3objectInputStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
	}
}
