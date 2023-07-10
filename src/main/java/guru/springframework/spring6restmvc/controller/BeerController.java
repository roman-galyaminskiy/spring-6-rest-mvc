package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.model.BeerDTO;
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

    public List<BeerDTO> listBeers(){
        return beerService.listBeers();
    }

    @GetMapping(BEER_ID)
    public BeerDTO getBeerById(@PathVariable(BEER_ID_PATH_VARIABLE) UUID id){
        log.debug("Get Beer by Id - in controller");
        return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createNewBeer(@RequestBody BeerDTO beerDTO) {
        BeerDTO savedBeerDTO = beerService.save(beerDTO);
        return new ResponseEntity<>(savedBeerDTO.getId().toString(), HttpStatus.CREATED);
    }

    @PatchMapping(BEER_ID)
    public ResponseEntity<BeerDTO> updateBeerById(@PathVariable(BEER_ID_PATH_VARIABLE) UUID id, @RequestBody BeerDTO patch){
        return new ResponseEntity<>(beerService.updateBeerById(id, patch),
                HttpStatus.OK);
    }

    @PutMapping(BEER_ID)
    public ResponseEntity<BeerDTO> replaceBeerById(@PathVariable(BEER_ID_PATH_VARIABLE) UUID id, @RequestBody BeerDTO replacement){
        return new ResponseEntity<>(beerService.replaceBeerById(id, replacement),
                HttpStatus.OK);
    }

    @DeleteMapping(BEER_ID)
    public ResponseEntity deleteBeerById(@PathVariable(BEER_ID_PATH_VARIABLE) UUID id){
        beerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @ExceptionHandler(BeerNotFoundException.class)
    // public ResponseEntity handleBeerNotFound() {
    //     return ResponseEntity.notFound().build();
    // }

}
