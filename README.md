# College Basketball Scoreboard
Name - Snail Hernandez
Computing ID - abs4hh

A real-time Android application that tracks NCAA Division 1 Basketball scores. 

## Key Features

* **Real-Time Data Sync:** Fetches live scores, game clocks, and period statuses (1st, 2nd, OT) via the NCAA API.
* **Offline Architecture:** Utilizes **Room Database** to cache scores. Once a date is loaded, it remains accessible even without an internet connection.
* **Accessibility Minded:** Uses Typography Weight  to signal winners, ensuring the app is fully functional for color-blind users who may struggle with Red/Green indicators.
* **UTC-Synced Calendar:** A custom DatePicker implementation that handles timezone offsets to ensure the correct games are loaded regardless of the user's location.



##  How to Run

1.  Clone this repository.
2.  Open the project in Android Studio.
3.  Sync Gradle and ensure you are using JDK 17+.
4.  Run on an emulator or physical device (API 26+).

---
