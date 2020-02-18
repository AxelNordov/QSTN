package ua.axel.qstn.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ua.axel.qstn.domain.WordCard;

import javax.transaction.Transactional;
import java.util.List;

public interface WordCardDAO extends CrudRepository<WordCard, Long> {

    List<WordCard> findByQuestionContaining(String question);

    List<WordCard> findByAnswerContaining(String answer);

    List<WordCard> findByLanguageId(Long languageId);

    //For PostgreSQL
    @Modifying
    @Transactional
    @Query(
            value = "DELETE \n" +
                    "FROM word_card t1 \n" +
                    "USING word_card t2 \n" +
                    "WHERE t1.id > t2.id \n" +
                    "AND t1.question = t2.question \n" +
                    "AND t1.answer = t2.answer \n" +
                    "AND t1.language_id = t2.language_id ",
            nativeQuery = true)
    void removeDuplicateWordCards();

//    //For MySQL
//    @Modifying
//    @Transactional
//    @Query(
//            value = "DELETE t1 \n" +
//                    "FROM word_card t1 \n" +
//                    "INNER JOIN word_card t2 \n" +
//                    "WHERE t1.id > t2.id \n" +
//                    "AND t1.question = t2.question \n" +
//                    "AND t1.answer = t2.answer \n" +
//                    "AND t1.language_id = t2.language_id;",
//            nativeQuery = true)
//    void removeDuplicateWordCards();

}