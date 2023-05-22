package med.voll.api.domain.medico;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;


public interface MedicoRepository extends JpaRepository<Medico,Long> {

    Page<Medico> findAllByAtivoTrue(Pageable paginacao);  //gera a consulta automaticamente para mostrar somente os ativos

    @Query("""
            select m from Medico m
            where
            m.ativo = 1
            and 
            m.especialidade = :especialidade 
            and 
            m.id not in(
                select  c.medico.id from Consulta c
                where 
                c.data = :data
            )         
            order by rand() 
            limit 1
            """)  // dois ponto em especialidade quer informar que o nome do parametro é o mesmo :especialidade
    //rand() trazer o médico aleatorio que está ativo
    //select  c.medico.id from consulta c
        //                where c.data = :data   traz todos id's de médicos livres para consulta na data atual

   Medico escolherMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);

    @Query("""
            select m.ativo 
            from Medico m
            where 
            m.id = :id
            """)
    Boolean findAtivoById(Long id);

}
