package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    Set<Beer> findAllByBeerNameLike(String pattern);

    Set<Beer> findAllByBeerNameLikeAndBeerStyle(String pattern, BeerStyle beerStyle);
}
