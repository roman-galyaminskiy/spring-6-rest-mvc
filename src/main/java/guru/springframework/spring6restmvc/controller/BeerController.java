package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.exceptions.NotFoundException;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.services.BeerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<String> createNewBeer(@Valid @RequestBody BeerDTO beerDTO) {
        BeerDTO savedBeerDTO = beerService.save(beerDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_API_URL + "/" + savedBeerDTO.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PatchMapping(BEER_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBeerById(@Valid @PathVariable(BEER_ID_PATH_VARIABLE) UUID id, @RequestBody BeerDTO patch){
        beerService.updateBeerById(id, patch);
    }

    @PutMapping(BEER_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void replaceBeerById(@Valid @PathVariable(BEER_ID_PATH_VARIABLE) UUID id, @RequestBody BeerDTO replacement){
        BeerDTO beerDTO = beerService.replaceBeerById(id, replacement);

        if (beerDTO == null) {
            throw new NotFoundException();
        }
    }

    @DeleteMapping(BEER_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeerById(@PathVariable(BEER_ID_PATH_VARIABLE) UUID id){
        beerService.deleteById(id);
    }

    // @ExceptionHandler(BeerNotFoundException.class)
    // public ResponseEntity handleBeerNotFound() {
    //     return ResponseEntity.notFound().build();
    // }

    // @DeleteMapping()
    // @ResponseStatus(HttpStatus.OK)
    // public void deleteByBody(@RequestBody BeerDTO test) {
    //     System.out.println(test.getId());
    // }

}
