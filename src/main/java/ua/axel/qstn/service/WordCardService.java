package ua.axel.qstn.service;

import org.springframework.stereotype.Service;
import ua.axel.qstn.domain.WordCard;

import java.util.List;
import java.util.Random;

@Service
public class WordCardService {

    public static WordCard getRandomWordCard(List<WordCard> wordCards) {
        Random rand = new Random();
        return wordCards.get(rand.nextInt(wordCards.size()));
    }

    static String getRandomWordCardTranslateExcept(List<WordCard> wordCards, List<String> wordCardTranslatedExcept) {
        String translated;
        do {
            translated = getRandomWordCard(wordCards).getTranslated();
        } while (wordCardTranslatedExcept.contains(translated));
        return translated;
    }

}
