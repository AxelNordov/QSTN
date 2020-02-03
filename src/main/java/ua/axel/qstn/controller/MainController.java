package ua.axel.qstn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.domain.Quiz;
import ua.axel.qstn.domain.WordCard;
import ua.axel.qstn.repository.QuizDAO;
import ua.axel.qstn.service.LanguageService;
import ua.axel.qstn.service.QuizService;
import ua.axel.qstn.service.WordCardService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {
/*
//    //Field-based dependency injection (Внедрение через поле)
//    @Autowired
//    private WordCardRepo wordCardRepo;

//    //Setter-based dependency injection (Внедрение через сеттер)
//    private WordCardRepo wordCardRepo;

//    //Constructor-based dependency injection (Внедрение через конструктор)
//    private final WordCardRepo wordCardRepo;
//
//    @Autowired
//    public MainController(WordCardRepo wordCardRepo) {
//        this.wordCardRepo = wordCardRepo;
//    }
*/

    private final QuizDAO quizDAO;
    private final QuizService quizService;
    private final WordCardService wordCardService;
    private final LanguageService languageService;

    @Autowired
    public MainController(QuizDAO quizDAO, QuizService quizService, WordCardService wordCardService, LanguageService languageService) {
        this.quizDAO = quizDAO;
        this.quizService = quizService;
        this.wordCardService = wordCardService;
        this.languageService = languageService;
    }

    @GetMapping("/")
    String index() {
        return "index";
    }

    @GetMapping("/playWords")
    public String playCards(Model model) {
        List<WordCard> wordCardsAll = wordCardService.findAll();
        WordCard wordCard = null;
        if (!wordCardsAll.isEmpty()) {
            wordCard = wordCardService.getRandomWordCard();
        } else {
            wordCard = new WordCard();
            wordCard.setText("No cards yet!");
            wordCard.setTranslated("Карт не завезли пока!");
        }
        model.addAttribute("wordCard", wordCard);
        return "playWords";
    }

    @GetMapping("/quizCards")
    public String quizzes(
            @RequestParam(required = false) String filterQuestion,
            @RequestParam(required = false) String filterLanguageId,
            Model model
    ) {
        List<Quiz> quizzes;

        if (filterQuestion != null && !filterQuestion.isEmpty()) {
            quizzes = quizDAO.findByQuestionContaining(filterQuestion);
            filterLanguageId = null;
        } else if (filterLanguageId != null && !filterLanguageId.isEmpty()) {
            quizzes = quizDAO.findByLanguageId(Long.valueOf(filterLanguageId));
        } else {
            quizzes = quizDAO.findAll();
        }

        List<Language> languages = languageService.findAll();

        model.addAttribute("quizzes", quizzes);
        model.addAttribute("languages", languages);
        model.addAttribute("filterQuestion", filterQuestion);
        model.addAttribute("filterLanguageId", filterLanguageId);
        return "quizCards";
    }

    @GetMapping("/playQuizzes")
    public String playQuiz(Model model) {
        List<WordCard> wordCardsAll = wordCardService.findAll();
        Quiz quiz = null;
        if (wordCardsAll.size() >= 4) {
            quiz = quizService.quizFromWordCard(wordCardsAll, wordCardService.getRandomWordCard());
        } else {
            quiz = new Quiz();
            quiz.setQuestion("Not enough cards yet for autoquiz!");
            quiz.setVariants(new ArrayList<>(Arrays.asList("Количества", "карточек", "пока", "не достаточно")));
        }
        model.addAttribute("quiz", quiz);
        return "playQuizzes";
    }


}