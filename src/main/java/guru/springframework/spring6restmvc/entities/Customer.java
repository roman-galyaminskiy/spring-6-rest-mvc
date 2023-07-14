package guru.springframework.spring6restmvc.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
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
public class Customer extends VersionedEntity {
    private String name;
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Email
    private String email;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
