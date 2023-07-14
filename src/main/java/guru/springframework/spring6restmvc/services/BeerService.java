package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
public interface BeerService {

    int DEFAULT_PAGE = 1;

    int DEFAULT_LIMIT = 10;

    int LIMIT_THRESHOLD = 1000;

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Integer page, Integer limit);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO save(BeerDTO beerDTO);

    BeerDTO updateBeerById(UUID id, BeerDTO patch);

    BeerDTO replaceBeerById(UUID id, BeerDTO replacement);

    void deleteById(UUID id);
}
