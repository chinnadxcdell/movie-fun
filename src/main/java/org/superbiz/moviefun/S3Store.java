package org.superbiz.moviefun;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {

    private AmazonS3Client amazonS3Client;
    private final Tika tika = new Tika();

    private String photoStorageBucket;

    public S3Store(AmazonS3Client amazonS3Client,String photoStorageBucket){
        this.amazonS3Client=amazonS3Client;
        this.photoStorageBucket=photoStorageBucket;
    }


    @Override
    public void put(Blob blob) throws IOException {
        amazonS3Client.putObject(photoStorageBucket,blob.name,blob.inputStream,null);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        if (!amazonS3Client.doesObjectExist(photoStorageBucket, name)) {
            return Optional.empty();
        }

        try (S3Object s3Object = amazonS3Client.getObject(photoStorageBucket, name)) {
            S3ObjectInputStream content = s3Object.getObjectContent();

            byte[] bytes = IOUtils.toByteArray(content);

            return Optional.of(new Blob(
                    name,
                    new ByteArrayInputStream(bytes),
                    tika.detect(bytes)
            ));
        }
    }

    @Override
    public void deleteAll() {
        amazonS3Client.deleteBucket(photoStorageBucket);
    }
}
