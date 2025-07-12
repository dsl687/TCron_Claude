# TCron - Agendador de Tarefas para Android

TCron é um aplicativo Android moderno para agendamento e automação de tarefas, scripts Python e Shell. Desenvolvido com Jetpack Compose e arquitetura limpa, oferece uma interface intuitiva e funcionalidades avançadas para administradores de sistema e usuários avançados.

## 🚀 Funcionalidades

### ✅ Funcionalidades Implementadas e Corrigidas

#### 🏠 Tela Principal (Home)
- ✅ **Notificações em Tempo Real**: Sistema de notificações tipo YouTube com contadores dinâmicos
- ✅ **Cards Informativos**: Exibição de métricas do sistema e tarefas agendadas
- ✅ **FAB Menu Funcional**: Menu de ações flutuante totalmente operacional
- ✅ **Navegação Otimizada**: Drawer menu restrito apenas à tela inicial

#### 📝 Criação e Gerenciamento de Scripts
- ✅ **Editor Python**: Interface completa para criação de scripts Python com:
  - Templates otimizados para Android (`#!/system/bin/env python3`)
  - Verificação de ambiente Android automática
  - Sistema de logs com timestamp
  - Tratamento de exceções robusto
- ✅ **Editor Shell**: Interface completa para criação de scripts Shell com:
  - Templates compatíveis com Android (`#!/system/bin/sh`)
  - Verificação de permissões root
  - Comandos rápidos pré-configurados
  - Teste de rotação de tela integrado
- ✅ **Persistência Local**: Scripts salvos com SharedPreferences e Gson
- ✅ **Teste de Execução**: Validação real de scripts com verificação de root

#### ⏰ Agendamento de Tarefas
- ✅ **Criação de Agendamentos**: Interface intuitiva para programar tarefas
- ✅ **Formatos Flexíveis**: Suporte a horário, diário, semanal, mensal e cron customizado
- ✅ **Persistência Completa**: Agendamentos salvos localmente com metadata
- ✅ **Validação Avançada**: Verificação de sintaxe e permissões

#### 🎨 Interface e Temas
- ✅ **Material Design 3**: Interface moderna seguindo diretrizes atuais
- ✅ **Temas Dinâmicos**: Aplicação instantânea sem reinicialização
- ✅ **Material You**: Suporte a cores dinâmicas do Android 12+
- ✅ **Modo Escuro/Claro**: Alternância fluida entre temas

#### 🔐 Segurança e Configurações
- ✅ **Autenticação Biométrica**: Integração com BiometricPrompt
  - Verificação de disponibilidade de hardware
  - Ativação com confirmação biométrica
  - Status detalhado de compatibilidade
- ✅ **Configurações Persistentes**: Todas as preferências mantidas entre sessões
- ✅ **Verificação Root**: Detecção automática de acesso root
- ✅ **Notificações Configuráveis**: Canais separados por tipo de evento

#### 🔔 Sistema de Notificações
- ✅ **Teste Funcional**: Botão de teste dispara notificações reais
- ✅ **Canais Organizados**: Separação por execução, alertas, updates e debug
- ✅ **Configuração Avançada**: Controles granulares de som, vibração e tipos

#### 📄 Documentação e Transparência
- ✅ **Licenças Open Source**: Dialog com bibliotecas utilizadas
- ✅ **Política de Privacidade**: Informações claras sobre uso de dados
- ✅ **Código Limpo**: Documentação inline e comentários explicativos

## 🏗️ Arquitetura

### Stack Tecnológico
- **UI**: Jetpack Compose + Material Design 3
- **Injeção de Dependência**: Hilt
- **Arquitetura**: MVVM + Clean Architecture
- **Persistência**: SharedPreferences + Gson
- **Reatividade**: StateFlow + Coroutines
- **Navegação**: Navigation Compose

### Estrutura de Módulos
```
app/                    # Módulo principal da aplicação
├── navigation/         # Configuração de navegação
└── ui/theme/          # Temas e cores

core/
├── common/            # Utilitários e helpers compartilhados
├── data/              # Repositórios e fontes de dados
└── domain/            # Models e regras de negócio

feature/
├── home/              # Tela principal e criação de scripts
├── settings/          # Configurações e preferências
├── task/              # Criação e gerenciamento de tarefas
└── terminal/          # Interface de terminal (futuro)
```

## 🛠️ Instalação e Uso

### Pré-requisitos
- Android 7.0 (API 24) ou superior
- Permissões de notificação (Android 13+)
- Acesso root (opcional, para scripts avançados)

### Instalação
1. Baixe o APK da versão mais recente
2. Habilite "Fontes desconhecidas" nas configurações
3. Instale o aplicativo
4. Conceda permissões de notificação quando solicitado

### Primeiros Passos
1. **Configurar Tema**: Acesse Configurações > Aparência
2. **Criar Script**: Use o FAB menu > "Criar script Python/Shell"
3. **Agendar Tarefa**: FAB menu > "Criar agendamento"
4. **Testar Notificações**: Configurações > Testar Notificação

## 🔧 Funcionalidades Avançadas

### Scripts Python
- Detecção automática de ambiente Android
- Imports seguros para execução móvel
- Logging estruturado com timestamps
- Verificações de compatibilidade

### Scripts Shell
- Shebang otimizado para Android (`#!/system/bin/sh`)
- Verificação automática de permissões root
- Comandos de rotação de tela pré-configurados
- Fallbacks para sistemas Unix/Linux

### Segurança
- Armazenamento local (nenhum dado enviado externamente)
- Criptografia opcional de scripts
- Autenticação biométrica para operações sensíveis
- Verificação de integridade de comandos

## 📋 Roadmap

### Próximas Funcionalidades
- [ ] Execução automática de agendamentos
- [ ] Interface de terminal integrada
- [ ] Backup e sincronização
- [ ] Widgets de tela inicial
- [ ] Notificações push para eventos críticos

### Melhorias Planejadas
- [ ] Editor de código com syntax highlighting
- [ ] Histórico de execuções
- [ ] Métricas de performance
- [ ] Integração com Tasker
- [ ] Suporte a plugins externos

## 🤝 Contribuição

Este projeto utiliza as seguintes bibliotecas open source:
- **Jetpack Compose** - Apache License 2.0
- **Kotlin** - Apache License 2.0
- **Hilt** - Apache License 2.0
- **Material Design Components** - Apache License 2.0
- **AndroidX Libraries** - Apache License 2.0
- **Gson** - Apache License 2.0

## 📞 Suporte

### Reportar Problemas
- Abra uma issue no repositório do projeto
- Inclua logs e steps para reproduzir
- Especifique versão do Android e dispositivo

### FAQ
**P: O app funciona sem root?**
R: Sim, a maioria das funcionalidades funciona sem root. Apenas scripts que modificam configurações do sistema precisam de root.

**P: Os dados são enviados para servidores externos?**
R: Não, todos os dados permanecem localmente no dispositivo.

**P: Como ativar a autenticação biométrica?**
R: Vá em Configurações > Segurança > Autenticação biométrica e ative o switch.

## 📄 Licença

Este projeto é distribuído sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

---

**TCron v1.0.0** - Desenvolvido com ❤️ para a comunidade Android