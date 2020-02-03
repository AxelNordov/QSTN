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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    public String wordCards(
            @RequestParam(required = false) String filterText,
            @RequestParam(required = false) String filterTranslated,
            @RequestParam(required = false) String filterLanguageId,
            Model model
    ) {
        List<WordCard> wordCards;
        if (filterText != null && !filterText.isEmpty()) {
            wordCards = wordCardService.findByTextContaining(filterText);
            filterTranslated = null;
            filterLanguageId = null;
        } else if (filterTranslated != null && !filterTranslated.isEmpty()) {
            wordCards = wordCardService.findByTranslatedContaining(filterTranslated);
            filterLanguageId = null;
        } else if (filterLanguageId != null && !filterLanguageId.isEmpty()) {
            wordCards = wordCardService.findByLanguageId(filterLanguageId);
        } else {
            wordCards = wordCardService.findAll();
        }

        List<Language> languages = languageService.findAll();

        model.addAttribute("wordCards", wordCards);
        model.addAttribute("languages", languages);
        model.addAttribute("filterText", filterText);
        model.addAttribute("filterTranslated", filterTranslated);
        model.addAttribute("filterLanguageId", filterLanguageId);
        return "wordCards";
    }

    @PostMapping("/add")
    public String wordCardsAdd(
            @RequestParam String text,
            @RequestParam String translated,
            @RequestParam String languageId
    ) {
        if (!languageId.isEmpty() && !text.isEmpty() && !translated.isEmpty()) {
            Language language = languageService.findById(languageId);
            text = text.trim();
            translated = translated.trim();
            WordCard wordCard = new WordCard();
            wordCard.setLanguage(language);
            wordCard.setText(text);
            wordCard.setTranslated(translated);
            wordCardService.save(wordCard);
        }
        return "redirect:/wordCards";
    }

    @GetMapping("/removeWordCard/{id}")
    public String removeWordCard(@PathVariable("id") String id) {
        wordCardService.deleteById(id);
        return "redirect:/wordCards";
    }

    @GetMapping("/removeAllWordCards")
    public String removeAllWordCards() {
        wordCardService.deleteAll();
        return "redirect:/wordCards";
    }

    @GetMapping("/removeDuplicateWordCards")
    public String removeDuplicateWordCards() {
        //TODO Need query
        List<WordCard> wordCardsAll = wordCardService.findAll();
        Set<WordCard> wordCardSet = new HashSet<>(wordCardsAll);
        wordCardService.deleteAll();
        wordCardService.saveAll(wordCardSet);
        return "redirect:/wordCards";
    }

    @GetMapping("/wordCards/editWordCard/{id}")
    public String editWordCard(@PathVariable("id") String id, Model model) {
        WordCard wordCard = wordCardService.findById(id);
        model.addAttribute("wordCard", wordCard);
        return "wordCards";
    }

    @PostMapping("/addCardWordsFromFile")
    public String addCardWordsFromFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam String languageId
    ) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty() && !languageId.isEmpty()) {
            File fileOnDisk = FileService.writeFileOnDisk(file);
            Language language = languageService.findById(languageId);
            List<WordCard> newWordCards = FileService.parseFileToWordCards(fileOnDisk, language);
            wordCardService.saveAll(newWordCards);
        }
        return "redirect:/wordCards";
    }

}
