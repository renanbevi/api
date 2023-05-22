package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController //essa classe é um controller Rest carregue a classe
@RequestMapping("medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired //injeção de dependencia
    private MedicoRepository repository; //interface repository

    @PostMapping
    @Transactional //método de inscrita para fazer o insert no banco de dados
    //Classe no padrão DTO seu objetivo é apenas representar dados que serão recebidos ou devolvidos pela API, sem nenhum tipo de comportamento.
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder){ //informar o spring que o parametro json vai puxar do corpo da requisição
       //UriComponentsBuilder classe que sabe criar a URI
       var medico = new Medico(dados);//var medico recebendo os dados
       repository.save(medico); // parametro 1 null porque o banco de dados que vai gerar o ID pra gente
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();  //buildAndExpand chamo o ID que acabou de ser cadastrado no banco de dados. toUri cria o objeto URI
        //endereço da apo classe que encapsula o endereço da API  path passa o complemento da URL
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico)); //URI define o endereço body devolve o corpo da resposta
        //passando o mesmo parametro para o metodo Body o dados detalhamento medico, pois tem todos os atributos.
    }
    @GetMapping   //somente para leitura
    public ResponseEntity <Page<DadosListagemMedico>> listar(Pageable paginacao){ //listar todos os medicos no banco

        var page =  repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new); //fazer um mapeamento para converter de Médico para Listagem Medico classe To list para converter para uma lista
        return ResponseEntity.ok(page);
    } // Conversão de médico para Dados Listagem médicos.
      // Oageable classe do Spring para paginação na hora de listar todos os itens
      // Page devolve informações sobre paginação com a lista dos dados dos médicos.

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico = repository.getReferenceById(dados.id());  //busca a referencia do ID
        medico.atualizarInformacoes(dados); // carreguei os dados do medico e vou atualizar com as informações atual

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));// criamos um construtor novo do objeto médico para o response entity, pois a atualização só mexe com os atributos nome, telefone e endereço
    }
    @DeleteMapping("/{id}") // avisar que o id que vem no método esta vindo no ID do postman
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){ //  PathVariable Uma variavel da URL deleteMapping

        var medico = repository.getReferenceById(id);//busca a referencia do ID]
        medico.excluir(); //excluir depois que foi alterado para falso o ativo e no banco vai alterar para 0

        return ResponseEntity.noContent().build(); //noContent cria o objeto e o build constroi o objeto.
    }

    @GetMapping("/{id}") // avisar que o id que vem no método esta vindo no ID do postman
    public ResponseEntity detalhar(@PathVariable Long id){ //  PathVariable Uma variavel da URL deleteMapping

        var medico = repository.getReferenceById(id);//carrega o médico do banco de dados
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico)); //retorna o parametro de médicos para listar o detalhamento
    }



}
