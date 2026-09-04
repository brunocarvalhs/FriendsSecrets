# Maestro Tests for FriendsSecrets

This directory contains Maestro tests for the FriendsSecrets app.

## Prerequisites

- **Java JDK**: Maestro requires Java. Ensure `JAVA_HOME` is set.
  - On macOS: `export JAVA_HOME=$(/usr/libexec/java_home)`
- **Maestro CLI**: `curl -fsSL "https://get.maestro.mobile.dev" | bash`
- **PATH**: Add Maestro to your PATH: `export PATH="$PATH":"$HOME/.maestro/bin"`
- **Running Emulator/Device**: Have an Android emulator or device connected.

## Running Tests

To run all tests:

```bash
maestro test .maestro/test_suite.yaml
```

To run a specific test:

```bash
maestro test .maestro/flows/login_flow.yaml
```

## Test Flows

The full regression suite (`test_suite.yaml`) runs these flows in order. The order matters:
`create_group_flow` creates the "Grupo de Natal 2024" fixture that
`group_details_flow`, `group_edit_flow`, `chat_flow` and `draw_flow` depend on,
and `draw_flow` deletes that group at the end, so nothing after it may rely on
it still existing.

- **home_flow.yaml**: Tests the home screen (search bar, status chips, empty state, FAB, entry points)
- **create_group_flow.yaml**: Tests creating a group with members, then confirms it appears under "Archived"
- **group_details_flow.yaml**: Tests the group details screen (header, description, draw details, participants, navigation to Invite/Chat/Draw)
- **group_edit_flow.yaml**: Tests editing a group's name, description and price range
- **chat_flow.yaml**: Tests entering the group chat, including the nickname prompt for anonymous participants
- **draw_flow.yaml**: Tests running the Secret Santa draw, revealing results, then deleting the group
- **search_filter_flow.yaml**: Tests the search field and the Active/Drawn/Not drawn/Archived filter chips
- **enter_group.yaml**: Tests joining an existing group by code and leaving it
- **settings_flow.yaml**: Tests the settings screens (General, Appearance, Review App, Legal)
- **deeplink_flow.yaml**: Tests opening the app via its `friendssecrets://` deep link, including joining a group via `friendssecrets://join?code=...`

## CI Integration

You can integrate these tests with your CI pipeline. See the [Maestro documentation](https://docs.maestro.dev/getting-started/running-flows-on-ci) for more information.
