package api.med.fisio.domain.paciente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

    @Autowired
    private PacienteRepository repository;

    @Transactional
    public DadosDetalhamentoPaciente cadastrar(DadosCadastroPaciente dados) {
        var paciente = new Paciente(dados);
        repository.save(paciente);
        logger.info("Paciente cadastrado: id={}, nome={}, email={}", paciente.getId(), paciente.getNome(), paciente.getEmail());
        return new DadosDetalhamentoPaciente(paciente);
    }

    public Page<DadosListagemPaciente> listar(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
    }

    @Transactional
    public DadosDetalhamentoPaciente atualizar(DadosAtualizacaoPaciente dados) {
        var paciente = repository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);
        logger.info("Paciente atualizado: id={}", paciente.getId());
        return new DadosDetalhamentoPaciente(paciente);
    }

    @Transactional
    public void excluir(Long id) {
        var paciente = repository.getReferenceById(id);
        paciente.excluir();
        logger.info("Paciente excluído (soft delete): id={}", id);
    }

    public DadosDetalhamentoPaciente detalhar(Long id) {
        var paciente = repository.getReferenceById(id);
        return new DadosDetalhamentoPaciente(paciente);
    }
}
