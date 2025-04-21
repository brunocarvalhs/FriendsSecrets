# Requisitos Funcionais

| Identificador | RF01 |
|---------------|-----------------------------|
| **Nome**      | Criação de novos grupos      |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 19/10/2024             |
| **Autor**     | Bruno Silva Carvalho         |
| **Data da última alteração** | 21/04/2025    |
| **Autor da última alteração** | OpenHands    |
| **Versão**    | 2                            |
| **Prioridade**| Alta                         |
| **Descrição** | O sistema deve permitir ao usuário criar novos grupos para amigos secretos, incluindo nome, descrição e adição de membros. O sistema deve gerar um token único para cada grupo criado, permitindo que outros usuários possam ingressar através deste token. |

| Identificador | RF02 |
|---------------|------------------------------------|
| **Nome**      | Exibição e gerenciamento de grupos |
| **Categoria** | Funcionalidade                    |
| **Data de criação** | 19/10/2024                  |
| **Autor**     | Bruno Silva Carvalho              |
| **Data da última alteração** | 21/04/2025         |
| **Autor da última alteração** | OpenHands         |
| **Versão**    | 2                                 |
| **Prioridade**| Alta                              |
| **Descrição** | O sistema deve permitir ao usuário visualizar detalhes do grupo, como nome, descrição, membros e status do sorteio. O usuário deve poder editar informações do grupo, adicionar/remover membros e excluir grupos que tenha criado. |

| Identificador | RF03 |
|---------------|-----------------------------|
| **Nome**      | Personalização de tema      |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 19/10/2024             |
| **Autor**     | Bruno Silva Carvalho         |
| **Data da última alteração** | 21/04/2025    |
| **Autor da última alteração** | OpenHands    |
| **Versão**    | 2                            |
| **Prioridade**| Média                        |
| **Descrição** | O sistema deve permitir ao usuário alternar entre tema claro e escuro nas configurações, com suporte a temas dinâmicos baseados nas configurações do sistema operacional. As preferências de tema devem ser salvas e aplicadas automaticamente em futuros acessos. |

| Identificador | RF04 |
|---------------|-----------------------------|
| **Nome**      | Autenticação por telefone   |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Alta                         |
| **Descrição** | O sistema deve permitir ao usuário se autenticar utilizando seu número de telefone. O processo deve incluir o envio de código de verificação via SMS e validação deste código para confirmar a identidade do usuário. |

| Identificador | RF05 |
|---------------|-----------------------------|
| **Nome**      | Navegação e interface       |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 19/10/2024             |
| **Autor**     | Bruno Silva Carvalho         |
| **Data da última alteração** | 21/04/2025    |
| **Autor da última alteração** | OpenHands    |
| **Versão**    | 2                            |
| **Prioridade**| Média                        |
| **Descrição** | O sistema deve fornecer uma interface intuitiva com navegação fluida entre telas, incluindo menu de navegação inferior para acesso às principais funcionalidades e navegação contextual para ações específicas dentro de cada tela. |

| Identificador | RF06 |
|---------------|-----------------------------|
| **Nome**      | Autenticação biométrica     |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 19/10/2024             |
| **Autor**     | Bruno Silva Carvalho         |
| **Data da última alteração** | 21/04/2025    |
| **Autor da última alteração** | OpenHands    |
| **Versão**    | 2                            |
| **Prioridade**| Alta                         |
| **Descrição** | O sistema deve permitir que o usuário faça login utilizando autenticação biométrica (impressão digital ou reconhecimento facial) após o primeiro login com telefone. O usuário deve poder ativar/desativar esta funcionalidade nas configurações. |

| Identificador | RF07 |
|---------------|-----------------------------|
| **Nome**      | Perfil de usuário           |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Alta                         |
| **Descrição** | O sistema deve permitir ao usuário criar e editar seu perfil, incluindo nome e foto. O perfil deve ser visível para outros membros dos grupos em que o usuário participa. |

| Identificador | RF08 |
|---------------|-----------------------------|
| **Nome**      | Configurações do aplicativo |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 19/10/2024             |
| **Autor**     | Bruno Silva Carvalho         |
| **Data da última alteração** | 21/04/2025    |
| **Autor da última alteração** | OpenHands    |
| **Versão**    | 2                            |
| **Prioridade**| Média                        |
| **Descrição** | O sistema deve oferecer uma seção de configurações onde o usuário pode personalizar a aparência, gerenciar preferências de notificação, alterar configurações de segurança e acessar informações sobre o aplicativo. |

| Identificador | RF09 |
|---------------|-----------------------------|
| **Nome**      | Sorteio de amigos secretos  |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Alta                         |
| **Descrição** | O sistema deve permitir ao administrador do grupo realizar o sorteio automático de amigos secretos. O sorteio deve garantir que ninguém tire a si mesmo e que todos os membros sejam incluídos. Após o sorteio, cada usuário deve poder ver apenas quem ele tirou, mantendo o sigilo para os demais. |

| Identificador | RF10 |
|---------------|-----------------------------|
| **Nome**      | Chat com IA para sugestões  |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Média                        |
| **Descrição** | O sistema deve oferecer um chat com inteligência artificial que permite ao usuário solicitar sugestões de presentes para seu amigo secreto. O chat deve ser contextual, permitindo que o usuário forneça informações sobre gostos e preferências para receber sugestões personalizadas. |

| Identificador | RF11 |
|---------------|-----------------------------|
| **Nome**      | Gerenciamento de preferências |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Média                        |
| **Descrição** | O sistema deve permitir ao usuário registrar suas preferências e gostos pessoais (como cores favoritas, hobbies, tamanhos de roupas) que podem ser compartilhados com seu amigo secreto para facilitar a escolha de presentes. |

| Identificador | RF12 |
|---------------|-----------------------------|
| **Nome**      | Compartilhamento de grupos  |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Média                        |
| **Descrição** | O sistema deve permitir ao administrador do grupo compartilhar o token de acesso com outros usuários através de mecanismos nativos de compartilhamento do dispositivo (WhatsApp, e-mail, SMS, etc.). |

| Identificador | RF13 |
|---------------|-----------------------------|
| **Nome**      | Feedback e suporte          |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Baixa                        |
| **Descrição** | O sistema deve oferecer mecanismos para que o usuário possa reportar problemas, enviar sugestões e acessar perguntas frequentes (FAQ) sobre o uso do aplicativo. |

| Identificador | RF14 |
|---------------|-----------------------------|
| **Nome**      | Revelação de amigo secreto  |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Média                        |
| **Descrição** | O sistema deve permitir a revelação do amigo secreto após a data definida para o evento, mostrando quem tirou quem para todos os participantes do grupo. |

| Identificador | RF15 |
|---------------|-----------------------------|
| **Nome**      | Onboarding para novos usuários |
| **Categoria** | Funcionalidade               |
| **Data de criação** | 21/04/2025             |
| **Autor**     | OpenHands                    |
| **Data da última alteração** | N/A           |
| **Autor da última alteração** | N/A          |
| **Versão**    | 1                            |
| **Prioridade**| Baixa                        |
| **Descrição** | O sistema deve fornecer um processo de onboarding para novos usuários, apresentando as principais funcionalidades do aplicativo e orientando sobre como criar ou participar de grupos de amigos secretos. |
