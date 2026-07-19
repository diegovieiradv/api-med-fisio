import { useState, useEffect } from 'react'
import api from '../services/api'
import {
  Plus,
  Search,
  X,
  Calendar,
  Clock,
  User,
  UserCog,
} from 'lucide-react'

const especialidades = ['ORTOPEDIA', 'CARDIOLOGIA', 'GINECOLOGIA', 'DERMATOLOGIA']

const especialidadeLabels = {
  ORTOPEDIA: 'Ortopedia',
  CARDIOLOGIA: 'Cardiologia',
  GINECOLOGIA: 'Ginecologia',
  DERMATOLOGIA: 'Dermatologia',
}

const motivosCancelamento = [
  { value: 'PACIENTE_DESISTIU', label: 'Paciente desistiu' },
  { value: 'MEDICO_CANCELOU', label: 'Medico cancelou' },
  { value: 'OUTROS', label: 'Outros' },
]

export default function Consultas() {
  const [consultas, setConsultas] = useState([])
  const [medicos, setMedicos] = useState([])
  const [pacientes, setPacientes] = useState([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [showCancelModal, setShowCancelModal] = useState(false)
  const [cancelConsultaId, setCancelConsultaId] = useState(null)
  const [motivoCancelamento, setMotivoCancelamento] = useState('')
  const [formData, setFormData] = useState({
    idMedico: '',
    idPaciente: '',
    data: '',
    especialidade: '',
  })
  const [errors, setErrors] = useState({})

  useEffect(() => {
    loadData()
  }, [])

  async function loadData() {
    try {
      setLoading(true)
      const [consultasRes, medicosRes, pacientesRes] = await Promise.all([
        api.get('/consultas?size=100'),
        api.get('/medicos?size=100'),
        api.get('/pacientes?size=100'),
      ])
      setConsultas(consultasRes.data.content)
      setMedicos(medicosRes.data.content)
      setPacientes(pacientesRes.data.content)
    } catch (error) {
      console.error('Erro ao carregar dados:', error)
    } finally {
      setLoading(false)
    }
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErrors({})

    try {
      const payload = {
        idPaciente: parseInt(formData.idPaciente),
        data: formData.data,
      }

      if (formData.idMedico) {
        payload.idMedico = parseInt(formData.idMedico)
      } else if (formData.especialidade) {
        payload.especialidade = formData.especialidade
      }

      await api.post('/consultas', payload)
      setShowModal(false)
      resetForm()
      loadData()
    } catch (error) {
      if (error.response?.status === 400) {
        const data = error.response.data
        if (Array.isArray(data)) {
          const fieldErrors = {}
          data.forEach((err) => {
            fieldErrors[err.campo] = err.mensagem
          })
          setErrors(fieldErrors)
        } else {
          setErrors({ geral: data })
        }
      }
    }
  }

  async function handleCancel() {
    if (!motivoCancelamento) return

    try {
      await api.delete('/consultas', {
        data: {
          idConsulta: cancelConsultaId,
          motivo: motivoCancelamento,
        },
      })
      setShowCancelModal(false)
      setCancelConsultaId(null)
      setMotivoCancelamento('')
      loadData()
    } catch (error) {
      console.error('Erro ao cancelar consulta:', error)
    }
  }

  function resetForm() {
    setFormData({
      idMedico: '',
      idPaciente: '',
      data: '',
      especialidade: '',
    })
  }

  function openCancelModal(id) {
    setCancelConsultaId(id)
    setMotivoCancelamento('')
    setShowCancelModal(true)
  }

  function formatDateTime(dateString) {
    if (!dateString) return '--'
    const date = new Date(dateString)
    return date.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  }

  function getMedicoNome(id) {
    const medico = medicos.find((m) => m.id === id)
    return medico?.nome || '--'
  }

  function getPacienteNome(id) {
    const paciente = pacientes.find((p) => p.id === id)
    return paciente?.nome || '--'
  }

  const filteredConsultas = consultas.filter(
    (c) =>
      getMedicoNome(c.idMedico).toLowerCase().includes(search.toLowerCase()) ||
      getPacienteNome(c.idPaciente).toLowerCase().includes(search.toLowerCase())
  )

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row gap-4 justify-between items-start sm:items-center">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
          <input
            type="text"
            placeholder="Buscar consulta..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent outline-none"
          />
        </div>
        <button
          onClick={() => {
            resetForm()
            setShowModal(true)
          }}
          className="bg-primary hover:bg-primary-dark text-white px-4 py-2 rounded-lg flex items-center gap-2 transition-colors"
        >
          <Plus className="w-5 h-5" />
          Nova Consulta
        </button>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        {loading ? (
          <div className="p-8 text-center text-gray-500">Carregando...</div>
        ) : filteredConsultas.length === 0 ? (
          <div className="p-8 text-center text-gray-500">
            <Calendar className="w-12 h-12 mx-auto mb-3 text-gray-300" />
            Nenhuma consulta encontrada
          </div>
        ) : (
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-100">
              <tr>
                <th className="text-left px-6 py-3 text-xs font-semibold text-gray-500 uppercase">
                  Medico
                </th>
                <th className="text-left px-6 py-3 text-xs font-semibold text-gray-500 uppercase">
                  Paciente
                </th>
                <th className="text-left px-6 py-3 text-xs font-semibold text-gray-500 uppercase">
                  Data/Hora
                </th>
                <th className="text-right px-6 py-3 text-xs font-semibold text-gray-500 uppercase">
                  Acoes
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {filteredConsultas.map((consulta) => (
                <tr key={consulta.id} className="hover:bg-gray-50 transition-colors">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <UserCog className="w-4 h-4 text-gray-400" />
                      <span className="text-gray-800">{getMedicoNome(consulta.idMedico)}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <User className="w-4 h-4 text-gray-400" />
                      <span className="text-gray-800">{getPacienteNome(consulta.idPaciente)}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <Clock className="w-4 h-4 text-gray-400" />
                      <span className="text-gray-600">{formatDateTime(consulta.data)}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-right">
                    <button
                      onClick={() => openCancelModal(consulta.id)}
                      className="text-gray-500 hover:text-red-600 text-sm font-medium"
                    >
                      Cancelar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl w-full max-w-lg">
            <div className="flex items-center justify-between p-6 border-b">
              <h3 className="text-lg font-semibold">Nova Consulta</h3>
              <button
                onClick={() => setShowModal(false)}
                className="text-gray-500 hover:text-gray-700"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="p-6 space-y-4">
              {errors.geral && (
                <div className="bg-red-50 text-red-600 px-4 py-3 rounded-lg text-sm">
                  {errors.geral}
                </div>
              )}

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Paciente *
                </label>
                <select
                  value={formData.idPaciente}
                  onChange={(e) => setFormData({ ...formData, idPaciente: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent outline-none"
                  required
                >
                  <option value="">Selecione o paciente...</option>
                  {pacientes.map((p) => (
                    <option key={p.id} value={p.id}>
                      {p.nome}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Medico (opcional - se vazio, sera escolhido automaticamente)
                </label>
                <select
                  value={formData.idMedico}
                  onChange={(e) => {
                    setFormData({ ...formData, idMedico: e.target.value, especialidade: '' })
                  }}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent outline-none"
                >
                  <option value="">Selecione o medico...</option>
                  {medicos.map((m) => (
                    <option key={m.id} value={m.id}>
                      {m.nome} - {especialidadeLabels[m.especialidade]}
                    </option>
                  ))}
                </select>
              </div>

              {!formData.idMedico && (
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Especialidade (requerida se medico nao selecionado)
                  </label>
                  <select
                    value={formData.especialidade}
                    onChange={(e) => setFormData({ ...formData, especialidade: e.target.value })}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent outline-none"
                  >
                    <option value="">Selecione...</option>
                    {especialidades.map((esp) => (
                      <option key={esp} value={esp}>
                        {especialidadeLabels[esp]}
                      </option>
                    ))}
                  </select>
                </div>
              )}

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Data e Hora *
                </label>
                <input
                  type="datetime-local"
                  value={formData.data}
                  onChange={(e) => setFormData({ ...formData, data: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent outline-none"
                  min={new Date().toISOString().slice(0, 16)}
                  required
                />
              </div>

              <div className="flex justify-end gap-3 pt-4 border-t">
                <button
                  type="button"
                  onClick={() => setShowModal(false)}
                  className="px-4 py-2 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 bg-primary hover:bg-primary-dark text-white rounded-lg transition-colors"
                >
                  Agendar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showCancelModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl w-full max-w-md">
            <div className="flex items-center justify-between p-6 border-b">
              <h3 className="text-lg font-semibold">Cancelar Consulta</h3>
              <button
                onClick={() => setShowCancelModal(false)}
                className="text-gray-500 hover:text-gray-700"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <div className="p-6 space-y-4">
              <p className="text-gray-600">
                Tem certeza que deseja cancelar esta consulta? O cancelamento so e permitido com
                antecedencia minima de 24h.
              </p>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Motivo do Cancelamento *
                </label>
                <select
                  value={motivoCancelamento}
                  onChange={(e) => setMotivoCancelamento(e.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent outline-none"
                >
                  <option value="">Selecione...</option>
                  {motivosCancelamento.map((m) => (
                    <option key={m.value} value={m.value}>
                      {m.label}
                    </option>
                  ))}
                </select>
              </div>

              <div className="flex justify-end gap-3 pt-4 border-t">
                <button
                  onClick={() => setShowCancelModal(false)}
                  className="px-4 py-2 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors"
                >
                  Voltar
                </button>
                <button
                  onClick={handleCancel}
                  disabled={!motivoCancelamento}
                  className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded-lg transition-colors disabled:opacity-50"
                >
                  Confirmar Cancelamento
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
