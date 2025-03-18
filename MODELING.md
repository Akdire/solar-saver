■ Beskrivelse og diagrammer, vi anbefaler å generere dem med
Mermaid som vist på forelesning. Se kravene til modellering
lenger ned i dette dokumentet. Ha med hvorfor diagrammet er
valgt og hva dere ønsker å med det.

# Use Case Diagram
Use Case helpe developers to see the overview of the main functionality of the app.
``` mermaid
graph TD
    A[Homeowner] -->|Uses| B(Estimate Solar Power Production)
    B --> C[Enter Address and Roof Details]
    B --> D[View Estimated Energy Output]
    B --> E[Fetch Coordinates from Address]
    B --> F[Retrieve Radiation Data]
    B --> G[Get Weather Data]
    B --> H[Calculate Net Radiation]
    B --> I[Calculate Power Production]
```
# Klassediagram
Klassediagrammet er her for at utviklere skal ha oversikt over hvilke klasser vi har i appen og sammenhengene mellom dem.
```mermaid
classDiagram
direction LR
%% Class definitions
class RadiationDatasource {
        - HttpClient ktorHttpClient
        + getRadiationForLocation(Double latitude, Double longitude) List~MonthlyRadiation~
    }

class RadiationRepository {
        - RadiationDatasource datasource
        + getRadiationData(Double latitude, Double longitude) List~Double~
        + getRadiationDataForMonth(Double latitude, Double longitude, Int month) Double
        - calculateAverage(List~MonthlyRadiation~ items) List~Double~
    }

class RadiationOutputs {
        + Outputs outputs
    }

class Outputs {
        + List~MontlyRadiation~ monthly
    }

class MonthlyRadiation {
        + Int year
        + Int month
        + Double radiation
    }

class FrostDatasource {
        - String clientId
        - String clientPassword
        - HttpClient ktorHttpClient
        + getLocationInfo(Double longitude, Double latitude) List~LocationInfo~
        + getObservations(List~String~ ids, String timeframe = "latest") List~ObservationInfo~
    }

class WeatherRepository {
        - FrostDataource datasource
        + getCurrentWeatherData(Double longitude, Double latitude), List~Double~
        + getHistoricalAverageWeatherData(Double longitude, Double latitude, String timeframe) List~Double~
    }

class MetaDataLocation {
        + List~LocationInfo~ data
    }

class LocationInfo {
        + String id
    }

class OutputObservation {
        List~ObservationMetaData~ data
    }

class ObservationMetaData {
        List~ObservationInfo~ observations
    }

class ObservationInfo {
        + String elementId
        + Double value
    }

%% Class relations
RadiationDatasource "1" --* "1" RadiationRepository

RadiationOutputs "1" --* "1" Outputs

Outputs "1" --* "*" MonthlyRadiation

FrostDatasource "1" --* "1" WeatherRepository

MetaDataLocation "1" --* "*" LocationInfo

OutputObservation "1" --* "*" ObservationMetaData

ObservationMetaData "1" --* "*" ObservationInfo
```

# sequenceDiagram
 ``` mermaid
   sequenceDiagram
    actor User
    participant UI as HomeScreen
    participant VM as HomeViewModel
    participant CR as CoordinateRepository
    participant RR as RadiationRepository
    participant WR as WeatherRepository
    participant Calc as Calculations
    

    User->>UI: Enter address, roof details
    User->>UI: Press "Beregn estimert energi"
    UI->>VM: onButtonClick(address, area, degrees, direction)
    VM->>CR: convertAddressToCoordinates(address)
    CR-->>VM: [latitude, longitude]
    VM->>RR: getRadiationData(latitude, longitude)
    RR-->>VM: [overallAvgRadiation, summerAvg, winterAvg]
    VM->>WR: getHistoricalAverageWeatherData(latitude, longitude, timeframe)
    WR-->>VM: [temperature, cloudiness, snowLevel]
    VM->>Calc: calculateNetRadiation(radiation, temp, cloud, snow)
    Calc-->>VM: netRadiation
    VM->>Calc: calculatePowerProduction(netRadiation, area, degrees, direction)
    Calc-->>VM: expectedPower
    VM->>UI: Update HomeUIState(netRadiation, expectedPower)
    UI-->>User: Display results (e.g., 137 kWh/m², 1491 kWh)
```
