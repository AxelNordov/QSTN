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
    public String add(@RequestParam String name) {
        languageService.save(name);
        return "redirect:/languages";
    }

    @PostMapping("/edit")
    public String edit(Language language) {
        languageService.save(language);
        return "redirect:/languages";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Long id) {
        languageService.deleteById(id);
        return "redirect:/languages";
    }

    @GetMapping(value = "/find/{id}")
    @ResponseBody
    public Language find(@PathVariable("id") Long id) {
        return languageService.findById(id);
    }

}
