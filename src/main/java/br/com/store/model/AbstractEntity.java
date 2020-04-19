package br.com.store.model;

import br.com.store.util.DateTimeDeserializer;
import br.com.store.util.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String normalizedName;
    /**
     * Indica se a entidade vai ser visível para usuário que não seja admin
     */
    @Column(columnDefinition = "boolean default true")
    private Boolean visible = true;

    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    @Column(updatable = false)
    protected DateTime created;

    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    protected DateTime updated;

    @PrePersist
    public void prePersist() {
        created = DateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updated = DateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity entity = (AbstractEntity) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
