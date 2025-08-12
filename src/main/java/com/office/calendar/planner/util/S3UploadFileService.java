package com.office.calendar.planner.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Slf4j
@Service
public class S3UploadFileService {

    final private S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private  String bucket;

    public S3UploadFileService(
        @Value("${aws.s3.accesskey}") String accesskey,
        @Value("${aws.s3.secretkey}") String secretkey,
        @Value("${aws.s3.region}") String region) {

        // AWS 자격증명 설정
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accesskey, secretkey);

        // S3 클라이언트 설정
        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();

    }



    // 파일 업로드 하기
    public UploadedFileInfo upload (MultipartFile multipartFile) {

        // 파일 확장자 포함한 고유 이름 생성
        UUID uuid = UUID.randomUUID();
        String uniqueFileName = uuid.toString().replaceAll("-", "");

        String fileOriName = multipartFile.getOriginalFilename();
        String fileExtension = fileOriName.substring(fileOriName.lastIndexOf("."));

        String fileName = uniqueFileName + fileExtension;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(multipartFile.getContentType())
                    .build();

            // 파일 업로드
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

            // 업로드한 파일 URL
            String fileUrl = s3Client.utilities()
                    .getUrl(GetUrlRequest.builder().bucket(bucket).key(fileName).build()).toString();

            // 업로드한 파일의 URL와 NAME 반환
            return new UploadedFileInfo(fileUrl, fileName);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

}

//    @Value("${calendar.image.upload.dir}")
//    private String imageUploadDir;                  // properties 안에 들어있는 값을 불러옴
//
//    @Value("${calendar.image.upload.dir.seperator}")
//    private String imageUploadDirSeperator;
//
//    public String upload(String id, MultipartFile file) {
//
//        boolean result = false;
//
//        String fileOriName = file.getOriginalFilename();
//        String fileExtension = fileOriName.substring(fileOriName.lastIndexOf("."), fileOriName.length());
//
//        /*
//        // for windows
//        // String uploadDir = "c:\\calendar\\upload\\" + id;
//
//        // for Ubuntu
//        String uploadDir = "/calendar/upload/" + id;
//
//         */
//
//        String uploadDir = imageUploadDir + id;
//
//        // 새로운 파일 이름 생성
//        UUID uuid = UUID.randomUUID();
//        String uniqueFileName = uuid.toString().replaceAll("-", "");
//
//        /*
//        // for windows
//        // File saveFile = new File(uploadDir + "\\" + uniqueFileName + fileExtension);
//
//        // for Ubuntu
//        File saveFile = new File(uploadDir + "/" + uniqueFileName + fileExtension);
//
//         */
//
//        File saveFile = new File(uploadDir + imageUploadDirSeperator + uniqueFileName + fileExtension);
//
//        if (!saveFile.exists())
//            saveFile.mkdirs();
//
//        try {
//            file.transferTo(saveFile);
//            result = true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
//        if (result) {
//            log.info("FILE UPLOAD SUCCESS!!");
//            return uniqueFileName + fileExtension;
//
//        } else {
//            log.info("FILE UPLOAD FAIL!!");
//            return null;
//
//        }
//
//    }
//
//}
