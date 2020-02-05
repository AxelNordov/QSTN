package ua.axel.qstn.service;

import org.springframework.stereotype.Service;
import ua.axel.qstn.domain.Option;
import ua.axel.qstn.repository.OptionDAO;

@Service
public class OptionService {

    private final OptionDAO optionDAO;

    public OptionService(OptionDAO optionDAO) {
        this.optionDAO = optionDAO;
    }

    public void save(Option option) {
        //TODO Почему @Column(unique = true) в Option само не решает этот вопрос? а с этим не привязывает к уже существующему Option
//        if (!optionDAO.existsByValue(option.getValue())) {
        optionDAO.save(option);
//        }
    }

}
