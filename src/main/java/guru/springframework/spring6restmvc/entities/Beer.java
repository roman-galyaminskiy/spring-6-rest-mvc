package guru.springframework.spring6restmvc.entities;

import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @Column(unique = true, nullable = false)
    private String beerName;
    @NotNull
    @Column(nullable = false)
    private BeerStyle beerStyle;
    @NotNull
    @Column(nullable = false)
    private String upc;
    private Integer quantityOnHand;
    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    // public Beer() {
    //     super();
    //     System.out.println("Beer constructor");
    //     System.out.println(super.getId());
    // }
    //
    // @Builder
    // public Beer(Integer version, String beerName, BeerStyle beerStyle, String upc, Integer quantityOnHand, BigDecimal price, LocalDateTime createdDate, LocalDateTime updateDate) {
    //     super(version);
    //     this.beerName = beerName;
    //     this.beerStyle = beerStyle;
    //     this.upc = upc;
    //     this.quantityOnHand = quantityOnHand;
    //     this.price = price;
    //     this.createdDate = createdDate;
    //     this.updateDate = updateDate;
    // }
}
