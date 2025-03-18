■ Beskrivelse og diagrammer, vi anbefaler å generere dem med
Mermaid som vist på forelesning. Se kravene til modellering
lenger ned i dette dokumentet. Ha med hvorfor diagrammet er
valgt og hva dere ønsker å med det.

# Use Case Diagram
Use Case helpe developers to see the overview of the main functionality of the app. we always work together
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
Bruksområde: Estimering av solenergiproduksjon for en boligeiendom

Navn: Anslå solenergiproduksjon
actor: Huseier (bruker)
Mål: Å beregne forventet månedlig solenergiproduksjon (i kWh) for en gitt eiendom basert på dens beliggenhet, takegenskaper og miljøforhold.

Forutsetninger (preconditions):

•	Brukeren har en enhet med SolarSaver-appen installert (Android-basert, gitt koden).
•	Appen har internettilgang for å hente data fra eksterne APIer (GeoNorge, PVGIS, Frost).
•	Brukeren kjenner sin adresse og grunnleggende takdetaljer (areal, vinkel, retning).

Hovedscenario for suksess:

1.	Start appen:
•	Brukeren starter SolarSaver-appen på sin Android-enhet.
•	Appen viser HomeScreen, med inndatafelt og et rent grensesnitt stil med SolarSaverTheme.

2.	Skriv inn adresse:
•	Brukeren legger inn adressen sin (f.eks. "Storgata 1") i OutlinedTextField merket "Adresse".
•	Appen godtar inngangen som en streng (f.eks. "Storgata 1"), og deler den opp i gatenavn og nummer for koordinatoppslag.

3.	Spesifiser takdetaljer:
•	Brukeren legger inn takområdet (f.eks. "50" m²) i "Areal"-feltet, som kun godtar sifre på grunn av KeyboardType.Number.
•	Brukeren legger inn takvinkelen (f.eks. "30" grader) i "Grader"-feltet, også begrenset til tall.
•	Brukeren velger takretningen (f.eks. "Sør") fra DropDownMenu, og velger blant alternativer som "Nord", "Øst", "Sør" eller "Vest" (definert i Direction.kt).

4.	Start beregning:
•	Brukeren trykker på knappen "Beregn estimert energi".
•	HomeScreen kaller HomeViewModel.onButtonClick, og sender adressen, takområdet, takgrader og retning.

5.	Hent koordinater:
•	HomeViewModel deler adressen (f.eks. "Storgata" og "1") og bruker CoordinateRepository.convertAddressToCoordinates for å spørre i GeoNorge API.
•	API-en returnerer breddegrad og lengdegrad (f.eks. [59.9139, 10.7522] for Oslo), håndtert av GeoNorgeDatasource.


6.	Hent data om solstråling:
•	Ved å bruke koordinatene henter RadiationRepository.getRadiationData historiske solstrålingsgjennomsnitt fra PVGIS (via RadiationDatasource).
•	Svaret inkluderer generelle, sommer- og vintergjennomsnitt (f.eks. [150,0, 200,0, 100,0] kWh/m²/måned), selv om bare det totale gjennomsnittet brukes for øyeblikket.

7.	Samle værdata:
•	WeatherRepository.getHistoricalAverageWeatherData spør Frost API for klimadata over det siste året (f.eks. "2024-03-17/2025-03-17").
•	Den returnerer gjennomsnitt for temperatur (f.eks. 5,0 °C), overskyet (f.eks. 4,0/8) og snødekning (f.eks. 1,0/4), hentet fra FrostDatasource.

8.	Beregn netto stråling:
•	Appen kaller calculateNetRadiation med strålingen (f.eks. 150,0 kWh/m²), temperatur, overskyet og snønivå.

                       Justeringer brukes:
•	Temperatur: 1,0 (innen -10°C til 25°C)
•	Skyet: 1 - (4/8 * 0,05) = 0,975
•	Snø: 1 - (1/4 * 0,25) = 0,9375
•	Netto stråling = 150,0 * 1,0 * 0,975 * 0,9375 ≈ 137,11 kWh/m².

9.	Estimer kraftproduksjon:
•	calculatePowerProduction tar netto stråling (137,11 kWh/m²), takareal (50 m²), vinkel (30°) og retning ("Sør").
•	Justeringer:
 Retning: 1,0 (Sør er optimal)
                         Vinkel: 1,0 (30° er i området 15°-40°)
                         Effekt = (137,11 * 50 * 1,0 * 1,0) * 0,2175 ≈ 1490,74 kWh/mnd.

10.	Vis resultater:
•	HomeUIState oppdaterer med innkommende solenergi (137 kWh/m², avrundet) og forventet PowerProduction (1491 kWh, avrundet).
•	Resultatkortet på hjemmeskjermen viser:
"Estimert netto innkommende solstråling: 137 kWh/m²"
                        "Estimert månedlig strømproduksjon: 1491 kWh."

Postbetingelser (Postconditions):
•	Brukeren ser estimert solenergi og kraftproduksjon for eiendommen sin.
•	Resultatene lagres i HomeViewModels tilstand, tilgjengelig for videre bruk eller visning.

Unntak:
•	Ugyldig inndata: Hvis adressen er feilformet eller takdata mangler/ugyldige, kan appen krasje eller returnere nuller (f.eks. UnknownHostException setter som standard koordinater til [0.0, 0.0]).
•	API-feil: Hvis GeoNorge, PVGIS eller Frost APIer er utilgjengelige, bruker appen reserveverdier (f.eks. 0,0), noe som fører til nullstilte resultater.
•	Ikke-norske tegn: Adressefeltet håndterer ikke "Æ", "Ø" eller "Å" riktig (bemerket feil i HomeScreen.kt).

Merknader:
•	Denne brukssaken gjenspeiler appens minimum viable product (MVP) i henhold til koden, og oppfyller de obligatoriske funksjonskravene: adresseoppslag, frostklimadata, strålingsberegning og effektestimering.
•	Valgfrie funksjoner (f.eks. kartvisning, kostnadsbesparelser, Enova-støtte) er ikke implementert ennå, men kan forlenge denne brukstilfellet.
![image](https://github.com/user-attachments/assets/9a721666-6e10-4e20-9bb7-427e861b01e7)

