package med.voll.api.infra.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component //Diz ao spring que essa classe é um componente genérico. Para carregar a classe
public class SecurityFilter  extends OncePerRequestFilter { //Herdando uma classe do Spring para filtro

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository respository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        //lógica de recuperar o token
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            //chegar se o token está válido e recuperar o Subject do usuário e senha
            var subject = tokenService.getSubject(tokenJWT); //recupera o token vindo no cabeçalho
            var usuario = respository.findByLogin(subject);//o token ta valido considera-se que ela está logada spring

            var authentication =  new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication); //SecurityContextHolder

        }

        //filterchain representa a cadeia de filtros da aplicação
        filterChain.doFilter(request, response);  //seguir o fluxo da requisição
    }


    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");  //getHeader pega o cabeçalho
        if (authorizationHeader != null) {

            return authorizationHeader.replace("Bearer ", ""); // apagará o prefixo Bearer no token na impressão
        }
        return null;
    }

}