# Use Cases: Friends Secrets

This document outlines the primary use cases for the Friends Secrets mobile application, detailing the interactions between users (Actors) and the system.

## Index

1. [UC01 - Secure User Authentication](#uc01---secure-user-authentication)
2. [UC02 - Profile & Preference Management](#uc02---profile--preference-management)
3. [UC03 - Group Creation & Orchestration](#uc03---group-creation--orchestration)
4. [UC04 - Group Management](#uc04---group-management)
5. [UC05 - Automated Secret Santa Draw](#uc05---automated-secret-santa-draw)
6. [UC06 - Result Retrieval](#uc06---result-retrieval)
7. [UC07 - AI-Enhanced Gift Assistance](#uc07---ai-enhanced-gift-assistance)
8. [UC08 - System Personalization](#uc08---system-personalization)
9. [UC09 - Secure Group Sharing](#uc09---secure-group-sharing)
10. [UC10 - Support & Feedback](#uc10---support--feedback)

---

## UC01 - Secure User Authentication

**Actors:** User

**Description:** Standard process for identifying and authenticating the user within the platform using multi-factor methods.

**Pre-conditions:**
- The App is installed on a compatible Android device.
- Active cellular or internet connection.

**Main Flow:**
1. User opens the App.
2. System presents the authentication gateway.
3. User selects "Phone Authentication".
4. User enters their phone number.
5. System triggers a secure verification code via SMS (Firebase Auth).
6. User enters the received code.
7. System validates the session and grants access.
8. System prompts for Biometric enrollment (if supported and not yet enabled).

**Alternative Flows:**

*A1: Returning User (Biometric)*
1. User opens the App.
2. System detects an active session and requests Biometric verification.
3. User provides biometric input (Fingerprint/Face).
4. System grants immediate access to the Dashboard.

*A2: Authentication Failure*
1. At step 7, the code is invalid or expired.
2. System provides an error message and the option to resend.

**Post-conditions:**
- User is securely logged in with a valid JWT/Session token.

---

## UC03 - Group Creation & Orchestration

**Actors:** Authenticated User (Organizer)

**Description:** The process of initiating a new Secret Santa event.

**Pre-conditions:**
- User is authenticated.

**Main Flow:**
1. User selects "Create New Group".
2. User provides group details (Name, Description, Gift Budget, Event Date).
3. User interacts with the Local Contact Picker to add participants.
4. System filters and processes contact data locally (Privacy-First).
5. User confirms creation.
6. System generates a unique Group ID and secure Invitation Token.
7. System designates the creator as "Administrator".

**Post-conditions:**
- Group is persisted in the cloud.
- Administrator can now share the access token.

---

## UC05 - Automated Secret Santa Draw

**Actors:** Group Administrator

**Description:** The core logic execution where participants are matched.

**Pre-conditions:**
- Group has at least 3 participants.
- The draw hasn't been executed yet.

**Main Flow:**
1. Administrator accesses Group Settings.
2. Administrator triggers "Execute Draw".
3. System runs the randomized matching algorithm (ensuring no self-draws).
4. System encrypts individual results.
5. System triggers push notifications to all participants.
6. Group status changes to "Drawn".

**Post-conditions:**
- Results are locked and available for individual viewing.

---

## UC07 - AI-Enhanced Gift Assistance

**Actors:** Authenticated User (Participant)

**Description:** Leveraging Generative AI to find the perfect gift for a drawn partner.

**Pre-conditions:**
- User has successfully drawn a partner in a group.
- Partner has provided "Wish List" or preferences.

**Main Flow:**
1. User views their drawn partner's profile.
2. User selects "Get AI Suggestions".
3. System feeds anonymized preferences and budget into the Generative AI engine (Gemini).
4. System displays a curated list of gift ideas with direct links (if applicable).
5. User can refine suggestions through a conversational interface.

**Post-conditions:**
- User obtains personalized gift ideas.

---
© 2026 Brunocarvalhs. All rights reserved.
