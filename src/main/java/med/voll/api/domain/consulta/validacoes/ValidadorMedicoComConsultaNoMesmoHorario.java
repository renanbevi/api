package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component //Componente genérico informando ao Spring
public class ValidadorMedicoComConsultaNoMesmoHorario implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosAgendamentoConsulta dados){
        var medicoPossuiOutraConsultaNoMesmohorario =
                repository.existsByMedicoIdAndData(dados.idMedico(),dados.data());
        if(medicoPossuiOutraConsultaNoMesmohorario){
            throw new ValidacaoException("Médico já possui outra consulta agendada neste mesmo horário");
        }
    }
}
