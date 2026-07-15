package api.med.fisio.controller;

import api.med.fisio.domain.consulta.AgendaDeConsultas;
import api.med.fisio.domain.consulta.ConsultaRepository;
import api.med.fisio.domain.consulta.DadosAgendamentoConsulta;
import api.med.fisio.domain.consulta.DadosCancelamentoConsulta;
import api.med.fisio.domain.consulta.DadosListagemConsulta;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {

    @Autowired
    private AgendaDeConsultas agenda;

    @Autowired
    private ConsultaRepository consultaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoConsulta dados) {
        var dto = agenda.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity cancelar(@RequestBody @Valid DadosCancelamentoConsulta dados) {
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemConsulta>> listar(@PageableDefault(size = 10, sort = {"data"}) Pageable paginacao) {
        var page = consultaRepository.findAllByMotivoCancelamentoIsNull(paginacao).map(DadosListagemConsulta::new);
        return ResponseEntity.ok(page);
    }
}
