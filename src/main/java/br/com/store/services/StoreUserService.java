package br.com.store.services;

import br.com.store.model.auth.Role;
import br.com.store.model.auth.StoreUser;
import br.com.store.repositories.StoreUserRepository;
import br.com.store.util.AbstractEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class StoreUserService {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private StoreUserRepository repository;

    public StoreUser createUser(StoreUser storeUser) {
        storeUser.setId(null);
        storeUser.setRoles(new HashSet<Role>(){{add(Role.CUSTOMER);}});
        storeUser.setPassword(this.encoder.encode(storeUser.getPassword()));
        AbstractEntityUtil.normalizeEntity(storeUser);
        return repository.save(storeUser);
    }

    /**
     * Evita alterações não permitidas nos dados dos usuários
     */
    public StoreUser updateFromStoreUser(String username, StoreUser storeUser) {
        StoreUser usuarioSaved = this.findByUsername(username);

        storeUser.setId(usuarioSaved.getId());
        storeUser.setUsername(usuarioSaved.getUsername());
        storeUser.setPassword(usuarioSaved.getPassword());
        storeUser.setRoles(usuarioSaved.getRoles());

        return this.save(storeUser);
    }

    public StoreUser updatePassword(String username, String newPassword) {
        StoreUser usuario = this.findByUsername(username);
        usuario.setPassword(this.encoder.encode(newPassword));
        return repository.save(usuario);
    }

    public StoreUser save(StoreUser storeUser) {
        AbstractEntityUtil.normalizeEntity(storeUser);
        return AbstractEntityUtil.save(this.repository, storeUser);
    }

    public boolean delete(Long idUsuario) {
        this.repository.deleteById(idUsuario);
        return true;
    }

    public List<StoreUser> findAll(Pageable pageable) {
        List<StoreUser> usuarios = repository.findAll(pageable);
        usuarios.stream().forEach(u -> u.setPassword(null));
        return usuarios;
    }

    public StoreUser findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<StoreUser> findByQuery(Pageable pageable, String query) {
        query = AbstractEntityUtil.normalizeText(query);
        List<StoreUser> usuarios = repository.findAll(AbstractEntityUtil.createExample(new StoreUser(), query), pageable).getContent();
        usuarios.stream().forEach(u -> u.setPassword(null));
        return usuarios;
    }

    public StoreUser findById(Long id) {
        return repository.findById(id).get();
    }

    public boolean has(Long id) {
        return repository.findById(id).isPresent();
    }
}
