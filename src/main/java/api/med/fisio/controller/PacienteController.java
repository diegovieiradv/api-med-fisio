package api.med.fisio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import api.med.fisio.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pacientes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Pacientes", description = "Gerenciamento de pacientes da clínica")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping
    @Operation(summary = "Cadastrar paciente", description = "Cadastra um novo paciente na clínica")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder) {
        var paciente = pacienteService.cadastrar(dados);

        var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.id()).toUri();
        return ResponseEntity.created(uri).body(paciente);
    }

    @GetMapping
    @Operation(summary = "Listar pacientes", description = "Lista todos os pacientes ativos com paginação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    })
    public ResponseEntity<Page<DadosListagemPaciente>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = pacienteService.listar(paginacao);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Operation(summary = "Atualizar paciente", description = "Atualiza as informações de um paciente existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
        var paciente = pacienteService.atualizar(dados);
        return ResponseEntity.ok(paciente);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir paciente", description = "Exclui (soft delete) um paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Paciente excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity excluir(@PathVariable Long id) {
        pacienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar paciente", description = "Retorna os detalhes de um paciente específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity detalhar(@PathVariable Long id) {
        var paciente = pacienteService.detalhar(id);
        return ResponseEntity.ok(paciente);
    }
}
