package ua.axel.qstn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.axel.qstn.domain.Quiz;
import ua.axel.qstn.domain.WordCard;
import ua.axel.qstn.repository.QuizDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuizService {

    private final QuizDAO quizDAO;
    private final WordCardService wordCardService;

    @Autowired
    public QuizService(QuizDAO quizDAO, WordCardService wordCardService) {
        this.quizDAO = quizDAO;
        this.wordCardService = wordCardService;
    }

    public Quiz quizFromWordCard(List<WordCard> wordCards, WordCard wordCard) {
        final int COUNT_OF_VARIANTS = 4;
        Quiz quiz = new Quiz();
        quiz.setQuestion(wordCard.getText());
        quiz.setLanguage(wordCard.getLanguage());
        quiz.setOneRightVariant(true);

        ArrayList<String> variants = new ArrayList<>();
        ArrayList<Boolean> rightVariants = new ArrayList<>();

        variants.add(wordCard.getTranslated());
        rightVariants.add(true);

        for (int i = 1; i < COUNT_OF_VARIANTS; i++) {
            variants.add(wordCardService.getRandomWordCardTranslateExcept(wordCards, variants));
            rightVariants.add(false);
        }
        quiz.setVariants(variants);
        quiz.setRightVariants(rightVariants);
        mixVariants(quiz);
        return quiz;
    }

    private static void mixVariants(Quiz quiz) {
        List<String> variants = quiz.getVariants();
        List<Boolean> rightVariants = quiz.getRightVariants();

        Random random = new Random();
        for (int i = variants.size() - 1; i > 0; i--) {
            int j = random.nextInt(i);
            MainService.swapElementsFromConcretePlaces(variants, i, j);
            MainService.swapElementsFromConcretePlaces(rightVariants, i, j);
        }
    }

}
