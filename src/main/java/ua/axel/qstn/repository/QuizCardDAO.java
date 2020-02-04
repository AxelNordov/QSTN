package ua.axel.qstn.repository;

import org.springframework.data.repository.CrudRepository;
import ua.axel.qstn.domain.QuizCard;

import java.util.List;

public interface QuizCardDAO extends CrudRepository<QuizCard, Long> {

    List<QuizCard> findByQuestionContaining(String question);

    List<QuizCard> findByLanguageId(Long languageId);

}