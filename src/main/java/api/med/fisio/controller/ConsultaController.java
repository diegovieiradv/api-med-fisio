package api.med.fisio.controller;

import api.med.fisio.domain.consulta.AgendaDeConsultas;
import api.med.fisio.domain.consulta.ConsultaRepository;
import api.med.fisio.domain.consulta.DadosAgendamentoConsulta;
import api.med.fisio.domain.consulta.DadosCancelamentoConsulta;
import api.med.fisio.domain.consulta.DadosListagemConsulta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Consultas", description = "Gerenciamento de consultas médicas")
public class ConsultaController {

    @Autowired
    private AgendaDeConsultas agenda;

    @Autowired
    private ConsultaRepository consultaRepository;

    @PostMapping
    @Transactional
    @Operation(summary = "Agendar consulta", description = "Agenda uma nova consulta médica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Consulta agendada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada"),
        @ApiResponse(responseCode = "404", description = "Médico ou paciente não encontrado")
    })
    public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoConsulta dados) {
        var dto = agenda.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    @Transactional
    @Operation(summary = "Cancelar consulta", description = "Cancela uma consulta existente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Consulta cancelada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada"),
        @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity cancelar(@RequestBody @Valid DadosCancelamentoConsulta dados) {
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar consultas", description = "Lista todas as consultas ativas (não canceladas) com paginação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de consultas retornada com sucesso")
    })
    public ResponseEntity<Page<DadosListagemConsulta>> listar(@PageableDefault(size = 10, sort = {"data"}) Pageable paginacao) {
        var page = consultaRepository.findAllByMotivoCancelamentoIsNull(paginacao).map(DadosListagemConsulta::new);
        return ResponseEntity.ok(page);
    }
}
