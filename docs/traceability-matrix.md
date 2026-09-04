# Traceability Matrix: Friends Secrets

This document establishes the relationship between functional requirements, non-functional requirements, and use cases, ensuring that every business need is mapped to a system interaction or implementation strategy.

## 1. Functional Requirements (FR) x Use Cases (UC)

| Requirement | Description | Related Use Cases |
|:---|:---|:---|
| **FR01** | Group Creation | UC03 |
| **FR02** | Secure Invitations | UC03, UC09 |
| **FR03** | Contact Integration | UC03 |
| **FR04** | Automated Drawing | UC05 |
| **FR05** | Result Secrecy | UC06 |
| **FR06** | Identity Reveal | UC06 |
| **FR07** | AI Gift Assistant | UC07 |
| **FR08** | Biometric Authentication | UC01, UC02 |
| **FR09** | User Profiles | UC02 |
| **FR10** | Real-time Notifications | UC05, UC10 |
| **FR11** | Theme Customization | UC08 |
| **FR12** | Multi-language Support | System-wide |

## 2. Use Cases (UC) x Functional Requirements (FR)

| Use Case | Description | Related Requirements |
|:---|:---|:---|
| **UC01** | Secure User Authentication | FR08 |
| **UC02** | Profile & Preference Management | FR08, FR09 |
| **UC03** | Group Creation & Orchestration | FR01, FR02, FR03 |
| **UC04** | Group Management | FR01, FR02 |
| **UC05** | Automated Secret Santa Draw | FR04, FR10 |
| **UC06** | Result Retrieval | FR05, FR06 |
| **UC07** | AI-Enhanced Gift Assistance | FR07 |
| **UC08** | System Personalization | FR11 |
| **UC09** | Secure Group Sharing | FR02 |
| **UC10** | Support & Feedback | FR10 |

## 3. Non-Functional Requirements (NFR) x Implementation Strategy

| Requirement | Description | Implementation / Technology |
|:---|:---|:---|
| **NFR01** | Performance & Latency | Baseline Profiles, Coroutines, Room/Firestore Caching |
| **NFR02** | Security & Encryption | Android Keystore, TLS 1.3, AES-256 |
| **NFR03** | Global Privacy Compliance | Local Contact Processing, GDPR/LGPD Consent API |
| **NFR04** | Scalability | Firebase Cloud Functions, Auto-scaling Firestore |
| **NFR05** | Availability | Offline-first Architecture, WorkManager Sync |
| **NFR06** | Accessibility (A11y) | Material 3 Semantic UI, WCAG 2.2 Testing |
| **NFR07** | Maintainability | Clean Architecture, Modular MVVM, Kover (Code Coverage) |
| **NFR08** | Energy Efficiency | Background Task Scheduling (WorkManager), API 35+ Standards |
| **NFR09** | Observability | Firebase Crashlytics, Sentry, Custom Analytics Module |
| **NFR10** | Localization & I18n | Android Resource Bundles (20+ locales), RTL Support |

## 4. Requirement Coverage Summary

### Functional Requirements
- **Fully Covered:** 100% (All FRs mapped to UCs or System Components).
- **Verification Method:** Unit Tests and Manual QA Journeys.

### Non-Functional Requirements
- **Implementation Identified:** 100% (All NFRs mapped to specific technologies or architectural patterns).
- **Verification Method:** Benchmarking, Security Audits, and Accessibility Scanner.

---
© 2026 Brunocarvalhs. All rights reserved.
