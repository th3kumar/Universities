# University Data App

## Overview

The University Data App is an Android application that fetches and displays a list of universities from an API. It employs various technologies, including Retrofit for API requests, MVVM architecture for data management, a foreground service for periodic data refreshing, WorkManager for service monitoring, and Coroutines for background tasks.

## Demonstration

Here is a video demonstration showcasing the UI components and foreground service in action:


https://github.com/th3kumar/Universities/assets/72141924/0dbc6962-06a7-426d-a395-9d4a294d7d78


## App Architecture & Project Structure

Here is the project's package structure and a high-level view of the architecture, highlighting the key components and their interactions.


![Untitled design (10)](https://github.com/th3kumar/Universities/assets/72141924/e0dba513-db60-4b83-9d94-b6b7687f9307)


## Technologies Used

- Kotlin
- Android SDK
- Retrofit (for API requests)
- MVVM Architecture
- Foreground Service
- WorkManager
- Coroutines

## App Architecture

### App Components

1. **UI Layer (View):**
   - Activities: The app's user interface consists of one or more activities.
   - Layouts: XML layout files define UI components' arrangement on the screen.
   - RecyclerView: Displays a list of universities fetched from the API.

2. **ViewModel Layer:**
   - `UniversityViewModel`: Acts as an intermediary between the UI and data, handling data retrieval and processing.
   - LiveData: Observes data changes in a lifecycle-aware manner.

3. **Repository Layer:**
   - `UniversityRepository`: Abstracts the data source (API) and provides clean APIs for data access.
   - API Interface: Defines API endpoints using Retrofit for network requests.

4. **Service Layer:**
   - `DataRefreshService`: A foreground service for periodic data refreshing, running even when the app is in the background or closed. It schedules API requests using a Handler and Coroutines.

5. **WorkManager Layer:**
   - `ServiceCheckerWorker`: A WorkManager worker for periodic checks, ensuring the `DataRefreshService` is always running.

6. **Networking Layer:**
   - Retrofit: Handles HTTP requests to the API and manages responses.

7. **Notification Manager:**
   - Android's Notification Manager: Creates a foreground notification for `DataRefreshService`, informing users of its status and keeping it in the foreground.

### Architecture Flow

1. **App Launch:**
   - When the app is launched (`MainActivity`), it initializes the `UniversityViewModel`.

2. **Data Fetching:**
   - `UniversityViewModel` fetches university data from the `UniversityRepository`.

3. **API Requests:**
   - `UniversityRepository` uses Retrofit to make API requests to retrieve university data.

4. **UI Presentation:**
   - LiveData observes the fetched data, and the UI (RecyclerView) updates in real-time to display the universities.

5. **Background Refresh:**
   - Simultaneously, the `DataRefreshService` runs in the background, periodically making API requests to refresh data.

6. **Data Update:**
   - The `DataRefreshService` posts updated data to `UniversityViewModel`, which updates the UI when new data is received.

### WorkManager Flow

1. **Periodic Checks:**
   - `ServiceCheckerWorker`, a WorkManager worker, periodically checks if the `DataRefreshService` is running.

2. **Service Restart:**
   - If the service isn't running, `ServiceCheckerWorker` starts the `DataRefreshService`, ensuring it remains active even when the app is not in the foreground.

## Future Improvements

- Implement a "Saved Universities" feature for users to bookmark universities.
- Add a "Search Universities" feature for specific university searches.

Contributions to enhance this project are welcome.

