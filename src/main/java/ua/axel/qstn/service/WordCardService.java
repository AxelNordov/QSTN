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

    public WordCard findById(Long id) {
        return wordCardDAO.findById(id).get();
    }

    public List<WordCard> findAll() {
        return (List<WordCard>) wordCardDAO.findAll();
    }

    public List<WordCard> findByLanguageId(Long languageId) {
        return wordCardDAO.findByLanguageId(languageId);
    }

    public List<WordCard> findByQuestionContaining(String filterQuestion) {
        return wordCardDAO.findByQuestionContaining(filterQuestion);
    }

    public List<WordCard> findByAnswerContaining(String filterAnswer) {
        return wordCardDAO.findByAnswerContaining(filterAnswer);
    }

    public WordCard getRandomWordCard() {
        Random random = new Random();
        List<WordCard> wordCards = findAll();
        return wordCards.get(random.nextInt(wordCards.size()));
    }

    public WordCard getRandomWordCard(Long languageId) {
        Random rand = new Random();
        List<WordCard> wordCardsByLanguageId = findByLanguageId(languageId);
        return wordCardsByLanguageId.get(rand.nextInt(wordCardsByLanguageId.size()));
    }

    public WordCard getRandomWordCard(List<WordCard> wordCards) {
        Random random = new Random();
        WordCard wordCard = null;
        if (!wordCards.isEmpty()) {
            wordCard = wordCards.get(random.nextInt(wordCards.size()));
        } else {
            wordCard = new WordCard();
            wordCard.setQuestion("No cards yet!");
            wordCard.setAnswer("Карт не завезли пока!");
        }
        return wordCard;
    }

    public String getRandomWordCardAnswerExcept(List<WordCard> wordCards, List<String> wordCardAnswerExcept) {
        String answer;
        do {
            answer = getRandomWordCard(wordCards).getAnswer();
        } while (wordCardAnswerExcept.contains(answer));
        return answer;
    }

    public void save(WordCard wordCard) {
        wordCardDAO.save(wordCard);
    }

    public void saveAll(Collection<WordCard> wordCardSet) {
        wordCardDAO.saveAll(wordCardSet);
    }

    public void removeDuplicateWordCards() {
        wordCardDAO.removeDuplicateWordCards();
    }

    public void deleteById(Long id) {
        wordCardDAO.deleteById(id);
    }

    public void deleteAll() {
        wordCardDAO.deleteAll();
    }
}