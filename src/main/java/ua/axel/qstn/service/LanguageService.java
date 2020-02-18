package ua.axel.qstn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.repository.LanguageDAO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LanguageService {

    final private LanguageDAO languageDAO;
    final private WordCardService wordCardService;
    final private QuizCardService quizCardService;

    @Autowired
    public LanguageService(LanguageDAO languageDAO, WordCardService wordCardService, QuizCardService quizCardService) {
        this.languageDAO = languageDAO;
        this.wordCardService = wordCardService;
        this.quizCardService = quizCardService;
    }

    public Language findById(Long id) {
        //TODO Optional.get() without 'isPresent()' ???
        return languageDAO.findById(id).get();
    }

    public List<Language> findAll() {
        return StreamSupport.stream(languageDAO.findAll().spliterator(), false).sorted((o1, o2) -> o2.getName().compareTo(o1.getName())).collect(Collectors.toList());
    }

    public List<Language> findByName(String name) {
        return languageDAO.findByName(name);
    }

    public void save(Language language) {
        languageDAO.save(language);
    }

    public void save(String name) {
        if (name != null && !name.isEmpty() && findByName(name).isEmpty()) {
            Language language = new Language();
            language.setName(name.trim());
            languageDAO.save(language);
        }
    }

    public void deleteById(Long id) {
        if (wordCardService.findByLanguageId(id).isEmpty() && quizCardService.findByLanguageId(id).isEmpty()) {
            languageDAO.deleteById(id);
        }
    }

    public void delete(Language language) {
        languageDAO.delete(language);
    }

}
