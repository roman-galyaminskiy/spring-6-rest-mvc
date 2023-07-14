package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
public interface BeerService {

    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO save(BeerDTO beerDTO);

    BeerDTO updateBeerById(UUID id, BeerDTO patch);

    BeerDTO replaceBeerById(UUID id, BeerDTO replacement);

    void deleteById(UUID id);
}
