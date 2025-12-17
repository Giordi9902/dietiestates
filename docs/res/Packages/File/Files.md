classDiagram
direction BT
class ImageStorageService {
<<Interface>>
  + storeImage(MultipartFile, Integer) String
  + deleteAllImagesForListing(Integer) int
  + isValidImage(MultipartFile) boolean
  + deleteImage(String) boolean
}
class S3Config {
  - String awsRegion
  - String awsSecretKey
  - String awsAccessKeyId
  + amazonS3() AmazonS3
}
class S3ImageStorageService {
  - String bucketName
  - AmazonS3 amazonS3
  - safeExtension(String, String) String
  + getOriginalUrl(String) String
  + generatePresignedUrl(String) URL
  + deleteImage(String) boolean
  + isValidImage(MultipartFile) boolean
  - buildObjectKey(Integer, String, String) String
  + presignUrl(String) String
  - listingPrefix(Integer) String
  + extractKeyFromUrl(String) String
  + deleteAllImagesForListing(Integer) int
  + storeImage(MultipartFile, Integer) String
}

S3ImageStorageService  ..>  ImageStorageService 
