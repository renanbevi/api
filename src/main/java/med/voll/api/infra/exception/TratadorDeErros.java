package med.voll.api.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import med.voll.api.domain.ValidacaoException;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.http.HttpResponse;

@RestControllerAdvice //dizer ao spring classe que trata erros
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)//Quando esse metodo vai ser chamado pra essa  exception
    public ResponseEntity tratarErro404(){

        return ResponseEntity.notFound().build();  // Build cria a exception.
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)//Quando esse metodo vai ser chamado pra essa  exception
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex){
       var erros = ex.getFieldErrors(); //capturando a exception para ver os campos inválidos

        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList()); //Body devolve o objeto com as informações no corpo da respostas
    }

    @ExceptionHandler(ValidacaoException.class)//Quando esse metodo vai ser chamado pra essa  exception
    public ResponseEntity tratarErroRegradeNegocio(ValidacaoException ex){
       return ResponseEntity.badRequest().body(ex.getMessage());  // irá devolver o código 400 e vai informar o erro da exception que criamos no if (AGENDADECONSULTAS) classe.
    }

    private record DadosErroValidacao(String campo, String mensagem){
        public DadosErroValidacao(FieldError erro){ //

            this(erro.getField(), erro.getDefaultMessage()); //construtor padrão Getfield dá o nome do campo  default Menssage dá a mensagem para o campo em especifico
        }

    }
}
