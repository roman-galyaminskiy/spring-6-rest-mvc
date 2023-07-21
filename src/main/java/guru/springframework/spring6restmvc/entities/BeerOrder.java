package guru.springframework.spring6restmvc.entities;

import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by jt, Spring Framework Guru.
 */

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BeerOrder extends VersionedEntity {

    @ManyToOne
    private Customer customer;

    private String customerRef;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "beerOrder")
    Set<BeerOrderLine> orderLines;

    @OneToOne(cascade = CascadeType.ALL)
    BeerOrderShipment beerOrderShipment;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getBeerOrders().add(this);
    }

    public void setBeerOrderShipment(BeerOrderShipment shipment) {
        this.beerOrderShipment = shipment;
        shipment.setBeerOrder(this);
    }
}
