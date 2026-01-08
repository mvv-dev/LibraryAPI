package io.github.marcosvinicius.LibraryAPI.service;

import io.github.marcosvinicius.LibraryAPI.controller.dto.UserRegisterDTO;
import io.github.marcosvinicius.LibraryAPI.exceptions.RegistroDuplicadoException;
import io.github.marcosvinicius.LibraryAPI.model.User;
import io.github.marcosvinicius.LibraryAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = repository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("Erro! Credenciais inválidas");
        }
        return user;
    }

    public void register(UserRegisterDTO userRegisterDTO) {

        User userExiste = repository.findByLogin(userRegisterDTO.login());

        if (userExiste == null) {

            String encryptedPassword = passwordEncoder.encode(userRegisterDTO.password());
            User newUser = new User(userRegisterDTO.login(), encryptedPassword, userRegisterDTO.role());
            repository.save(newUser);

        } else {
            throw new RegistroDuplicadoException("Erro! Esse email não está disponível");
        }




    }


}
