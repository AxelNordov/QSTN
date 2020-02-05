package ua.axel.qstn.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class QuizCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Language language;

    private String question;

    @OneToMany
    private List<Option> options;

    private int rightOptions;
    private int numberOfOptions;
    private Boolean oneRightOptionRadio;

}
