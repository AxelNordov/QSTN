package ua.axel.qstn.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class WordCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Language language;

    private String text;
    private String translated;

}

