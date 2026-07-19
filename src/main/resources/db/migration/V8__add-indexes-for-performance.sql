-- Indexes para melhorar performance das queries

-- Index para medicos (especialidade e ativo)
CREATE INDEX idx_medicos_especialidade_ativo ON medicos(especialidade, ativo);

-- Index para consultas (medico_id, data, motivo_cancelamento)
CREATE INDEX idx_consultas_medico_data ON consultas(medico_id, data, motivo_cancelamento);

-- Index para consultas (paciente_id, data)
CREATE INDEX idx_consultas_paciente_data ON consultas(paciente_id, data);

-- Index para consultas (data) para ordenação
CREATE INDEX idx_consultas_data ON consultas(data);
