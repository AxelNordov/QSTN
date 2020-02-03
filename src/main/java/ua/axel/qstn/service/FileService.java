package ua.axel.qstn.service;

import org.springframework.web.multipart.MultipartFile;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.domain.WordCard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileService {
    private final static String UPLOAD_PATH = "/d:/temp/";


    public static List<WordCard> parseFileToWordCards(File file, Language language) {
        List<WordCard> wordCards = new LinkedList<>();
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
        File uploadDir = new File(UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        File fileOnDisk = new File(UPLOAD_PATH + "/" + file.getOriginalFilename());
        file.transferTo(fileOnDisk);
        return fileOnDisk;
    }
}
