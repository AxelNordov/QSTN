package ua.axel.qstn.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "myOption")
public class Option {

    @Id
    @GeneratedValue
    private Long id;

//    @Column(unique = true)
    private String value;

}
