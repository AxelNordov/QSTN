package ua.axel.qstn.domain;

import javax.persistence.*;

@Entity
public class WordCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;
    private String translated;

    @ManyToOne
    private Language language;

    public WordCard() {
    }

    public WordCard(Language language, String text, String translated) {
        this.language = language;
        this.text = text;
        this.translated = translated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }
}
