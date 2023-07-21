package guru.springframework.spring6restmvc.entities;

import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class Beer extends VersionedEntity {

    @NotNull
    @Column(nullable = false)
    private String beerName;
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BeerStyle beerStyle;
    @NotNull
    @Column(nullable = false)
    private String upc;
    private Integer quantityOnHand;
    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "beer")
    Set<BeerOrderLine> beerOrderLines;

    @Builder.Default
    @ManyToMany(mappedBy = "beers")
    Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        categories.add(category);
        category.getBeers().add(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getBeers().remove(this);
    }
}
