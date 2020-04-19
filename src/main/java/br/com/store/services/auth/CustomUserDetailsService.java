package br.com.store.services.auth;

import br.com.store.model.auth.StoreUser;
import br.com.store.model.auth.StoreUserSpring;
import br.com.store.services.StoreUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private StoreUserService storeUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StoreUser user = Optional.ofNullable(this.storeUserService.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new StoreUserSpring(user);
    }
}
