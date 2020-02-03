package ua.axel.qstn.repository;

import org.springframework.data.repository.CrudRepository;
import ua.axel.qstn.domain.Quiz;

import java.util.List;

public interface QuizDAO extends CrudRepository<Quiz, Long> {
    @Override
    List<Quiz> findAll();

    List<Quiz> findByQuestionContaining(String question);
    List<Quiz> findByLanguageId(Long languageId);
}