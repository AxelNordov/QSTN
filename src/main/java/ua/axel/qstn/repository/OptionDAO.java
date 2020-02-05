package ua.axel.qstn.repository;

import org.springframework.data.repository.CrudRepository;
import ua.axel.qstn.domain.Option;

public interface OptionDAO extends CrudRepository<Option, Long> {

    boolean existsByValue(String value);
}