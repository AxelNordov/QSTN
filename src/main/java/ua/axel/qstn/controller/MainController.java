package ua.axel.qstn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.domain.Quiz;
import ua.axel.qstn.domain.WordCard;
import ua.axel.qstn.repository.LanguageRepo;
import ua.axel.qstn.repository.QuizRepo;
import ua.axel.qstn.repository.WordCardRepo;
import ua.axel.qstn.service.QuizService;
import ua.axel.qstn.service.WordCardService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

//    //Field-based dependency injection (Внедрение через поле)
//    @Autowired
//    private WordCardRepo wordCardRepo;
//    @Autowired
//    private QuizRepo quizRepo;
//    @Autowired
//    private LanguageRepo languageRepo;

//    //Constructor-based dependency injection (Внедрение через конструктор)
//    private final WordCardRepo wordCardRepo;
//    private final QuizRepo quizRepo;
//    private final LanguageRepo languageRepo;
//
//    @Autowired
//    public MainController(WordCardRepo wordCardRepo, QuizRepo quizRepo, LanguageRepo languageRepo) {
//        this.wordCardRepo = wordCardRepo;
//        this.quizRepo = quizRepo;
//        this.languageRepo = languageRepo;
//    }

    //Setter-based dependency injection (Внедрение через сеттер)
    private WordCardRepo wordCardRepo;
    private QuizRepo quizRepo;
    private LanguageRepo languageRepo;

    @Autowired
    public void setWordCardRepo(WordCardRepo wordCardRepo) {
        this.wordCardRepo = wordCardRepo;
    }

    @Autowired
    public void setQuizRepo(QuizRepo quizRepo) {
        this.quizRepo = quizRepo;
    }

    @Autowired
    public void setLanguageRepo(LanguageRepo languageRepo) {
        this.languageRepo = languageRepo;
    }


    @GetMapping("/")
    String index() {
        return "index";
    }

    @GetMapping("/wordCards")
    public String wordCards(
            @RequestParam(required = false) String filterText,
            @RequestParam(required = false) String filterTranslated,
            @RequestParam(required = false) String filterLanguageId,
            Model model
    ) {
        List<WordCard> wordCards;
        if (filterText != null && !filterText.isEmpty()) {
            wordCards = wordCardRepo.findByTextContaining(filterText);
            filterTranslated = null;
            filterLanguageId = null;
        } else if (filterTranslated != null && !filterTranslated.isEmpty()) {
            wordCards = wordCardRepo.findByTranslatedContaining(filterTranslated);
            filterLanguageId = null;
        } else if (filterLanguageId != null && !filterLanguageId.isEmpty()) {
            wordCards = wordCardRepo.findByLanguageId(Long.valueOf(filterLanguageId));
        } else {
            wordCards = wordCardRepo.findAll();
        }

        List<Language> languages = languageRepo.findAll();

        model.addAttribute("wordCards", wordCards);
        model.addAttribute("languages", languages);
        model.addAttribute("filterText", filterText);
        model.addAttribute("filterTranslated", filterTranslated);
        model.addAttribute("filterLanguageId", filterLanguageId);
        return "wordCards";
    }

    @PostMapping("/wordCards/add")
    public String wordCardsAdd(
            @RequestParam String text,
            @RequestParam String translated,
            @RequestParam String languageId
    ) {
        if (!languageId.isEmpty() && !text.isEmpty() && !translated.isEmpty()) {
            //TODO Optional.get() without 'isPresent()' ???
            Language language = languageRepo.findById(Long.valueOf(languageId)).get();
            text = text.trim();
            translated = translated.trim();
            WordCard wordCard = new WordCard();
            wordCard.setLanguage(language);
            wordCard.setText(text);
            wordCard.setTranslated(translated);
            wordCardRepo.save(wordCard);
        }
        return "redirect:/wordCards";
    }

    @GetMapping("/wordCards/removeWordCard/{id}")
    public String removeWordCard(@PathVariable("id") String id) {
        wordCardRepo.deleteById(Long.valueOf(id));
        return "redirect:/wordCards";
    }

    @GetMapping("/wordCards/editWordCard/{id}")
    public String editWordCard(@PathVariable("id") String id, Model model) {
        WordCard wordCard = wordCardRepo.findById(Long.valueOf(id)).get();
        model.addAttribute("wordCard", wordCard);
        return "wordCards";
    }

    @GetMapping("/playWords")
    public String playCards(Model model) {
        List<WordCard> wordCardsAll = wordCardRepo.findAll();
        WordCard randomWordCard = null;
        if (!wordCardsAll.isEmpty()) {
            randomWordCard = WordCardService.getRandomWordCard(wordCardsAll);
        } else {
            randomWordCard = new WordCard();
            randomWordCard.setText("No cards yet!");
            randomWordCard.setTranslated("Карт не завезли пока!");
        }
        model.addAttribute("randomWordCard", randomWordCard);
        return "playWords";
    }

    @GetMapping("/quizCards")
    public String quizzes(@RequestParam(required = false) String filterQuestion, @RequestParam(required = false) String filterLanguageId, Model model) {
        List<Quiz> quizzes;

        if (filterQuestion != null && !filterQuestion.isEmpty()) {
            quizzes = quizRepo.findByQuestionContaining(filterQuestion);
            filterLanguageId = null;
        } else if (filterLanguageId != null && !filterLanguageId.isEmpty()) {
            quizzes = quizRepo.findByLanguageId(Long.valueOf(filterLanguageId));
        } else {
            quizzes = quizRepo.findAll();
        }

        List<Language> languages = languageRepo.findAll();

        model.addAttribute("quizzes", quizzes);
        model.addAttribute("languages", languages);
        model.addAttribute("filterQuestion", filterQuestion);
        model.addAttribute("filterLanguageId", filterLanguageId);
        return "quizCards";
    }

    @GetMapping("/playQuizzes")
    public String playQuiz(Model model) {
        List<WordCard> wordCardsAll = wordCardRepo.findAll();
        Quiz quiz = null;
        if (wordCardsAll.size() >= 4) {
            quiz = QuizService.quizFromWordCard(wordCardsAll, WordCardService.getRandomWordCard(wordCardsAll));
        } else {
            quiz = new Quiz();
            quiz.setQuestion("Not enough cards yet for autoquiz!");
            quiz.setVariants(new ArrayList<>(Arrays.asList("Количества", "карточек", "пока", "не достаточно")));
        }
        model.addAttribute("quiz", quiz);
        return "playQuizzes";
    }

    @GetMapping("/languages")
    public String languages(Model model) {
        List<Language> languages = languageRepo.findAll();
        model.addAttribute("languages", languages.stream().sorted((o1, o2) -> -o2.getName().compareTo(o1.getName())).collect(Collectors.toList()));
        return "languages";
    }

    @PostMapping("/languages/add")
    public String languagesAdd(@RequestParam String name) {
        if (name != null && !name.isEmpty() && languageRepo.findByName(name).isEmpty()) {
            Language language = new Language();
            language.setName(name.trim().toLowerCase());
            languageRepo.save(language);
        }
        return "redirect:/languages";
    }

    @GetMapping("/languages/removeLanguage/{id}")
    public String removeLanguage(@PathVariable("id") String id) {
        Long longId = Long.valueOf(id);
        if (wordCardRepo.findByLanguageId(longId).isEmpty()) {
            languageRepo.delete(languageRepo.findById(Long.valueOf(id)).get());
        }
        return "redirect:/languages";
    }

    @GetMapping("/languages/editLanguage/{id}")
    public String editLanguage(@PathVariable("id") String id, Model model) {
        Language languages = languageRepo.findById(Long.valueOf(id)).get();
        model.addAttribute("languages", languages);
        return "languages";
    }

}