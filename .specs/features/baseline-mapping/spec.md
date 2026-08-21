# Baseline Structural Mapping Specification

## Problem Statement
O projeto Friends Secrets possui uma arquitetura modular sofisticada, mas a documentação técnica (docs/ARCHITECTURE.md) está parcialmente defasada em relação à implementação real (Hilt vs Injeção Manual). É necessário estabelecer um baseline documentado sob a metodologia SDD para garantir consistência em desenvolvimentos futuros.

## Goals
- [ ] Documentar os padrões de injeção de dependência e navegação.
- [ ] Mapear as responsabilidades dos módulos core e feature.
- [ ] Garantir que novos UseCases sigam o padrão de escopo por feature.

## Out of Scope
| Feature | Reason |
| --- | --- |
| Refatoração de código | Este spec foca apenas em documentação e mapeamento, não em mudanças de código. |
| Testes de regressão | A validação é documental e estrutural. |

---

## Assumptions & Open Questions
| Assumption / decision | Chosen default | Rationale | Confirmed? |
| --- | --- | --- | --- |
| Padrão de DI | Hilt | Confirmado via @HiltAndroidApp no CustomApplication e build.gradle.kts. | y |
| Localização de UseCases | Feature-scoped | Observado em múltiplas features (chat, biometric, group). | y |
| Navegação entre módulos | Core Navigation | Baseado no módulo :core:navigation e interfaces CommonNavigator. | y |

**Open questions:** none - all resolved or logged above (required before the spec is confirmed).

---

## User Stories

### P1: Arquitetura e Injeção de Dependência ⭐ MVP
**User Story**: Como desenvolvedor, quero que as dependências sejam gerenciadas pelo Hilt para facilitar o desacoplamento e a testabilidade.

**Acceptance Criteria**:
1. The Friends Secrets system SHALL usar Hilt para injeção de dependência em todos os módulos Android. <!-- ubiquitous -->
2. The Application class SHALL ser anotada com @HiltAndroidApp. <!-- ubiquitous -->
3. WHEN uma nova feature for adicionada, THEN as dependências da camada Domain SHALL ser providas via constutor. <!-- event-driven -->

### P1: Estrutura Modular e Domínio
**User Story**: Como desenvolvedor, quero que a lógica de negócio de cada feature seja isolada em seu próprio módulo.

**Acceptance Criteria**:
1. The feature modules SHALL conter sua própria camada de Domain com UseCases específicos. <!-- ubiquitous -->
2. The core domain entities (como UserModel) SHALL residir no módulo :core:domain. <!-- ubiquitous -->
3. IF uma feature precisar de dados de outra, THEN ela SHALL interagir apenas via interfaces de repositório ou navegação. <!-- unwanted-behavior -->

---

## Edge Cases
- IF um módulo não-Android (pure Kotlin) precisar de DI, THEN o sistema SHALL usar injeção manual ou interfaces que o Hilt possa implementar nos módulos Android.
- WHEN houver conflitos de versão de dependência, THEN o sistema SHALL centralizar a resolução no `libs.versions.toml`.

---

## Requirement Traceability
| Requirement ID | Story | Phase | Status |
| --- | --- | --- | --- |
| BASE-01 | P1: DI e Hilt | Specify | Verified |
| BASE-02 | P1: Estr Estrutura Modular | Specify | Verified |
| BASE-03 | P1: Core Navigation | Specify | Verified |

**Coverage:** 3 total, 0 mapped to tasks, 0 unmapped.

---

## Success Criteria
- [ ] Documento STATE.md refletindo a realidade do código (Hilt, Módulos).
- [ ] Estrutura .specs/ inicializada e validada.
