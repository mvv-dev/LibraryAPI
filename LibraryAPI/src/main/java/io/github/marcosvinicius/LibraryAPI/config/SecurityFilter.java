package io.github.marcosvinicius.LibraryAPI.config;

import io.github.marcosvinicius.LibraryAPI.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod();
        String path = request.getRequestURI();

        var token = this.recoverToken(request);
        if (token != null) {
            String subject = tokenService.validateToken(token);

            if (subject != null && !subject.isBlank()) {
                UserDetails user = userRepository.findByLogin(subject);

                if (user != null) {
                    var authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Autenticado via JWT | subject = {} | Role = {} | {} {}", subject, user.getAuthorities(), method, path);
                } else {
                    log.warn("JWT subject não encontrado no banco | subject = {} | {} {}", subject, method, path);
                }
            } else {
                log.warn("JWT inválido/expirado | {} {}", method, path);
            }

        } else {
            log.warn("JWT ausente | {} {}", method, path);
        }

        filterChain.doFilter(request,response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        if (!authHeader.startsWith("Bearer ")) return null;

        return authHeader.substring(7);
    }
}
