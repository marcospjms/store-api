package br.com.store.configs.security;

import br.com.store.model.auth.StoreUser;
import br.com.store.model.auth.StoreUserSpring;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManger;

    public JWTAuthenticationFilter(
            AuthenticationManager authenticationManger
    ) {
        this.authenticationManger = authenticationManger;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            StoreUser storeUser = new ObjectMapper().readValue(request.getInputStream(), StoreUser.class);

            return this.authenticationManger.authenticate(
                    new UsernamePasswordAuthenticationToken(storeUser.getUsername(), storeUser.getPassword())
            );
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authResult;

        StoreUserSpring storeUserSpring = (StoreUserSpring) authenticationToken.getPrincipal();
        StoreUser storeUser = storeUserSpring.getStoreUser();
        storeUser.setPassword("");
        String storeUserSpringJson = new ObjectMapper().writeValueAsString(storeUser);
        String token = Jwts.builder()
                .setSubject(storeUserSpringJson)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();

        response.setContentType("application/json");

        response.getWriter().write( " { \"token\" : \""+ token + "\" }");
    }
}
