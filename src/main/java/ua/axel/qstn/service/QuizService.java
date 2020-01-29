package ua.axel.qstn.service;

import ua.axel.qstn.domain.Quiz;
import ua.axel.qstn.domain.WordCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizService {

    public static Quiz quizFromWordCard(List<WordCard> wordCards, WordCard wordCard) {
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
            variants.add(WordCardService.getRandomWordCardTranslateExcept(wordCards, variants));
            rightVariants.add(false);
        }

        quiz.setVariants(variants);
        quiz.setRightVariants(rightVariants);

        randomVariants(quiz);

        return quiz;
    }

    private static void randomVariants(Quiz quiz) {
        ArrayList<String> variants = quiz.getVariants();
        ArrayList<Boolean> rightVariants = quiz.getRightVariants();

        Random random = new Random();
        for (int i = variants.size() - 1; i > 0; i--) {
            int j = random.nextInt(i);
            swapElementsFromConcretePlaces(variants, i, j);
            swapElementsFromConcretePlaces(rightVariants, i, j);
        }
    }

    private static <T> void swapElementsFromConcretePlaces(ArrayList<T> arrayList, int i, int j) {
        T temp = arrayList.get(i);
        arrayList.set(i, arrayList.get(j));
        arrayList.set(j, temp);
    }

}
