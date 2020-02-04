package ua.axel.qstn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.axel.qstn.domain.QuizCard;
import ua.axel.qstn.domain.WordCard;
import ua.axel.qstn.repository.QuizCardDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuizCardService {

    private final QuizCardDAO quizCardDAO;
    private final WordCardService wordCardService;

    @Autowired
    public QuizCardService(QuizCardDAO quizCardDAO, WordCardService wordCardService) {
        this.quizCardDAO = quizCardDAO;
        this.wordCardService = wordCardService;
    }

    public List<QuizCard> findAll() {
        return (List<QuizCard>) quizCardDAO.findAll();
    }

    public List<QuizCard> findByLanguageId(Long languageId) {
        return quizCardDAO.findByLanguageId(languageId);
    }

    public List<QuizCard> findByQuestionContaining(String filterQuestion) {
        return quizCardDAO.findByQuestionContaining(filterQuestion);
    }

    public QuizCard quizCardFromWordCard(List<WordCard> wordCards, WordCard wordCard) {
        final int COUNT_OF_VARIANTS = 4;
        QuizCard quizCard = new QuizCard();
        quizCard.setQuestion(wordCard.getQuestion());
        quizCard.setLanguage(wordCard.getLanguage());
        quizCard.setOneRightAnswer(true);

        ArrayList<String> variants = new ArrayList<>();
        ArrayList<Boolean> rightVariants = new ArrayList<>();

        variants.add(wordCard.getAnswer());
        rightVariants.add(true);

        for (int i = 1; i < COUNT_OF_VARIANTS; i++) {
            variants.add(wordCardService.getRandomWordCardAnswerExcept(wordCards, variants));
            rightVariants.add(false);
        }
        quizCard.setAnswers(variants);
        quizCard.setRightAnswers(rightVariants);
        mixVariantsOfQuizCard(quizCard);
        return quizCard;
    }

    private static void mixVariantsOfQuizCard(QuizCard quizCard) {
        List<String> variants = quizCard.getAnswers();
        List<Boolean> rightVariants = quizCard.getRightAnswers();

        Random random = new Random();
        for (int i = variants.size() - 1; i > 0; i--) {
            int j = random.nextInt(i);
            MainService.swapElementsFromConcretePlaces(variants, i, j);
            MainService.swapElementsFromConcretePlaces(rightVariants, i, j);
        }
    }

    public void save(QuizCard quizCard) {
        quizCardDAO.save(quizCard);
    }
}
