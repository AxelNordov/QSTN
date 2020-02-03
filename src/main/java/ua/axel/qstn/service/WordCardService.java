package ua.axel.qstn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.axel.qstn.domain.WordCard;
import ua.axel.qstn.repository.WordCardDAO;

import java.util.Collection;
import java.util.List;
import java.util.Random;

@Service
public class WordCardService {

    private WordCardDAO wordCardDAO;

    @Autowired
    public WordCardService(WordCardDAO wordCardDAO) {
        this.wordCardDAO = wordCardDAO;
    }

    public WordCard getRandomWordCard() {
        Random rand = new Random();
        List<WordCard> wordCardsAll = findAll();
        return wordCardsAll.get(rand.nextInt(wordCardsAll.size()));
    }

    public WordCard getRandomWordCard(String languageId) {
        Random rand = new Random();
        List<WordCard> wordCardsByLanguageId = findByLanguageId(languageId);
        return wordCardsByLanguageId.get(rand.nextInt(wordCardsByLanguageId.size()));
    }

    public List<WordCard> findAll() {
        return wordCardDAO.findAll();
    }

    public List<WordCard> findByLanguageId(String stringId) {
        return wordCardDAO.findByLanguageId(Long.valueOf(stringId));
    }

    public void deleteAll() {
        wordCardDAO.deleteAll();
    }

    public void saveAll(Collection<WordCard> wordCardSet) {
        wordCardDAO.saveAll(wordCardSet);
    }

    public void deleteById(String stringId) {
        wordCardDAO.deleteById(Long.valueOf(stringId));
    }

    public WordCard findById(String stringId) {
        return wordCardDAO.findById(Long.valueOf(stringId)).get();
    }

    public List<WordCard> findByTextContaining(String filterText) {
        return wordCardDAO.findByTextContaining(filterText);
    }

    public List<WordCard> findByTranslatedContaining(String filterTranslated) {
        return wordCardDAO.findByTranslatedContaining(filterTranslated);
    }

    public void save(WordCard wordCard) {
        wordCardDAO.save(wordCard);
    }

    String getRandomWordCardTranslateExcept(List<WordCard> wordCards, List<String> wordCardTranslatedExcept) {
        String translated;
        do {
            translated = getRandomWordCard().getTranslated();
        } while (wordCardTranslatedExcept.contains(translated));
        return translated;
    }

}


/*
    delete t1 FROM word_card t1
        INNER JOIN word_card t2
        where t1.id < t2.id
        AND t1.text = t2.text
        AND t1.translated = t2.translated
        AND t1.language_id = t2.language_id;
*/
