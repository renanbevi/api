package med.voll.api.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Faz o acesso na tabela do banco de dados.

    UserDetails findByLogin(String login); //metodo que faz a consulta do usuário no banco de dados.
}
