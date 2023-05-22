package med.voll.api.domain.paciente;



import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.endereco.DadosEndereco;


public record DadosCadastroPaciente(
    @NotBlank(message = "Dados do nome são obrigatórios")
     String nome,
    @NotBlank(message = "Dados do e-mail são obrigatórios")
    @Email
    String email,
    @NotBlank(message = "Dados do telefone são obrigatórios")
    String telefone,
    @NotBlank(message = "Dados do cpf são obrigatórios")
    String cpf,
    @NotNull
    @Valid
    DadosEndereco endereco) {


}


