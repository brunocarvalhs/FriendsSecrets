# Casos de Uso

Este documento descreve os principais casos de uso do aplicativo Friends Secrets, detalhando as interações entre os usuários e o sistema.

## Índice

1. [UC01 - Autenticação de Usuário](#uc01---autenticação-de-usuário)
2. [UC02 - Gerenciamento de Perfil](#uc02---gerenciamento-de-perfil)
3. [UC03 - Criação de Grupo](#uc03---criação-de-grupo)
4. [UC04 - Gerenciamento de Grupo](#uc04---gerenciamento-de-grupo)
5. [UC05 - Sorteio de Amigos Secretos](#uc05---sorteio-de-amigos-secretos)
6. [UC06 - Visualização de Amigo Secreto](#uc06---visualização-de-amigo-secreto)
7. [UC07 - Obtenção de Sugestões de Presentes](#uc07---obtenção-de-sugestões-de-presentes)
8. [UC08 - Personalização de Tema](#uc08---personalização-de-tema)
9. [UC09 - Compartilhamento de Grupo](#uc09---compartilhamento-de-grupo)
10. [UC10 - Envio de Feedback](#uc10---envio-de-feedback)

---

## UC01 - Autenticação de Usuário

**Atores:** Usuário

**Descrição:** Este caso de uso descreve o processo de autenticação do usuário no aplicativo.

**Pré-condições:**
- O usuário possui o aplicativo instalado
- O usuário possui um número de telefone válido

**Fluxo Principal:**
1. O usuário abre o aplicativo
2. O sistema exibe a tela de login
3. O usuário seleciona a opção de login com telefone
4. O usuário insere seu número de telefone
5. O sistema envia um código de verificação via SMS
6. O usuário insere o código recebido
7. O sistema valida o código
8. O sistema autentica o usuário e exibe a tela principal

**Fluxos Alternativos:**

*A1: Usuário já autenticado anteriormente*
1. O usuário abre o aplicativo
2. O sistema detecta uma sessão válida
3. O sistema exibe a tela de autenticação biométrica (se ativada)
4. O usuário fornece sua biometria
5. O sistema autentica o usuário e exibe a tela principal

*A2: Código de verificação inválido*
1. No passo 7 do fluxo principal, o sistema identifica que o código é inválido
2. O sistema exibe uma mensagem de erro
3. O sistema permite que o usuário tente novamente ou solicite um novo código

**Pós-condições:**
- O usuário está autenticado no sistema
- O sistema exibe a tela principal do aplicativo

---

## UC02 - Gerenciamento de Perfil

**Atores:** Usuário autenticado

**Descrição:** Este caso de uso descreve como o usuário pode criar e gerenciar seu perfil.

**Pré-condições:**
- O usuário está autenticado no sistema

**Fluxo Principal:**
1. O usuário acessa a seção de perfil
2. O sistema exibe as informações atuais do perfil
3. O usuário seleciona a opção de editar perfil
4. O sistema exibe um formulário com os campos editáveis
5. O usuário atualiza seu nome e/ou foto
6. O usuário confirma as alterações
7. O sistema valida e salva as informações
8. O sistema exibe uma mensagem de sucesso

**Fluxos Alternativos:**

*A1: Usuário cancela a edição*
1. Em qualquer ponto após o passo 3, o usuário cancela a operação
2. O sistema descarta as alterações não salvas
3. O sistema retorna à visualização do perfil

*A2: Erro ao salvar alterações*
1. No passo 7, ocorre um erro ao salvar as informações
2. O sistema exibe uma mensagem de erro
3. O sistema mantém o formulário aberto com os dados inseridos

**Pós-condições:**
- O perfil do usuário está atualizado no sistema

---

## UC03 - Criação de Grupo

**Atores:** Usuário autenticado

**Descrição:** Este caso de uso descreve como o usuário pode criar um novo grupo de amigos secretos.

**Pré-condições:**
- O usuário está autenticado no sistema

**Fluxo Principal:**
1. O usuário acessa a tela principal
2. O usuário seleciona a opção de criar novo grupo
3. O sistema exibe um formulário para criação do grupo
4. O usuário preenche o nome do grupo
5. O usuário adiciona uma descrição (opcional)
6. O usuário confirma a criação
7. O sistema gera um token único para o grupo
8. O sistema cria o grupo e adiciona o usuário como administrador
9. O sistema exibe a tela de detalhes do grupo recém-criado

**Fluxos Alternativos:**

*A1: Usuário cancela a criação*
1. Em qualquer ponto após o passo 2, o usuário cancela a operação
2. O sistema descarta as informações não salvas
3. O sistema retorna à tela principal

*A2: Erro na criação do grupo*
1. No passo 8, ocorre um erro ao criar o grupo
2. O sistema exibe uma mensagem de erro
3. O sistema permite que o usuário tente novamente

**Pós-condições:**
- Um novo grupo é criado no sistema
- O usuário é definido como administrador do grupo
- O sistema gera um token único para o grupo

---

## UC04 - Gerenciamento de Grupo

**Atores:** Usuário autenticado (administrador do grupo)

**Descrição:** Este caso de uso descreve como o administrador pode gerenciar um grupo existente.

**Pré-condições:**
- O usuário está autenticado no sistema
- O usuário é administrador de pelo menos um grupo

**Fluxo Principal:**
1. O usuário acessa a lista de grupos
2. O usuário seleciona um grupo que administra
3. O sistema exibe os detalhes do grupo
4. O usuário seleciona a opção de editar grupo
5. O sistema exibe um formulário com os campos editáveis
6. O usuário atualiza as informações do grupo
7. O usuário confirma as alterações
8. O sistema valida e salva as informações
9. O sistema exibe uma mensagem de sucesso

**Fluxos Alternativos:**

*A1: Adicionar membros ao grupo*
1. No passo 3, o usuário seleciona a opção de adicionar membros
2. O sistema exibe uma interface para adicionar membros
3. O usuário adiciona novos membros por contato ou compartilha o token
4. O sistema adiciona os novos membros ao grupo

*A2: Remover membros do grupo*
1. No passo 3, o usuário seleciona um membro do grupo
2. O usuário seleciona a opção de remover membro
3. O sistema solicita confirmação
4. O usuário confirma a remoção
5. O sistema remove o membro do grupo

*A3: Excluir grupo*
1. No passo 3, o usuário seleciona a opção de excluir grupo
2. O sistema solicita confirmação
3. O usuário confirma a exclusão
4. O sistema exclui o grupo e notifica todos os membros

**Pós-condições:**
- As alterações no grupo são salvas no sistema
- Os membros afetados são notificados quando apropriado

---

## UC05 - Sorteio de Amigos Secretos

**Atores:** Usuário autenticado (administrador do grupo)

**Descrição:** Este caso de uso descreve como o administrador realiza o sorteio de amigos secretos em um grupo.

**Pré-condições:**
- O usuário está autenticado no sistema
- O usuário é administrador de pelo menos um grupo
- O grupo possui pelo menos 3 membros
- O sorteio ainda não foi realizado

**Fluxo Principal:**
1. O usuário acessa os detalhes do grupo
2. O usuário seleciona a opção de realizar sorteio
3. O sistema exibe uma tela de confirmação
4. O usuário confirma o sorteio
5. O sistema realiza o sorteio aleatório
6. O sistema armazena os resultados do sorteio
7. O sistema notifica todos os membros
8. O sistema exibe uma mensagem de sucesso

**Fluxos Alternativos:**

*A1: Grupo com menos de 3 membros*
1. No passo 5, o sistema detecta que o grupo possui menos de 3 membros
2. O sistema exibe uma mensagem informando que são necessários pelo menos 3 membros
3. O sistema cancela o sorteio

*A2: Sorteio já realizado*
1. No passo 2, o sistema detecta que o sorteio já foi realizado
2. O sistema exibe uma mensagem informando que o sorteio já foi realizado
3. O sistema oferece a opção de realizar um novo sorteio (cancelando o anterior)

**Pós-condições:**
- O sorteio é realizado e os resultados são armazenados
- Cada membro pode ver apenas quem ele tirou
- O administrador pode ver todos os resultados

---

## UC06 - Visualização de Amigo Secreto

**Atores:** Usuário autenticado (membro do grupo)

**Descrição:** Este caso de uso descreve como o usuário visualiza quem ele tirou no sorteio.

**Pré-condições:**
- O usuário está autenticado no sistema
- O usuário é membro de pelo menos um grupo
- O sorteio já foi realizado no grupo

**Fluxo Principal:**
1. O usuário acessa a lista de grupos
2. O usuário seleciona um grupo onde participou do sorteio
3. O sistema exibe os detalhes do grupo
4. O sistema exibe quem o usuário tirou no sorteio
5. O usuário pode ver informações sobre a pessoa que tirou

**Fluxos Alternativos:**

*A1: Sorteio ainda não realizado*
1. No passo 3, o sistema detecta que o sorteio ainda não foi realizado
2. O sistema exibe uma mensagem informando que o sorteio ainda não foi realizado
3. O sistema não exibe informações sobre o amigo secreto

**Pós-condições:**
- O usuário visualiza quem ele tirou no sorteio

---

## UC07 - Obtenção de Sugestões de Presentes

**Atores:** Usuário autenticado (membro do grupo)

**Descrição:** Este caso de uso descreve como o usuário obtém sugestões de presentes para seu amigo secreto.

**Pré-condições:**
- O usuário está autenticado no sistema
- O usuário é membro de pelo menos um grupo
- O sorteio já foi realizado no grupo

**Fluxo Principal:**
1. O usuário acessa os detalhes do grupo
2. O usuário visualiza quem ele tirou no sorteio
3. O usuário seleciona a opção de obter sugestões
4. O sistema abre o chat com IA
5. O usuário descreve preferências ou características do amigo secreto
6. O sistema gera sugestões personalizadas de presentes
7. O usuário visualiza as sugestões

**Fluxos Alternativos:**

*A1: Usuário inicia chat diretamente*
1. O usuário acessa a seção de chat com IA
2. O usuário informa sobre quem deseja sugestões
3. O sistema gera sugestões baseadas nas informações fornecidas

*A2: Erro na geração de sugestões*
1. No passo 6, ocorre um erro na geração de sugestões
2. O sistema exibe uma mensagem de erro
3. O sistema permite que o usuário tente novamente

**Pós-condições:**
- O usuário recebe sugestões personalizadas de presentes

---

## UC08 - Personalização de Tema

**Atores:** Usuário autenticado

**Descrição:** Este caso de uso descreve como o usuário personaliza o tema do aplicativo.

**Pré-condições:**
- O usuário está autenticado no sistema

**Fluxo Principal:**
1. O usuário acessa as configurações do aplicativo
2. O usuário seleciona a opção de aparência
3. O sistema exibe as opções de tema disponíveis
4. O usuário seleciona o tema desejado (claro, escuro ou sistema)
5. O sistema aplica o tema selecionado imediatamente
6. O sistema salva a preferência do usuário

**Fluxos Alternativos:**

*A1: Usuário seleciona tema do sistema*
1. No passo 4, o usuário seleciona a opção "Seguir sistema"
2. O sistema configura o tema para seguir as configurações do dispositivo
3. O sistema salva a preferência do usuário

**Pós-condições:**
- O tema do aplicativo é alterado conforme a seleção do usuário
- A preferência é salva para futuros acessos

---

## UC09 - Compartilhamento de Grupo

**Atores:** Usuário autenticado (administrador do grupo)

**Descrição:** Este caso de uso descreve como o administrador compartilha o acesso ao grupo.

**Pré-condições:**
- O usuário está autenticado no sistema
- O usuário é administrador de pelo menos um grupo

**Fluxo Principal:**
1. O usuário acessa os detalhes do grupo
2. O usuário seleciona a opção de compartilhar grupo
3. O sistema exibe o token de acesso ao grupo
4. O usuário seleciona o método de compartilhamento
5. O sistema abre a interface de compartilhamento do dispositivo
6. O usuário compartilha o token com os contatos desejados

**Fluxos Alternativos:**

*A1: Usuário copia o token*
1. No passo 3, o usuário seleciona a opção de copiar token
2. O sistema copia o token para a área de transferência
3. O sistema exibe uma mensagem de confirmação

**Pós-condições:**
- O token de acesso ao grupo é compartilhado com potenciais novos membros

---

## UC10 - Envio de Feedback

**Atores:** Usuário autenticado

**Descrição:** Este caso de uso descreve como o usuário envia feedback sobre o aplicativo.

**Pré-condições:**
- O usuário está autenticado no sistema

**Fluxo Principal:**
1. O usuário acessa as configurações do aplicativo
2. O usuário seleciona a opção de feedback
3. O sistema exibe um formulário para envio de feedback
4. O usuário preenche o formulário com sua mensagem
5. O usuário envia o feedback
6. O sistema registra o feedback
7. O sistema exibe uma mensagem de agradecimento

**Fluxos Alternativos:**

*A1: Usuário reporta um problema*
1. No passo 3, o usuário seleciona a opção de reportar problema
2. O sistema exibe um formulário específico para relato de problemas
3. O usuário preenche os detalhes do problema
4. O usuário envia o relatório
5. O sistema registra o problema
6. O sistema exibe uma mensagem de confirmação

**Pós-condições:**
- O feedback ou relatório de problema é registrado no sistema