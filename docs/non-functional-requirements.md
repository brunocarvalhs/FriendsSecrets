# Requisitos Não Funcionais

| Identificador | RNF01 |
|---------------|-----------------------------|
| **Nome**      | Desempenho                   |
| **Categoria** | Não Funcional                |
| **Data de criação** | 19/10/2024             |
| **Autor**     | Bruno Silva Carvalho         |
| **Data da última alteração** | 21/04/2025    |
| **Autor da última alteração** | OpenHands    |
| **Versão**    | 2                            |
| **Prioridade**| Essencial                    |
| **Descrição** | O sistema deve garantir o processamento eficiente de dados, com tempo de resposta máximo de 2 segundos para operações comuns como carregamento de grupos e perfis. O tempo de resposta para operações mais complexas como sorteio de amigos secretos não deve exceder 5 segundos. O aplicativo deve monitorar o desempenho usando Firebase Performance para identificar e corrigir gargalos. |

| Identificador | RNF02 |
|---------------|-----------------------------|
| **Nome**      | Segurança de Dados           |
| **Categoria** | Não Funcional                |
| **Data de criação** | 19/10/2024             |
| **Autor**     | Bruno Silva Carvalho         |
| **Data da última alteração** | 21/04/2025    |
| **Autor da última alteração** | OpenHands    |
| **Versão**    | 2                            |
| **Prioridade**| Alta                         |
| **Descrição** | O sistema deve implementar práticas robustas de segurança, incluindo: autenticação por telefone com verificação em duas etapas, autenticação biométrica opcional, criptografia de dados sensíveis em repouso e em trânsito, regras de segurança no Firestore para garantir que usuários só acessem dados autorizados, e proteção contra ataques comuns como injeção e XSS. O aplicativo deve seguir as diretrizes de segurança do Android e as melhores práticas do Firebase. |

| Identificador | RNF03 |
|---------------|-----------------------------|
| **Nome**      | Compatibilidade e Responsividade |
| **Categoria** | Não Funcional                |
| **Data de criação** | 19/10/2024             |
| **Autor**     | Bruno Silva Carvalho         |
| **Data da última alteração** | 21/04/2025    |
| **Autor da última alteração** | OpenHands    |
| **Versão**    | 2                            |
| **Prioridade**| Alta                         |
| **Descrição** | O aplicativo deve ser compatível com dispositivos Android rodando API 24 (Android 7.0 Nougat) ou superior. A interface deve ser responsiva e se adaptar a diferentes tamanhos de tela, desde smartphones compactos até tablets. O aplicativo deve seguir as diretrizes de Material Design 3 e suportar tanto orientação retrato quanto paisagem quando apropriado. |

| Identificador | RNF04 |
|---------------|-----------------------------|
| **Nome**      | Escalabilidade               |
| **Categoria** | Não Funcional                |
| **Data de criação** | 19/10/2024             |
| **Autor**     | Bruno Silva Carvalho         |
| **Data da última alteração** | 21/04/2025    |
| **Autor da última alteração** | OpenHands    |
| **Versão**    | 2                            |
| **Prioridade**| Média                        |
| **Descrição** | A arquitetura do sistema deve suportar escalabilidade para lidar com crescimento no número de usuários e grupos. O backend baseado em Firebase deve ser configurado para escalar automaticamente durante períodos de pico (como épocas festivas quando o uso de amigos secretos aumenta). O sistema deve ser capaz de lidar com pelo menos 100.000 usuários ativos e 50.000 grupos sem degradação significativa de desempenho. |

| Identificador | RNF05 |
|---------------|-----------------------------|
| **Nome**      | Usabilidade                  |
| **Categoria** | Não Funcional                |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Alta                         |
| **Descrição** | A interface do usuário deve ser intuitiva e fácil de usar, com fluxos de navegação claros e consistentes. O aplicativo deve seguir as diretrizes de acessibilidade do Android, incluindo suporte a leitores de tela, contraste adequado e tamanhos de texto ajustáveis. O sistema deve fornecer feedback claro para ações do usuário e mensagens de erro informativas quando ocorrerem problemas. |

| Identificador | RNF06 |
|---------------|-----------------------------|
| **Nome**      | Disponibilidade              |
| **Categoria** | Não Funcional                |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Alta                         |
| **Descrição** | O sistema deve estar disponível 24/7, com tempo de inatividade planejado não excedendo 0,1% (aproximadamente 8,8 horas por ano). O aplicativo deve implementar mecanismos de cache para permitir funcionalidade básica mesmo quando offline, sincronizando dados quando a conexão for restaurada. |

| Identificador | RNF07 |
|---------------|-----------------------------|
| **Nome**      | Manutenibilidade             |
| **Categoria** | Não Funcional                |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Média                        |
| **Descrição** | O código do aplicativo deve seguir a arquitetura Clean Architecture com MVVM para facilitar manutenção e testes. O sistema deve usar injeção de dependência para desacoplamento de componentes. A documentação do código deve ser clara e abrangente, seguindo padrões KDoc. O sistema deve incluir testes unitários e de integração com cobertura mínima de 70% para o código de negócios. |

| Identificador | RNF08 |
|---------------|-----------------------------|
| **Nome**      | Monitoramento e Análise      |
| **Categoria** | Não Funcional                |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Média                        |
| **Descrição** | O sistema deve implementar monitoramento abrangente usando Firebase Crashlytics para rastreamento de erros e Firebase Analytics para análise de comportamento do usuário. Logs detalhados devem ser mantidos para facilitar a depuração, com diferentes níveis de severidade. Métricas de desempenho devem ser coletadas para identificar áreas de melhoria. |

| Identificador | RNF09 |
|---------------|-----------------------------|
| **Nome**      | Privacidade                  |
| **Categoria** | Não Funcional                |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Alta                         |
| **Descrição** | O sistema deve cumprir com regulamentos de privacidade como LGPD (Brasil) e GDPR (quando aplicável). O aplicativo deve incluir uma política de privacidade clara e acessível. Os usuários devem ter controle sobre seus dados pessoais, incluindo a capacidade de excluir sua conta e todos os dados associados. O sistema deve coletar apenas os dados necessários para o funcionamento do aplicativo. |

| Identificador | RNF10 |
|---------------|-----------------------------|
| **Nome**      | Configuração Remota          |
| **Categoria** | Não Funcional                |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Baixa                        |
| **Descrição** | O sistema deve utilizar Firebase Remote Config para permitir alterações dinâmicas em configurações do aplicativo sem necessidade de atualizações. Isso inclui a capacidade de ativar/desativar recursos, modificar textos e ajustar parâmetros de desempenho remotamente. |
