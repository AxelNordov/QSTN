package ua.axel.qstn.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.domain.WordCard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileService {

    @Value("${upload.path}")
    private String uploadPath;

    public List<WordCard> parseFileToWordCards(File file, Language language) {
        List<WordCard> wordCards = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    line = line.replaceAll("&nbsp;", "&nbsp");
                    String[] lineParts = line.split(";");
                    WordCard wordCard = new WordCard();
                    lineParts[0] = lineParts[0].replaceAll("&nbsp", "&nbsp;");
                    wordCard.setQuestion(lineParts[0]);
                    if (lineParts.length > 1) {
                        lineParts[1] = lineParts[1].replaceAll("&nbsp", "&nbsp;");
                        wordCard.setAnswer(lineParts[1]);
                    }
                    wordCard.setLanguage(language);
                    wordCards.add(wordCard);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();
        return wordCards;
    }

    public File writeFileOnDisk(MultipartFile file) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        File fileOnDisk = new File(uploadPath + "/" + file.getOriginalFilename());
        file.transferTo(fileOnDisk);
        return fileOnDisk;
    }
}
