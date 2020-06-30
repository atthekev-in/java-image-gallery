package edu.au.cc.gallery.aws;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;

public class S3 {

	private S3Client client;
	private static final Region region = Region.US_EAST_2;

	public void connect() {
		Region region = Region.US_EAST_2;
		client = S3Client.builder().region(region).build();
	}

	public void createBucket(String bucketName) {
		CreateBucketRequest createBucketRequest = CreateBucketRequest.builder().bucket(bucketName)
				.createBucketConfiguration(CreateBucketConfiguration.builder().locationConstraint(region.id()).build())
				.build();
		client.createBucket(createBucketRequest);

	}

	public void putObject(String bucketName, String key, String value) {
		PutObjectRequest por = PutObjectRequest.builder().bucket(bucketName).key(key).build();
		client.putObject(por, RequestBody.fromString(value));

	}

	public String getObject(String bucketName, String key) throws Exception {
		ResponseInputStream<GetObjectResponse> response = client.getObject(GetObjectRequest.builder()
																							.bucket(bucketName)
																							.key(key)
																							.build());
		BufferedReader br = new BufferedReader(new InputStreamReader(response));

		String result = br.readLine();
		return result;
	}

	public static void demo() {

	String bucketName = "edu.au.cc.kzw0068.image-gallery";
	S3 s3 = new S3();
	s3.connect();
	//s3.createBucket(bucketName);
	s3.putObject(bucketName, "banana", "yellow");
	}




}

