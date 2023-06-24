package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {
    @Autowired
    private ConsultaRepository consultaRepository; //injetando o repository
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;
    //Cria uma lista de todos os validadores que estão nesta interface
    //Injeta a lista com cada uma delas conforme o Autowired

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados){
           if(!pacienteRepository.existsById(dados.idPaciente())){ // utilizando ! para negar se não existir o paciente com esse ID faz a linha abaixo
                throw new ValidacaoException("Id do paciente informado não existe!");
           }

           if(dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){
               throw new ValidacaoException("Id do Medico informado não existe");
           }
        validadores.forEach(v -> v.validar(dados));

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente()); // utilizando o objeto para carregar todos os dados do paciente e não só o ID
        var medico = escolherMedico(dados);
        if (medico == null) {
            throw new ValidacaoException("Não existe médico disponivel nesta data");
        }
        var consulta = new Consulta(null, medico, paciente, dados.data(),null);
        consultaRepository.save(consulta);
        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if(dados.idMedico() != null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if(dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatória quando o médico não for escolhido");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }


    public void cancelar(DadosCancelamentoConsulta dados) {
        if(!consultaRepository.existsById(dados.idConsulta())){
            throw new ValidacaoException("Id da consulta informado não existe");

        }

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());

    }
}

