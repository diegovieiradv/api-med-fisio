import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import api from '../services/api'
import {
  LayoutDashboard,
  Users,
  UserCog,
  Calendar,
  ArrowRight,
} from 'lucide-react'

export default function Dashboard() {
  const [stats, setStats] = useState({
    medicos: 0,
    pacientes: 0,
    consultas: 0,
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadStats()
  }, [])

  async function loadStats() {
    try {
      setLoading(true)
      const [medicosRes, pacientesRes, consultasRes] = await Promise.all([
        api.get('/medicos?size=1'),
        api.get('/pacientes?size=1'),
        api.get('/consultas?size=1'),
      ])
      setStats({
        medicos: medicosRes.data.totalElements || 0,
        pacientes: pacientesRes.data.totalElements || 0,
        consultas: consultasRes.data.totalElements || 0,
      })
    } catch (error) {
      console.error('Erro ao carregar estatisticas:', error)
    } finally {
      setLoading(false)
    }
  }

  const cards = [
    {
      title: 'Medicos Ativos',
      value: stats.medicos,
      icon: UserCog,
      color: 'blue',
      link: '/medicos',
    },
    {
      title: 'Pacientes Ativos',
      value: stats.pacientes,
      icon: Users,
      color: 'green',
      link: '/pacientes',
    },
    {
      title: 'Consultas Agendadas',
      value: stats.consultas,
      icon: Calendar,
      color: 'purple',
      link: '/consultas',
    },
  ]

  const colorClasses = {
    blue: 'bg-blue-100 text-blue-600',
    green: 'bg-green-100 text-green-600',
    purple: 'bg-purple-100 text-purple-600',
  }

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {cards.map((card) => {
          const Icon = card.icon
          return (
            <Link
              key={card.title}
              to={card.link}
              className="bg-white rounded-xl shadow-sm p-6 border border-gray-100 hover:shadow-md transition-shadow group"
            >
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-4">
                  <div
                    className={`w-12 h-12 rounded-lg flex items-center justify-center ${colorClasses[card.color]}`}
                  >
                    <Icon className="w-6 h-6" />
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">{card.title}</p>
                    <p className="text-2xl font-bold text-gray-800">
                      {loading ? '--' : card.value}
                    </p>
                  </div>
                </div>
                <ArrowRight className="w-5 h-5 text-gray-400 group-hover:text-primary transition-colors" />
              </div>
            </Link>
          )
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">Acesso Rapido</h3>
          <div className="space-y-3">
            <Link
              to="/medicos"
              className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors group"
            >
              <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
                <UserCog className="w-5 h-5 text-blue-600" />
              </div>
              <div className="flex-1">
                <p className="font-medium text-gray-800 group-hover:text-primary">
                  Gerenciar Medicos
                </p>
                <p className="text-sm text-gray-500">Cadastrar, editar e listar medicos</p>
              </div>
              <ArrowRight className="w-4 h-4 text-gray-400 group-hover:text-primary" />
            </Link>

            <Link
              to="/pacientes"
              className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors group"
            >
              <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                <Users className="w-5 h-5 text-green-600" />
              </div>
              <div className="flex-1">
                <p className="font-medium text-gray-800 group-hover:text-primary">
                  Gerenciar Pacientes
                </p>
                <p className="text-sm text-gray-500">Cadastrar, editar e listar pacientes</p>
              </div>
              <ArrowRight className="w-4 h-4 text-gray-400 group-hover:text-primary" />
            </Link>

            <Link
              to="/consultas"
              className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors group"
            >
              <div className="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center">
                <Calendar className="w-5 h-5 text-purple-600" />
              </div>
              <div className="flex-1">
                <p className="font-medium text-gray-800 group-hover:text-primary">
                  Agendar Consultas
                </p>
                <p className="text-sm text-gray-500">Agendar e cancelar consultas</p>
              </div>
              <ArrowRight className="w-4 h-4 text-gray-400 group-hover:text-primary" />
            </Link>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">Sobre o Sistema</h3>
          <div className="space-y-4">
            <div className="flex items-start gap-3">
              <div className="w-2 h-2 bg-primary rounded-full mt-2 flex-shrink-0"></div>
              <div>
                <p className="font-medium text-gray-800">Med Fisio</p>
                <p className="text-sm text-gray-500">
                  Sistema de gestao de consultas para clinicas de fisioterapia.
                </p>
              </div>
            </div>
            <div className="flex items-start gap-3">
              <div className="w-2 h-2 bg-primary rounded-full mt-2 flex-shrink-0"></div>
              <div>
                <p className="font-medium text-gray-800">Funcionalidades</p>
                <p className="text-sm text-gray-500">
                  Gerenciamento de medicos, pacientes e agendamento de consultas
                  com validacao de horarios e regras de negocio.
                </p>
              </div>
            </div>
            <div className="flex items-start gap-3">
              <div className="w-2 h-2 bg-primary rounded-full mt-2 flex-shrink-0"></div>
              <div>
                <p className="font-medium text-gray-800">API REST</p>
                <p className="text-sm text-gray-500">
                  Backend construido com Spring Boot, Spring Security e JWT.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
