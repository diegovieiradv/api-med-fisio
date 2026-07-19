package api.med.fisio.domain.consulta;

import api.med.fisio.domain.ValidacaoException;
import api.med.fisio.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import api.med.fisio.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
import api.med.fisio.domain.medico.Medico;
import api.med.fisio.domain.medico.MedicoRepository;
import api.med.fisio.domain.paciente.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AgendaDeConsultas {

    private static final Logger logger = LoggerFactory.getLogger(AgendaDeConsultas.class);

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;

    @Autowired
    private List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
        if (!pacienteRepository.existsById(dados.idPaciente())) {
            throw new ValidacaoException("Id do paciente informado não existe!");
        }

        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
            throw new ValidacaoException("Id do médico informado não existe!");
        }

        validadores.forEach(v -> v.validar(dados));

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);
        if (medico == null) {
            throw new ValidacaoException("Não existe médico disponível nessa data!");
        }

        var consulta = new Consulta(null, medico, paciente, dados.data(), null);
        consultaRepository.save(consulta);

        logger.info("Consulta agendada: id={}, medico_id={}, paciente_id={}, data={}",
                consulta.getId(), medico.getId(), paciente.getId(), dados.data());

        return new DadosDetalhamentoConsulta(consulta);
    }

    @Transactional
    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());

        logger.info("Consulta cancelada: id={}, motivo={}", dados.idConsulta(), dados.motivo());
    }


    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null) {
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if (dados.especialidade() == null) {
            throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido!");
        }

        var medicosLivres = medicoRepository.listarMedicosLivresNaData(dados.especialidade(), dados.data());
        if (medicosLivres.isEmpty()) {
            return null;
        }

        var indiceAleatorio = ThreadLocalRandom.current().nextInt(medicosLivres.size());
        return medicosLivres.get(indiceAleatorio);
    }

}
