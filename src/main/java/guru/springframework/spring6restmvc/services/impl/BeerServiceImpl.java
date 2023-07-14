package guru.springframework.spring6restmvc.services.impl;

import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beerDTO1 = BeerDTO.builder()
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

        BeerDTO beerDTO2 = BeerDTO.builder()
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

        BeerDTO beerDTO3 = BeerDTO.builder()
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

        beerMap.put(beerDTO1.getId(), beerDTO1);
        beerMap.put(beerDTO2.getId(), beerDTO2);
        beerMap.put(beerDTO3.getId(), beerDTO3);
    }

    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle) {
        Stream<BeerDTO> stream = beerMap.values().stream();
        if (StringUtils.isBlank(beerName)) {
            stream = stream.filter(beerDTO -> beerDTO.getBeerName().matches(
                    beerName.replace('%', '*')));
        }
        if (beerStyle == null) {
            stream = stream.filter(beerDTO -> beerDTO.getBeerStyle() == beerStyle);
        }

        return stream.toList();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("Get Beer by Id - in service. Id: " + id.toString());

        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO save(BeerDTO beerDTO) {
        log.debug("");

        var beerId = UUID.randomUUID();
        beerDTO.setId(beerId);
        beerMap.put(beerId, beerDTO);
        return beerDTO;
    }

    @Override
    public BeerDTO updateBeerById(UUID id, BeerDTO patch) {
        log.debug("");

        BeerDTO existingBeerDTO = beerMap.get(id);

        if (existingBeerDTO == null) {
            throw new NotFoundException();
        }

        if (existingBeerDTO.getBeerName() != null) {
            existingBeerDTO.setBeerName(patch.getBeerName());
        }
        if (existingBeerDTO.getBeerStyle() != null) {
            existingBeerDTO.setBeerStyle(patch.getBeerStyle());
        }
        if (existingBeerDTO.getPrice() != null) {
            existingBeerDTO.setPrice(patch.getPrice());
        }
        if (existingBeerDTO.getUpc() != null) {
            existingBeerDTO.setUpc(patch.getUpc());
        }
        if (existingBeerDTO.getQuantityOnHand() != null) {
            existingBeerDTO.setQuantityOnHand(patch.getQuantityOnHand());
        }
        if (existingBeerDTO.getCreatedDate() != null) {
            existingBeerDTO.setCreatedDate(patch.getCreatedDate());
        }
        if (existingBeerDTO.getUpdateDate() != null) {
            existingBeerDTO.setUpdateDate(patch.getUpdateDate());
        }
        if (existingBeerDTO.getVersion() != null) {
            existingBeerDTO.setVersion(patch.getVersion());
        }

        beerMap.put(existingBeerDTO.getId(), existingBeerDTO);
        return existingBeerDTO;
    }

    @Override
    public BeerDTO replaceBeerById(UUID id, BeerDTO replacement) {
        log.debug("");

        BeerDTO existingBeerDTO = beerMap.get(id);

        if (existingBeerDTO == null) {
            throw new NotFoundException();
        }

        existingBeerDTO.setBeerName(replacement.getBeerName());
        existingBeerDTO.setBeerStyle(replacement.getBeerStyle());
        existingBeerDTO.setVersion(replacement.getVersion());
        existingBeerDTO.setUpc(replacement.getUpc());
        existingBeerDTO.setPrice(replacement.getPrice());
        existingBeerDTO.setQuantityOnHand(replacement.getQuantityOnHand());
        existingBeerDTO.setCreatedDate(replacement.getCreatedDate());
        existingBeerDTO.setUpdateDate(LocalDateTime.now());

        beerMap.put(existingBeerDTO.getId(), existingBeerDTO);
        return existingBeerDTO;
    }

    @Override
    public void deleteById(UUID id) {
        beerMap.remove(id);
    }
}
