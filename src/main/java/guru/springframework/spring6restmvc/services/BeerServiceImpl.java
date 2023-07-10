package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.exceptions.BeerNotFoundException;
import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<Beer> listBeers(){
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Get Beer by Id - in service. Id: " + id.toString());

        return beerMap.get(id);
    }

    @Override
    public Beer save(Beer beer) {
        log.debug("");

        var beerId = UUID.randomUUID();
        beer.setId(beerId);
        beerMap.put(beerId, beer);
        return beer;
    }

    @Override
    public Beer updateBeerById(UUID id, Beer patch) {
        log.debug("");

        Beer existingBeer = beerMap.get(id);

        if (existingBeer == null) {
            throw new BeerNotFoundException();
        }

        if (existingBeer.getBeerName() != null) {
            existingBeer.setBeerName(patch.getBeerName());
        }
        if (existingBeer.getBeerStyle() != null) {
            existingBeer.setBeerStyle(patch.getBeerStyle());
        }
        if (existingBeer.getPrice() != null) {
            existingBeer.setPrice(patch.getPrice());
        }
        if (existingBeer.getUpc() != null) {
            existingBeer.setUpc(patch.getUpc());
        }
        if (existingBeer.getQuantityOnHand() != null) {
            existingBeer.setQuantityOnHand(patch.getQuantityOnHand());
        }
        if (existingBeer.getCreatedDate() != null) {
            existingBeer.setCreatedDate(patch.getCreatedDate());
        }
        if (existingBeer.getUpdateDate() != null) {
            existingBeer.setUpdateDate(patch.getUpdateDate());
        }
        if (existingBeer.getVersion() != null) {
            existingBeer.setVersion(patch.getVersion());
        }

        beerMap.put(existingBeer.getId(), existingBeer);
        return existingBeer;
    }

    @Override
    public Beer replaceBeerById(UUID id, Beer replacement) {
        log.debug("");

        Beer existingBeer = beerMap.get(id);

        if (existingBeer == null) {
            throw new BeerNotFoundException();
        }

        existingBeer.setBeerName(replacement.getBeerName());
        existingBeer.setBeerStyle(replacement.getBeerStyle());
        existingBeer.setVersion(replacement.getVersion());
        existingBeer.setUpc(replacement.getUpc());
        existingBeer.setPrice(replacement.getPrice());
        existingBeer.setQuantityOnHand(replacement.getQuantityOnHand());
        existingBeer.setCreatedDate(replacement.getCreatedDate());
        existingBeer.setUpdateDate(LocalDateTime.now());

        beerMap.put(existingBeer.getId(), existingBeer);
        return existingBeer;
    }

    @Override
    public void deleteById(UUID id) {
        beerMap.remove(id);
    }
}
