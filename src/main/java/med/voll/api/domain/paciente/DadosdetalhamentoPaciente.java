package med.voll.api.domain.paciente;

import med.voll.api.domain.endereco.Endereco;

public record DadosdetalhamentoPaciente(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        Endereco endereco) {

    public DadosdetalhamentoPaciente(Paciente paciente){

        this(paciente.getId(), paciente.getNome(), paciente.getTelefone(), paciente.getEmail(), paciente.getEmail(), paciente.getEndereco());


    }



}
