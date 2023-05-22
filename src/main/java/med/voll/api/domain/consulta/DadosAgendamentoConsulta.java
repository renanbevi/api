package med.voll.api.domain.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.medico.Especialidade;

import java.time.LocalDateTime;


public record DadosAgendamentoConsulta(

        Long idMedico,
        @NotNull
        Long idPaciente,

        @NotNull
        @Future // agendamento de data daqui para a frente não tem como agendar algo que já passo
       //  @JsonFormat(pattern = "dd/mm/yyyy HH:mm") serve para formatar um campo que vai receber as informaçõez
        LocalDateTime data,

        Especialidade especialidade){


}
