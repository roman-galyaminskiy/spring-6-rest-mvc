package guru.springframework.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Created by jt, Spring Framework Guru.
 */

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BeerOrderLine extends VersionedEntity {

    @ManyToOne
    private Beer beer;

    @ManyToOne
    private BeerOrder beerOrder;

    private Integer orderQuantity;

    private Integer quantityAllocated;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;
}
