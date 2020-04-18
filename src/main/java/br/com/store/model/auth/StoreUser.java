package br.com.store.model.auth;

import br.com.store.model.AbstractEntity;
import br.com.store.model.ShoppingCart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class StoreUser extends AbstractEntity {

    @NotEmpty
    @Column(unique = true)
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToOne(cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

    @JsonIgnore
    public String[] getStrRoles() {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        return this.roles.stream().map(role -> "ROLE_" + role.toString()).collect(Collectors.toList()).toArray(new String[] {});
    }
}
