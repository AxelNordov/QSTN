package ua.axel.qstn.repository;

import org.springframework.data.repository.CrudRepository;
import ua.axel.qstn.domain.WordCard;

import java.util.List;

public interface WordCardRepo extends CrudRepository<WordCard, Long> {
    @Override
    List<WordCard> findAll();

    List<WordCard> findByTextContaining(String text);
    List<WordCard> findByTranslatedContaining(String translated);
    List<WordCard> findByLanguageId(Long languageId);
}
