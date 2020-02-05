package ua.axel.qstn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.axel.qstn.domain.Option;
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
    private final OptionService optionService;

    @Autowired
    public QuizCardService(QuizCardDAO quizCardDAO, WordCardService wordCardService, OptionService optionService) {
        this.quizCardDAO = quizCardDAO;
        this.wordCardService = wordCardService;
        this.optionService = optionService;
    }

    public static void mixVariantsOfQuizCard(QuizCard quizCard) {
        List<Option> options = quizCard.getOptions();
        List<Boolean> rightBooleanOptions = getRightBooleanOptions(quizCard);

        Random random = new Random();
        for (int i = options.size() - 1; i > 0; i--) {
            int j = random.nextInt(i);
            MainService.swapElementsFromConcretePlaces(options, i, j);
            MainService.swapElementsFromConcretePlaces(rightBooleanOptions, i, j);
        }

        quizCard.setRightOptions(rightBooleanToIntOptions(rightBooleanOptions));
    }

    public static List<Boolean> getRightBooleanOptions(QuizCard quizCard) {
        List<Boolean> rightBooleanOptions = new ArrayList<>();
        for (int i = 0; i < quizCard.getNumberOfOptions(); i++) {
            rightBooleanOptions.add(isOptionTrue(quizCard.getRightOptions(), i));
        }
        return rightBooleanOptions;
    }

//    public static int rightIntToBooleanOptions(List<Boolean> rightBooleanOptions) {
//        int rightIntOptions = 0;
//        for (int i = 0; i < rightBooleanOptions.size(); i++) {
//            if(rightBooleanOptions.get(i))
//                rightIntOptions +=
//        }
//        return rightBooleanOptions;
//    }

    static boolean isOptionTrue(int rightOptions, int number) {
        return rightOptions / (int) Math.pow(2, number) % 2 == 1;
    }

    public static int rightBooleanToIntOptions(List<Boolean> rightOptionsBoolean) {
        int rightOptions = 0;
        for (int i = 0; i < rightOptionsBoolean.size(); i++) {
            if (rightOptionsBoolean.get(i)) {
                rightOptions += Math.pow(2, i);
            }
        }
        return rightOptions;
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
        final int NUMBER_OF_OPTIONS = 4;

        QuizCard quizCard = new QuizCard();
        quizCard.setQuestion(wordCard.getQuestion());
        quizCard.setLanguage(wordCard.getLanguage());
        quizCard.setNumberOfOptions(NUMBER_OF_OPTIONS);
        quizCard.setOneRightOptionRadio(true);

        ArrayList<Option> options = new ArrayList<>();
        ArrayList<Boolean> rightBooleanOptions = new ArrayList<>();

        Option option = new Option();
        option.setValue(wordCard.getAnswer());
        options.add(option);
        rightBooleanOptions.add(true);

        for (int i = 1; i < quizCard.getNumberOfOptions(); i++) {
            Option tempOption = new Option();
            tempOption.setValue(wordCardService.getRandomWordCardAnswerExcept(wordCards, optionsToStrings(options)));
            options.add(tempOption);
            rightBooleanOptions.add(false);
        }
        quizCard.setOptions(options);
        quizCard.setRightOptions(rightBooleanToIntOptions(rightBooleanOptions));
        mixVariantsOfQuizCard(quizCard);
        return quizCard;
    }

    public void save(QuizCard quizCard) {
        quizCard.setNumberOfOptions(quizCard.getOptions().size());
        quizCardDAO.save(quizCard);
    }

    public List<Option> stringsToOptions(List<String> stringList) {
        List<Option> optionList = new ArrayList<>();
        for (String s : stringList) {
            if (!s.isEmpty()) {
                Option option = new Option();
                option.setValue(s);
                optionService.save(option);
                optionList.add(option);
            }
        }
        return optionList;
    }

    public List<String> optionsToStrings(List<Option> optionList) {
        List<String> stringList = new ArrayList<>();
        for (Option option : optionList) {
            stringList.add(option.getValue());
        }
        return stringList;
    }

    public QuizCard findById(Long id) {
        return quizCardDAO.findById(id).get();
    }

    public void deleteById(Long id) {
        quizCardDAO.deleteById(id);
    }
}
