package guru.springframework.spring6restmvc.entities;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Created by jt, Spring Framework Guru.
 */

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public class VersionedEntity extends BaseEntity {

    @Version
    protected Integer version;
}
