package br.com.store.model.auth;


import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class StoreUserSpring extends User {

    private StoreUser storeUser;

    public StoreUserSpring(StoreUser storeUser) {
        super(storeUser.getUsername(), storeUser.getPassword(), AuthorityUtils.createAuthorityList(storeUser.getStrRoles()));
        this.storeUser = storeUser;
    }

    public StoreUser getStoreUser() {
        return storeUser;
    }
}
