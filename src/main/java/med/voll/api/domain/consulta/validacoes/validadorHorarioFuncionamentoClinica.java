package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component //Componente genérico informando ao Spring
public class validadorHorarioFuncionamentoClinica implements ValidadorAgendamentoDeConsulta {

    public void validar(DadosAgendamentoConsulta dados) {
        var dataconsulta = dados.data(); // pegando a data do DTO do agendamento e jogando na variavel
        var domingo = dataconsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY); // dayofweek pega o dia da semana e no equals vê se é Domingo a data que esta chegando no DTO
        var antesDaAberturaDaClinica = dataconsulta.getHour() < 7; // Checando se a hora da consulta é antes da 7 hs da manhã
        var depoisDoFechamentoDaClinica = dataconsulta.getHour() > 18; //Checando se a hora que está chegando é depois do encerramento

        if(domingo || antesDaAberturaDaClinica || depoisDoFechamentoDaClinica){
            throw new ValidacaoException("Consulta fora do horário de funcionamento da clinica");
        }


    }
}
