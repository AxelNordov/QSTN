package ua.axel.qstn.service;

import org.springframework.web.multipart.MultipartFile;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.domain.WordCard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    public static List<WordCard> parseFileToWordCards(File file, Language language) {
        List<WordCard> wordCards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineParts = line.split(";");
                WordCard wordCard = new WordCard();
                wordCard.setText(lineParts[0]);
                wordCard.setTranslated(lineParts[1]);
                wordCard.setLanguage(language);
                wordCards.add(wordCard);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();
        return wordCards;
    }

    public static File writeFileOnDisk(MultipartFile file) throws IOException {
        String uploadPath = "/d:/temp/";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        File fileOnDisk = new File(uploadPath + "/" + file.getOriginalFilename());
        file.transferTo(fileOnDisk);
        return fileOnDisk;
    }
}
