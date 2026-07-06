# SolarSaver ☀️

Android app that estimates how much solar power a homeowner's roof could
produce. Enter your address and roof details, and the app fetches
coordinates, solar radiation data, and live weather to calculate an
estimated energy output.

Built as a BSc team project (team of 6) in Informatics at the
University of Oslo, using a GitHub pull-request workflow and Agile
practices.

## How it works

​```mermaid
graph TD
    A[Homeowner] -->|Uses| B(Estimate Solar Power Production)
    B --> C[Enter Address and Roof Details]
    B --> D[View Estimated Energy Output]
    B --> E[Fetch Coordinates from Address]
    B --> F[Retrieve Radiation Data]
    B --> G[Get Weather Data]
    B --> H[Calculate Net Radiation]
    B --> I[Calculate Power Production]
​```

The estimation model is documented in [MODELING.md](MODELING.md).

## Tech

- **Kotlin** (Android)
- Multiple external REST API integrations: geocoding, solar radiation
  data, and weather data
- Team workflow: GitHub pull requests, code review, Agile

## My role

I worked across all parts of the app, with main responsibility for the
frontend, design, and user interviews.

## Running the project

Open the project in Android Studio, let Gradle sync, and run on an
emulator or device (API level XX+).
```
