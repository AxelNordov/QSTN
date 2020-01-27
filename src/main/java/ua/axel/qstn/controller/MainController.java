package ua.axel.qstn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.domain.WordCard;
import ua.axel.qstn.repository.LanguageRepo;
import ua.axel.qstn.repository.WordCardRepo;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    private WordCardRepo wordCardRepo;

    @Autowired
    private LanguageRepo languageRepo;

    @GetMapping("/")
    String index() {
        return "index";
    }

    @GetMapping("/wordCards")
    public String wordCards(
            @RequestParam(required = false) String filterText,
            @RequestParam(required = false) String filterTranslated,
            @RequestParam(required = false) String filterLanguageId, Model model
    ) {
        Iterable<WordCard> wordCards = wordCardRepo.findAll();
        Iterable<Language> languages = languageRepo.findAll();

        if (filterText != null && !filterText.isEmpty()) {
            wordCards = wordCardRepo.findByTextContaining(filterText);
        } else if (filterTranslated != null && !filterTranslated.isEmpty()) {
            wordCards = wordCardRepo.findByTranslatedContaining(filterTranslated);
        } else if (filterLanguageId != null && !filterLanguageId.isEmpty()) {
            wordCards = wordCardRepo.findByLanguageId(Long.valueOf(filterLanguageId));
        }

        model.addAttribute("wordCards", wordCards);
        model.addAttribute("languages", languages);
        return "wordCards";
    }

    @PostMapping("/wordCards/add")
    public String add(
            @RequestParam String text,
            @RequestParam String translated,
            @RequestParam String languageId
    ) {
        if (!languageId.equals("Язык...") && !languageId.isEmpty()) {
            Language language = languageRepo.findById(Long.valueOf(languageId)).get();
            text = text.trim();
            translated = translated.trim();
            if (!text.isEmpty()) {
                WordCard wordCard = new WordCard(language, text, translated);
                wordCardRepo.save(wordCard);
            }
        }
        return "redirect:/wordCards";
    }

    @GetMapping("/wordCards/removeWordCard/{id}")
    public String removeWordCard(@PathVariable("id") String id) {
        wordCardRepo.deleteById(Long.valueOf(id));
//        wordCardRepo.delete(wordCardRepo.findById(Long.valueOf(id)));
        return "redirect:/wordCards";
    }

    @GetMapping("/wordCards/editWordCard/{id}")
    public String editWordCard(@PathVariable("id") String id, Model model) {
        WordCard wordCard = wordCardRepo.findById(Long.valueOf(id)).get();
        model.addAttribute("wordCard", wordCard);
        return "wordCards";
    }

    @GetMapping("/playCards")
    public String playCards(Model model) {
        Random rand = new Random();
        List<WordCard> wordCardRepoAll = (List<WordCard>) wordCardRepo.findAll();
        WordCard randomWordCard = wordCardRepoAll.get(rand.nextInt(wordCardRepoAll.size()));
        model.addAttribute("randomWordCard", randomWordCard);
        return "playCards";
    }

    @GetMapping("/playQuiz")
    public String playQuiz(Model model) {

        return "playQuiz";
    }

    @GetMapping("/languages")
    public String languages(Model model) {
        List<Language> languages = (List<Language>) languageRepo.findAll();
        model.addAttribute("languages", languages.stream().sorted((o1, o2) -> -o2.getName().compareTo(o1.getName())).collect(Collectors.toList()));
        return "languages";
    }

    @PostMapping("/languages/add")
    public String add(@RequestParam String name) {
        if (name != null && !name.isEmpty() && ((List<Language>) languageRepo.findByName(name)).isEmpty()) {
            Language language = new Language(name.trim().toLowerCase());
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