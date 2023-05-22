package med.voll.api.domain.medico;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

//Bin validation validar os campos do Json o que são obrigatórios

public record DadosCadastroMedico(
        @NotBlank(message = "Nome é obrigatório") // verifica o atributo não pode ser nulo nem vazio
        String nome,
        @NotBlank(message = "Email é obrigatório")
        @Email
        String email,
        @NotBlank(message = "Telefone é obrigatório")
        String telefone,
        @NotBlank(message = "CRM é obrigatório")
        @Pattern(regexp = "\\d{4,6}") //passamos uma expressão regular 4,6 de 4 a 6 digitos
        String crm,
        @NotNull(message = "Especialidade é obrigatória")
        Especialidade especialidade,
        @NotNull(message = "Dados do endereço são obrigatórios")
        @Valid // validando o dados cadastro médico dentro dele é outro DTO e precisa validar também.
        DadosEndereco endereco) {


}
