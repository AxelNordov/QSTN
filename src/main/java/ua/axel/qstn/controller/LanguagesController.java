package ua.axel.qstn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.service.LanguageService;

import java.util.List;

@Controller
@RequestMapping(value = "/languages")
public class LanguagesController {

    private final LanguageService languageService;

    @Autowired
    public LanguagesController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public String languages(Model model) {
        List<Language> languages = languageService.findAll();
        model.addAttribute("languages", languages);
        return "languages";
    }

    @PostMapping("/add")
    public String languagesAdd(@RequestParam String name) {
        languageService.save(name);
        return "redirect:/languages";
    }

    @GetMapping("/removeLanguage/{id}")
    public String removeLanguage(@PathVariable("id") String id) {
        languageService.deleteById(id);
        return "redirect:/languages";
    }

    @GetMapping("/editLanguage/{id}")
    public String editLanguage(@PathVariable("id") String id, Model model) {
        Language languages = languageService.findById(id);
        model.addAttribute("languages", languages);
        return "languages";
    }

}
