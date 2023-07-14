package guru.springframework.spring6restmvc.services.impl;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Primary
@RequiredArgsConstructor
@Service
public class BeerServiceJPAImpl implements BeerService {

    private final BeerRepository beerRepository;

    private final BeerMapper beerMapper;

    private Pageable buildPageble(Integer page, Integer limit) {
        int requestPage = page == null ? DEFAULT_PAGE : page;
        int requestPageSize = limit == null ? DEFAULT_LIMIT : limit;

        if (requestPageSize > LIMIT_THRESHOLD) {
            log.warn("limit {} is over the threshold {}", requestPageSize, LIMIT_THRESHOLD);
            requestPageSize = LIMIT_THRESHOLD;
        }

        Sort sort = Sort.by("beerName");
        return PageRequest.of(requestPage - 1, requestPageSize, sort);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Integer page, Integer limit) {
        Page<Beer> beerPage;

        Pageable pageable = buildPageble(page, limit);

        if (!StringUtils.isBlank(beerName) && beerStyle == null) {
            beerPage = beerRepository.findAllByBeerNameLike("%" + beerName + "%", pageable);
        } else if (StringUtils.isBlank(beerName) && beerStyle != null) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageable);
        } else if (!StringUtils.isBlank(beerName) && beerStyle != null) {
            beerPage = (beerRepository.findAllByBeerNameLikeAndBeerStyle("%" + beerName + "%", beerStyle, pageable));
        } else {
            beerPage = beerRepository.findAll(pageable);
        }

        return beerPage.map(beerMapper::entityToDto);
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
