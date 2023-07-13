package guru.springframework.spring6restmvc.entities;

import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.persistence.*;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public class BaseEntity {

    @Id
    @GeneratedValue
    @Column(length = 16, columnDefinition = "varchar", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    protected UUID id;


    // protected BaseEntity() {
    //     System.out.println("BaseEntity constructor");
    //     id = UUID.randomUUID();
    //     System.out.println(id);
    // }

    // find it in the book
    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;
    //
    //     BaseEntity that = (BaseEntity) o;
    //
    //     return id.equals(that.id);
    // }
    //
    // @Override
    // public int hashCode() {
    //     return id.hashCode();
    // }
}
