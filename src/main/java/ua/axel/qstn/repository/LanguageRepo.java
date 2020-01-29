package ua.axel.qstn.repository;

import org.springframework.data.repository.CrudRepository;
import ua.axel.qstn.domain.Language;

import java.util.List;

public interface LanguageRepo extends CrudRepository<Language, Long> {
    @Override
    List<Language> findAll();

    List<Language> findByName(String name);
}
