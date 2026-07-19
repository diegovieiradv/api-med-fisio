package api.med.fisio.domain.medico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

    private static final Logger logger = LoggerFactory.getLogger(MedicoService.class);

    @Autowired
    private MedicoRepository repository;

    @Transactional
    public DadosDetalhamentoMedico cadastrar(DadosCadastroMedico dados) {
        var medico = new Medico(dados);
        repository.save(medico);
        logger.info("Médico cadastrado: id={}, nome={}, email={}", medico.getId(), medico.getNome(), medico.getEmail());
        return new DadosDetalhamentoMedico(medico);
    }

    public Page<DadosListagemMedico> listar(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
    }

    @Transactional
    public DadosDetalhamentoMedico atualizar(DadosAtualizacaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
        logger.info("Médico atualizado: id={}", medico.getId());
        return new DadosDetalhamentoMedico(medico);
    }

    @Transactional
    public void excluir(Long id) {
        var medico = repository.getReferenceById(id);
        medico.excluir();
        logger.info("Médico excluído (soft delete): id={}", id);
    }

    public DadosDetalhamentoMedico detalhar(Long id) {
        var medico = repository.getReferenceById(id);
        return new DadosDetalhamentoMedico(medico);
    }
}
