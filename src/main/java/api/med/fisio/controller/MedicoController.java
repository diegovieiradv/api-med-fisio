package api.med.fisio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import api.med.fisio.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Médicos", description = "Gerenciamento de médicos da clínica")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @PostMapping
    @Operation(summary = "Cadastrar médico", description = "Cadastra um novo médico na clínica")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Médico cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        var medico = medicoService.cadastrar(dados);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.id()).toUri();

        return ResponseEntity.created(uri).body(medico);
    }

    @GetMapping
    @Operation(summary = "Listar médicos", description = "Lista todos os médicos ativos com paginação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de médicos retornada com sucesso")
    })
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = medicoService.listar(paginacao);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Operation(summary = "Atualizar médico", description = "Atualiza as informações de um médico existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Médico atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        var medico = medicoService.atualizar(dados);
        return ResponseEntity.ok(medico);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir médico", description = "Exclui (soft delete) um médico")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Médico excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public ResponseEntity excluir(@PathVariable Long id) {
        medicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar médico", description = "Retorna os detalhes de um médico específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Médico encontrado"),
        @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public ResponseEntity detalhar(@PathVariable Long id) {
        var medico = medicoService.detalhar(id);
        return ResponseEntity.ok(medico);
    }
}
