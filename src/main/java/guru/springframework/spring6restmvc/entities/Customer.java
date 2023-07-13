package guru.springframework.spring6restmvc.entities;

import guru.springframework.spring6restmvc.model.BeerStyle;
// import jakarta.persistence.Entity;
import jakarta.persistence.Entity;
import lombok.*;
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
public class Customer extends VersionedEntity {
    private String name;
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
