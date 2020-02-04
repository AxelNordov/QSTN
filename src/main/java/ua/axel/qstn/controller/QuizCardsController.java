package ua.axel.qstn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.domain.QuizCard;
import ua.axel.qstn.domain.WordCard;
import ua.axel.qstn.service.LanguageService;
import ua.axel.qstn.service.QuizCardService;
import ua.axel.qstn.service.WordCardService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/quizCards")
public class QuizCardsController {

    private final QuizCardService quizCardService;
    private final WordCardService wordCardService;
    private final LanguageService languageService;

    @Autowired
    public QuizCardsController(QuizCardService quizCardService, WordCardService wordCardService, LanguageService languageService) {
        this.quizCardService = quizCardService;
        this.wordCardService = wordCardService;
        this.languageService = languageService;
    }

    @GetMapping
    public String quizzes(
            @RequestParam(required = false) String filterQuestion,
            @RequestParam(required = false) Long filterLanguageId,
            Model model
    ) {
        List<QuizCard> quizCards;

        if (filterQuestion != null && !filterQuestion.isEmpty()) {
            quizCards = quizCardService.findByQuestionContaining(filterQuestion);
            filterLanguageId = null;
        } else if (filterLanguageId != null) {
            quizCards = quizCardService.findByLanguageId(filterLanguageId);
        } else {
            quizCards = quizCardService.findAll();
        }

        List<Language> languages = languageService.findAll();

        model.addAttribute("quizCards", quizCards);
        model.addAttribute("languages", languages);
        model.addAttribute("filterQuestion", filterQuestion);
        model.addAttribute("filterLanguageId", filterLanguageId);
        return "quizCards";
    }

    @GetMapping("/play")
    public String playQuizCards(
            @RequestParam(required = false) Long filterLanguageId,
            Model model
    ) {
        List<Language> languages = languageService.findAll();

        List<WordCard> wordCards;
        QuizCard quizCard;
        if (filterLanguageId != null) {
            wordCards = wordCardService.findByLanguageId(filterLanguageId);
            if (wordCards.size() >= 4) {
                quizCard = quizCardService.quizCardFromWordCard(wordCards, wordCardService.getRandomWordCard(filterLanguageId));
            } else {
                quizCard = new QuizCard();
                quizCard.setQuestion("Not enough cards yet for autoquizcard!");
                quizCard.setAnswers(new ArrayList<>(Arrays.asList("Количества", "карточек", "пока", "не достаточно")));
            }
        } else {
            wordCards = wordCardService.findAll();
            if (wordCards.size() >= 4) {
                quizCard = quizCardService.quizCardFromWordCard(wordCards, wordCardService.getRandomWordCard());
            } else {
                quizCard = new QuizCard();
                quizCard.setQuestion("Not enough cards yet for autoquizcard!");
                quizCard.setAnswers(new ArrayList<>(Arrays.asList("Количества", "карточек", "пока", "не достаточно")));
            }
        }

        model.addAttribute("languages", languages);
        model.addAttribute("filterLanguageId", filterLanguageId);
        model.addAttribute("quizCard", quizCard);
        return "quizCardsPlay";
    }

    @PostMapping("/edit")
    public String edit(QuizCard quizCard, @RequestParam Long languageId) {
        //TODO Костыль. Непонятно как передать language
        quizCard.setLanguage(languageService.findById(languageId));
        quizCardService.save(quizCard);
        return "redirect:/quizCards";
    }

}
