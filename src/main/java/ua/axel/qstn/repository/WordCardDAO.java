package ua.axel.qstn.repository;

import org.springframework.data.repository.CrudRepository;
import ua.axel.qstn.domain.WordCard;

import java.util.List;

public interface WordCardDAO extends CrudRepository<WordCard, Long> {
    @Override
    List<WordCard> findAll();

    List<WordCard> findByText(String text);
    List<WordCard> findByTextContaining(String text);
    List<WordCard> findByTranslatedContaining(String translated);
    List<WordCard> findByLanguageId(Long languageId);

//    @Modifying
//    @Query(
//            value =
//                    "insert into Users (name, age, email, status) values (:name, :age, :email, :status)",
//            nativeQuery = true)
//    void insertUser(@Param("name") String name, @Param("age") Integer age,
//                    @Param("status") Integer status, @Param("email") String email);
}
