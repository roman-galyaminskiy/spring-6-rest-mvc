package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(BeerController.BEER_API_URL)
public class BeerController {
    public static final String BEER_API_URL = "/api/v1/beer";
    public static final String BEER_ID = "/{beerId}";
    public static final String BEER_ID_PATH_VARIABLE = "beerId";
    private final BeerService beerService;

    @GetMapping({"", "/"})

    public List<Beer> listBeers(){
        return beerService.listBeers();
    }

    @GetMapping(BEER_ID)
    public Beer getBeerById(@PathVariable(BEER_ID_PATH_VARIABLE) UUID id){
        log.debug("Get Beer by Id - in controller");
        return beerService.getBeerById(id);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createNewBeer(@RequestBody Beer beer) {
        Beer savedBeer = beerService.save(beer);
        return new ResponseEntity<>(savedBeer.getId().toString(), HttpStatus.CREATED);
    }

    @PatchMapping(BEER_ID)
    public ResponseEntity<Beer> updateBeerById(@PathVariable(BEER_ID_PATH_VARIABLE) UUID id, @RequestBody Beer patch){
        return new ResponseEntity<>(beerService.updateBeerById(id, patch),
                HttpStatus.OK);
    }

    @PutMapping(BEER_ID)
    public ResponseEntity<Beer> replaceBeerById(@PathVariable(BEER_ID_PATH_VARIABLE) UUID id, @RequestBody Beer replacement){
        return new ResponseEntity<>(beerService.replaceBeerById(id, replacement),
                HttpStatus.OK);
    }

    @DeleteMapping(BEER_ID)
    public ResponseEntity deleteBeerById(@PathVariable(BEER_ID_PATH_VARIABLE) UUID id){
        beerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
