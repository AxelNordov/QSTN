package ua.axel.qstn.repository;

import org.springframework.data.repository.CrudRepository;
import ua.axel.qstn.domain.WordCard;

import java.util.List;

public interface WordCardRepo extends CrudRepository<WordCard, Long> {
    List<WordCard> findByLanguage(Long languageId);
    List<WordCard> findByTextContaining(String valueOf);
    List<WordCard> findByTranslatedContaining(String filterTranslated);

    List<WordCard> findByLanguageId(Long filterLanguageId);
}
