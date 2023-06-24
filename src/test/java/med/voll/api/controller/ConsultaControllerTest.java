package med.voll.api.controller;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.consulta.MotivoCancelamento;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static med.voll.api.domain.consulta.MotivoCancelamento.MEDICO_CANCELOU;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters //teste com json
@AutoConfigureMockMvc //teste de MOCK
// Teste de controller
@SpringBootTest
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    //tipo do objeto que o jacksonTester vai trabalhar o mesmo parametro do método que vamos testar neste caso
    // consultacontroller dadosagendamentoconsulta
    //Json que chega na API
    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;
    //Json que enviamos na API devolvemos
    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @Autowired
    private JacksonTester<DadosCancelamentoConsulta> dadosCancelamentoConsultaJson;


    @Test
    @DisplayName("Deveria devolver código Http 400 quando informações estiverem inválidas")
    @WithMockUser
        //Spring faz o mock de um usuário onde já funciona como eu estou logado pulando o teste de segurança
    void agendar_cenario1() throws Exception {
        //variavel response para guardar as informações no response
        var response = mvc.perform(post("/consultas")) //performa uma requisição em nossa api post devido ao metodo ser um POST
                .andReturn().getResponse();
        //Assert recebendo o status do response  bad request que é o código 400.
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código Http 200 quando informações estiverem válidas")
    @WithMockUser
        //Spring faz o mock de um usuário onde já funciona como eu estou logado pulando o teste de segurança
    void agendar_cenario2() throws Exception {

        var data = LocalDateTime.now().plusHours(12); // adiciono 1 hora a mais
        var especialidade = Especialidade.CARDIOLOGIA;

        //variavel response para guardar as informações no response

        var response = mvc
                .perform(
                        post("/consultas")
                                .contentType(MediaType.APPLICATION_JSON) //leva o cabeçalho com o valor json
                                .content(dadosAgendamentoConsultaJson.write(
                                        new DadosAgendamentoConsulta(1l, 1l, data, especialidade)
                                ).getJson())
                        //getJason converte o objeto para String Jason
                )
                //performa uma requisição em nossa api post devido ao metodo ser um POST
                .andReturn().getResponse();

        //Assert recebendo o status do response  sucess  que é o código 200.
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosDetalhamentoConsultaJson.write(
                new DadosDetalhamentoConsulta(null, 1l, 1l, data)
        ).getJson(); //devolve o json esperado

        //resposta me devolve o conteudo de String e verifica se é igual o Json que estou esperando
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @DisplayName("Deveria devolver código Http 204 quando cancelar uma consulta")
    @WithMockUser
        //Spring faz o mock de um usuário onde já funciona como eu estou logado pulando o teste de segurança
    void agendar_cenario3() throws Exception {


        var motivoCancelamento = MEDICO_CANCELOU;
        //variavel response para guardar as informações no response

        var response = mvc
                .perform(
                        delete("/consultas")
                                .contentType(MediaType.APPLICATION_JSON) //leva o cabeçalho com o valor json
                                .content(dadosCancelamentoConsultaJson.write(
                                        new DadosCancelamentoConsulta(3l, motivoCancelamento)
                                ).getJson())
                        //getJason converte o objeto para String Jason
                )
                //performa uma requisição em nossa api post devido ao metodo ser um POST
                .andReturn().getResponse();

        //Assert recebendo o status do response  sucess  que é o código 204.
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());


    }

    @Test
    @DisplayName("Deveria devolver código Http 400 quando não existir uma consulta para cancelar")
    @WithMockUser
        //Spring faz o mock de um usuário onde já funciona como eu estou logado pulando o teste de segurança
    void agendar_cenario4() throws Exception {


        var motivoCancelamento = MEDICO_CANCELOU;
        //variavel response para guardar as informações no response

        var response = mvc.perform(delete("/consultas").contentType(MediaType.APPLICATION_JSON) //leva o cabeçalho com o valor json
                                .content(dadosCancelamentoConsultaJson.write(new DadosCancelamentoConsulta(400l, motivoCancelamento)).getJson())
                        //getJason converte o objeto para String Jason
                )
                //performa uma requisição em nossa api post devido ao metodo ser um POST
                .andReturn().getResponse();

        //Assert recebendo o status do response  sucess  que é o código 400.
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());


    }

}