package ua.axel.qstn.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class WordCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Language language;

    private String question;

    @Column(length = 1024)
    private String answer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordCard wordCard = (WordCard) o;
        return language.equals(wordCard.language) &&
                question.equals(wordCard.question) &&
                answer.equals(wordCard.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, question, answer);
    }
}

