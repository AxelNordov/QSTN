package ua.axel.qstn.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;

@Data
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Language language;

    private String question;
    private ArrayList<String> variants;
    private ArrayList<Boolean> rightVariants;
    private Boolean oneRightVariant;

}
