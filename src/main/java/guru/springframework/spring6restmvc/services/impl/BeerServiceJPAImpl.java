package guru.springframework.spring6restmvc.services.impl;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Primary
@RequiredArgsConstructor
@Service
public class BeerServiceJPAImpl implements BeerService {

    private final BeerRepository beerRepository;

    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle) {
        List<Beer> dtos = new ArrayList<>();

        if (StringUtils.isBlank(beerName) && beerStyle == null) {
            dtos.addAll(beerRepository.findAll());
        } else if (!StringUtils.isBlank(beerName) && beerStyle == null) {
            dtos.addAll(
                beerRepository.findAllByBeerNameLike("%" + beerName + "%"));
        } else if (!StringUtils.isBlank(beerName) && beerStyle != null) {
            dtos.addAll(
                beerRepository.findAllByBeerNameLikeAndBeerStyle("%" + beerName + "%", beerStyle));
        }

        return dtos.stream()
                .map(beerMapper::entityToDto)
                .toList();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID uuid) {
        return Optional.ofNullable(
                beerMapper.entityToDto(beerRepository.findById(uuid).orElse(null))
        );
    }

    @Override
    public BeerDTO save(BeerDTO beerDTO) {
        return beerMapper.entityToDto(beerRepository.save(beerMapper.dtoToEntity(beerDTO)));
    }

    @Override
    public BeerDTO updateBeerById(UUID id, BeerDTO patch) {
        return beerRepository.findById(id).map(beer -> {
            if (patch.getBeerName() != null) {
                beer.setBeerName(patch.getBeerName());
            }
            if (patch.getBeerStyle() != null) {
                beer.setBeerStyle(patch.getBeerStyle());
            }
            if (patch.getPrice() != null) {
                beer.setPrice(patch.getPrice());
            }
            if (patch.getUpc() != null) {
                beer.setUpc(patch.getUpc());
            }
            if (patch.getQuantityOnHand() != null) {
                beer.setQuantityOnHand(patch.getQuantityOnHand());
            }
            beerRepository.save(beer);
            return beerMapper.entityToDto(beer);
        }).orElseThrow(NotFoundException::new);
    }

    @Override
    public BeerDTO replaceBeerById(UUID id, BeerDTO replacement) {
        return beerRepository.findById(id).map(
                beer -> {
                    beer.setBeerName(replacement.getBeerName());
                    beer.setBeerStyle(replacement.getBeerStyle());
                    beer.setUpc(replacement.getUpc());
                    beer.setQuantityOnHand(replacement.getQuantityOnHand());
                    beer.setPrice(replacement.getPrice());
                    beerRepository.save(beer);
                    return beerMapper.entityToDto(beer);
                }).orElseThrow(NotFoundException::new);
    }

    @Override
    public void deleteById(UUID id) {
        beerRepository.deleteById(id);
    }
}
