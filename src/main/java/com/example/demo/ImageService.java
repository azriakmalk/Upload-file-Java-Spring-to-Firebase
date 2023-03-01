package com.example.demo;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public ImageEntity addImage(ImageEntity image) {
        String link_image = uploadImage(image.getImage());
        image.setLink_image(link_image);
        ImageEntity save = imageRepository.save(image);
        save.setImage(null);
        return save;
    }

    public List<ImageEntity> getAllImage() {
        List<ImageEntity> getAll = imageRepository.findAll();
        return getAll;
    }
    public String uploadImage(MultipartFile file) {
        try {
            File modifiedFile = new File(file.getOriginalFilename());
            FileOutputStream os = new FileOutputStream(modifiedFile);
            os.write(file.getBytes());

            String filename = System.currentTimeMillis()+"_"+file.getOriginalFilename();



            BlobId blobId = BlobId.of("easy-dining.appspot.com", filename);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./easy-dining-firebase-adminsdk-mppus-c141f34cec.json"));
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.create(blobInfo, Files.readAllBytes(modifiedFile.toPath()));

            os.close();
            modifiedFile.delete();

            return String.format("https://firebasestorage.googleapis.com/v0/b/easy-dining.appspot.com/o/%s?alt=media", URLEncoder.encode(filename, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println(e);
            return "Gagal";
        }
    }
}
