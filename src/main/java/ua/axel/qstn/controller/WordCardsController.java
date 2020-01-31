package ua.axel.qstn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.domain.WordCard;
import ua.axel.qstn.repository.LanguageRepo;
import ua.axel.qstn.repository.QuizRepo;
import ua.axel.qstn.repository.WordCardRepo;
import ua.axel.qstn.service.FileService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/wordCards")
public class WordCardsController {
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


//    @Value("${upload.path}")
//    private String uploadPath;

    @GetMapping
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

    @PostMapping("/add")
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

    @GetMapping("/removeWordCard/{id}")
    public String removeWordCard(@PathVariable("id") String id) {
        wordCardRepo.deleteById(Long.valueOf(id));
        return "redirect:/wordCards";
    }

    @GetMapping("/removeAllWordCards")
    public String removeAllWordCards() {
        wordCardRepo.deleteAll();
        return "redirect:/wordCards";
    }

    @GetMapping("/removeDuplicateWordCards")
    public String removeDuplicateWordCards() {
        List<WordCard> allWordCards = wordCardRepo.findAll();
//        for (int i = allWordCards.size() - 1; i > 0; i--) {
//            for (int j = i - 1; i >= 0; i--) {
//                if (allWordCards.get(i).equals(allWordCards.get(j))) {
//                    wordCardRepo.deleteById(allWordCards.get(j).getId());
//                }
//            }
//        }

        Iterator iterator = allWordCards.iterator();
        while (iterator.hasNext()) {
            ArrayList<WordCard> wordCardsByText = (ArrayList<WordCard>) wordCardRepo.findByText(((WordCard) iterator.next()).getText());
            for (int i = wordCardsByText.size() - 1; i >= 0; i--) {
                WordCard iteratorNext = (WordCard) iterator.next();
                if (wordCardsByText.get(i) != iteratorNext
                        && wordCardsByText.get(i).equals(iteratorNext)
//                        && wordCardsByText.get(i).getTranslated().equals((iteratorNext).getTranslated())
//                        && wordCardsByText.get(i).getLanguage().equals((iteratorNext).getLanguage())
                ) {
                    wordCardRepo.deleteById(wordCardsByText.get(i).getId());
                }
            }
        }

        return "redirect:/wordCards";
    }

    @GetMapping("/wordCards/editWordCard/{id}")
    public String editWordCard(@PathVariable("id") String id, Model model) {
        WordCard wordCard = wordCardRepo.findById(Long.valueOf(id)).get();
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

            Language language = languageRepo.findById(Long.valueOf(languageId)).get();
            List<WordCard> newWordCards = FileService.parseFileToWordCards(fileOnDisk, language);

            wordCardRepo.saveAll(newWordCards);
        }
        return "redirect:/wordCards";
    }

}
