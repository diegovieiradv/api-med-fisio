package api.med.fisio.domain.consulta;

import java.time.LocalDateTime;

public record DadosListagemConsulta(Long id, Long idMedico, Long idPaciente, LocalDateTime data) {
    public DadosListagemConsulta(Consulta consulta) {
        this(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente().getId(), consulta.getData());
    }
}
