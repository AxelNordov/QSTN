package ua.axel.qstn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.domain.Option;
import ua.axel.qstn.domain.QuizCard;
import ua.axel.qstn.domain.WordCard;
import ua.axel.qstn.service.LanguageService;
import ua.axel.qstn.service.QuizCardService;
import ua.axel.qstn.service.WordCardService;

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
                quizCard.setOptions(quizCardService.stringsToOptions(Arrays.asList("Количества", "карточек", "пока", "недостаточно")));
            }
        } else {
            wordCards = wordCardService.findAll();
            if (wordCards.size() >= 4) {
                quizCard = quizCardService.quizCardFromWordCard(wordCards, wordCardService.getRandomWordCard());
            } else {
                quizCard = new QuizCard();
                quizCard.setQuestion("Not enough cards yet for autoquizcard!");
                quizCard.setOptions(quizCardService.stringsToOptions(Arrays.asList("Количества", "карточек", "пока", "недостаточно")));
            }
        }

        List<Boolean> rightBooleanOptions = QuizCardService.getRightBooleanOptions(quizCard);

        model.addAttribute("languages", languages);
        model.addAttribute("filterLanguageId", filterLanguageId);
        model.addAttribute("rightBooleanOptions", rightBooleanOptions);
        model.addAttribute("quizCard", quizCard);
        return "quizCardsPlay";
    }

    @PostMapping("/edit")
    public String edit(QuizCard quizCard, @RequestParam Long languageId, @RequestParam List<String> stringOptions) {
        //TODO Костыль. Непонятно как передать language
        if (languageId != null) {
            quizCard.setLanguage(languageService.findById(languageId));
            List<Option> options = quizCardService.stringsToOptions(stringOptions);
            quizCard.setOptions(options);
            quizCard.setOneRightOptionRadio(true);
            quizCard.setRightOptions(1);
            quizCardService.save(quizCard);
        }
        return "redirect:/quizCards";
    }

    @GetMapping(value = "/find/{id}")
    @ResponseBody
    public QuizCard find(@PathVariable("id") Long id) {
        return quizCardService.findById(id);
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Long id) {
        quizCardService.deleteById(id);
        return "redirect:/quizCards";
    }


}
