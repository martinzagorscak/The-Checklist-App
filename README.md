# The Checklist App

The Checklist App is an Android prototype that renders a dynamic checklist UI from a remote JSON definition. It focuses on clean Compose-based rendering, support for nested checklist structures, and resilient data loading with local caching.

## Key Features

- **JSON-driven checklist rendering**: Builds the UI from a remote checklist payload instead of hardcoded screens.
- **Nested checklist structure**: Supports pages, sections, text blocks, images, and choice-based questions.
- **Single and multiple choice answers**: Handles both exclusive and multi-select response sets.
- **Detailed image view**: Tapping an image opens a dedicated full-screen preview.
- **Shared element transitions**: Smooth image and title transitions between the checklist and detail screen.
- **Connectivity-aware states**: Shows dedicated UI states for unstable network connection and data loading failures.
- **Local checklist cache**: Stores the latest successful checklist payload in Room for faster recovery and offline fallback.

## Technologies & Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Layered architecture with data, domain, and UI modules
- **Dependency Injection**: Koin
- **Networking**: Ktor
- **Serialization**: Kotlinx Serialization
- **Local Persistence**: Room
- **Image Loading**: Coil
- **Navigation**: Navigation Compose
- **Minimum SDK**: Android 24 (API Level 24)
- **Target SDK**: Android 37

## Project Structure

- `data/` -> API, cache, persistence, repository, and DI setup
- `domain/` -> domain models and use cases
- `ui/` -> screens, components, navigation, theme, and view models
- `device/` -> device-specific integrations such as connectivity monitoring

Open the project in Android Studio and run the `app` configuration on an emulator or physical device.

## Assumptions and Tradeoffs

- **Remote JSON schema drives the UI** -> Flexible and easy to extend, but strongly coupled to the payload structure and supported item types
- **Checklist answers are kept only in memory** -> Fast to implement and simple to manage, but user selections are not persisted across app restarts
- **Cached checklist stores the payload, not interaction state** -> Reliable content fallback, but no restoration of checked answers
- **Simplified Connectivity state** -> Clear UX for connection loss, but does not distinguish between partial failures and endpoint-specific issues
- **Room is used only as a lightweight cache** -> Good resilience with low complexity, but no advanced sync or conflict handling
- **No submission flow** -> Keeps the prototype focused on rendering and interaction, but the checklist cannot be completed or sent anywhere

## Potential Future Enhancements

- **Persist checklist selections**: Save checked answers locally so state survives process death and app relaunches.
- **Checklist submission flow**: Add validation, completion state, and result export or backend submission.
- **Richer input types**: Extend support with text input, date pickers, attachments, signatures, or comments.
- **Normalized checklist persistence**: Store JSON payload as related entities instead of a single blob for better query efficiency and data integrity.
- **Offline-first refresh strategy**: Improve caching behavior with timestamps, stale-data indicators, and smarter refresh rules.
- **Backend integration**: Replace the single static JSON endpoint with a managed API.

## Note:
GitHub repository is connected with Bitrise CI/CD, so you can test the by downloading app [here]().

**Specs can be found [here](specs.pdf).**

Video recordings:

[overview.webm](overview.webm)

For "retry case", it was simulated by throwing an exception on the first attempt to fetch the checklist payload.

[retry_case.webm](retry_case.webm)
