package ua.axel.qstn.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;

@Data
@Entity
public class QuizCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Language language;

    private String question;
    private ArrayList<String> answers;
    private ArrayList<Boolean> rightAnswers;
    private Boolean oneRightAnswer;

}
