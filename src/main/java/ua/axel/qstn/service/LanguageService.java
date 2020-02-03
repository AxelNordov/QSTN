package ua.axel.qstn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.axel.qstn.domain.Language;
import ua.axel.qstn.repository.LanguageDAO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LanguageService {

    private LanguageDAO languageDAO;
    private WordCardService wordCardService;

    @Autowired
    public LanguageService(LanguageDAO languageDAO, WordCardService wordCardService) {
        this.languageDAO = languageDAO;
        this.wordCardService = wordCardService;
    }

    public List<Language> findAll() {
        return languageDAO.findAll().stream().sorted((o1, o2) -> o2.getName().compareTo(o1.getName())).collect(Collectors.toList());
    }

    public void save(String name) {
        if (name != null && !name.isEmpty() && findByName(name).isEmpty()) {
            Language language = new Language();
            language.setName(name.trim().toLowerCase());
            languageDAO.save(language);
        }
    }

    private List<Language> findByName(String name) {
        return languageDAO.findByName(name);
    }

    public void delete(Language language) {
        languageDAO.delete(language);
    }

    public void deleteById(String stringId) {
        if (wordCardService.findByLanguageId(stringId).isEmpty()) {
            languageDAO.deleteById(Long.valueOf(stringId));
        }
    }

    public Language findById(String stringId) {
        //TODO Optional.get() without 'isPresent()' ???
        return languageDAO.findById(Long.valueOf(stringId)).get();
    }
}
