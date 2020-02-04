package ua.axel.qstn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.domain.WordCard;
import ua.axel.qstn.service.FileService;
import ua.axel.qstn.service.LanguageService;
import ua.axel.qstn.service.WordCardService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value = "/wordCards")
public class WordCardsController {

    private final WordCardService wordCardService;
    private final LanguageService languageService;

    @Autowired
    public WordCardsController(WordCardService wordCardService, LanguageService languageService) {
        this.wordCardService = wordCardService;
        this.languageService = languageService;
    }

    @GetMapping
    public String wordCards(@RequestParam(required = false) String filterQuestion, @RequestParam(required = false) String filterAnswer, @RequestParam(required = false) Long filterLanguageId, Model model) {
        List<WordCard> wordCards;
        if (filterQuestion != null && !filterQuestion.isEmpty()) {
            wordCards = wordCardService.findByQuestionContaining(filterQuestion);
            filterAnswer = null;
            filterLanguageId = null;
        } else if (filterAnswer != null && !filterAnswer.isEmpty()) {
            wordCards = wordCardService.findByAnswerContaining(filterAnswer);
            filterLanguageId = null;
        } else if (filterLanguageId != null) {
            wordCards = wordCardService.findByLanguageId(filterLanguageId);
        } else {
            wordCards = wordCardService.findAll();
        }

        final int NUMBER_OF_ITEMS = 256;
        if (wordCards.size() > NUMBER_OF_ITEMS) {
            wordCards = wordCards.subList(wordCards.size() - NUMBER_OF_ITEMS, wordCards.size());
        }

        List<Language> languages = languageService.findAll();

        model.addAttribute("wordCards", wordCards);
        model.addAttribute("languages", languages);
        model.addAttribute("filterQuestion", filterQuestion);
        model.addAttribute("filterAnswer", filterAnswer);
        model.addAttribute("filterLanguageId", filterLanguageId);
        return "wordCards";
    }

    @GetMapping("/play")
    public String play(@RequestParam(required = false) Long filterLanguageId, Model model) {
        List<Language> languages = languageService.findAll();
        WordCard wordCard;
        if (filterLanguageId != null) {
            wordCard = wordCardService.getRandomWordCard(wordCardService.findByLanguageId(filterLanguageId));
        } else {
            wordCard = wordCardService.getRandomWordCard(wordCardService.findAll());
        }

        model.addAttribute("languages", languages);
        model.addAttribute("filterLanguageId", filterLanguageId);
        model.addAttribute("wordCard", wordCard);
        return "wordCardsPlay";
    }

    @PostMapping("/add")
    public String add(@RequestParam String question, @RequestParam String answer, @RequestParam Long languageId) {
        if (languageId != null && !question.isEmpty() && !answer.isEmpty()) {
            Language language = languageService.findById(languageId);
            question = question.trim();
            answer = answer.trim();
            WordCard wordCard = new WordCard();
            wordCard.setLanguage(language);
            wordCard.setQuestion(question);
            wordCard.setAnswer(answer);
            wordCardService.save(wordCard);
        }
        return "redirect:/wordCards";
    }

    @PostMapping("/addFromFile")
    public String addFromFile(@RequestParam("file") MultipartFile file, @RequestParam Long languageId) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty() && languageId != null) {
            File fileOnDisk = FileService.writeFileOnDisk(file);
            Language language = languageService.findById(languageId);
            List<WordCard> newWordCards = FileService.parseFileToWordCards(fileOnDisk, language);
            wordCardService.saveAll(newWordCards);
        }
        return "redirect:/wordCards";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Long id) {
        wordCardService.deleteById(id);
        return "redirect:/wordCards";
    }

    @GetMapping("/removeDuplicates")
    public String removeDuplicates() {
        wordCardService.removeDuplicateWordCards();
        return "redirect:/wordCards";
    }

    @GetMapping("/removeAll")
    public String removeAll() {
        wordCardService.deleteAll();
        return "redirect:/wordCards";
    }

    @PostMapping("/edit")
    public String edit(WordCard wordCard, @RequestParam Long languageId) {
        //TODO Костыль. Непонятно как передать language
        wordCard.setLanguage(languageService.findById(languageId));
        wordCardService.save(wordCard);
        return "redirect:/wordCards";
    }

    @GetMapping(value = "/find/{id}")
    @ResponseBody
    public WordCard find(@PathVariable("id") Long id) {
        return wordCardService.findById(id);
    }

}
