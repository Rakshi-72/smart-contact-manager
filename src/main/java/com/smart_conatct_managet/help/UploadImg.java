package com.smart_conatct_managet.help;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadImg {
    private final String uplodDirectory = new ClassPathResource("/static/images").getFile().getAbsolutePath();

    public UploadImg() throws IOException {
    }

   /**
    * It takes a MultipartFile as a parameter, copies it to a directory, and returns a boolean value
    * 
    * @param profileImage The image file that is being uploaded.
    * @return A boolean value.
    */
    public boolean processImage(MultipartFile profileImage) {

        boolean result = false;
        try {
            Files.copy(profileImage.getInputStream(),
                    Paths.get(uplodDirectory + File.separator + profileImage.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
