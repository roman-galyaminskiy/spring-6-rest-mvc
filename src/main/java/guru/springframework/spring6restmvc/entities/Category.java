package guru.springframework.spring6restmvc.entities;

import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jt, Spring Framework Guru.
 */

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Category extends VersionedEntity {

    @NotNull
    @Column(nullable = false)
    private String description;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "beer_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id"))
    Set<Beer> beers = new HashSet<>();
}
