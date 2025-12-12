package it.unina.ingsw.dietiestates_backend.file;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

	@Value("${aws.access-key}")
	private String awsAccessKeyId;

	@Value("${aws.secret-key}")
	private String awsSecretKey;

	@Value("${aws.region}")
	private String awsRegion;

	@Bean
	public AmazonS3 amazonS3(){
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
		return AmazonS3ClientBuilder.standard()
				.withRegion(awsRegion)
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.build();
	}
}