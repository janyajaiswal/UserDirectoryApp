User Directory (Offline-First Android App)

A simple offline-capable User Directory app built with Kotlin, Jetpack Compose, Room Database, Retrofit, and MVVM architecture.

This project demonstrates how to build a fully offline-first Android application that:

Fetches user data from a public API

Caches it locally using Room

Always displays data from the database

Continues to work with zero internet

Supports fast local search using SQL

Updates UI automatically using Kotlin Flow and StateFlow

📱 Features
✔️ Fetch Users from API (GET only)

The app retrieves users from:

https://jsonplaceholder.typicode.com/users


Values fetched:

id

name

email

phone

No POST/PUT/DELETE—strictly read-only.

✔️ Local Persistence with Room (Single Source of Truth)

All API data is stored in a Room Database using:

@Insert(onConflict = OnConflictStrategy.REPLACE)


Room is the only source of data shown in the UI.

✔️ Offline-First Architecture

On app launch:

UI immediately loads data from Room

App tries to refresh data from the API

If successful → Room updates → UI updates automatically

If API fails (offline) → cached data continues to show

The app never leaves the user with a blank screen.

✔️ Local Search (No API calls)

Searching happens 100% locally using SQL:

SELECT * FROM users
WHERE name LIKE '%query%' OR email LIKE '%query%'


Fast, offline, and efficient.

✔️ Smooth, Lag-Free Search Bar

The UI uses:

debounce(250) to avoid spamming queries

Compose recomposition isolation to prevent keyboard lag

A stable LocalQuery state to keep cursor stable


🛠 Tech Stack

Kotlin

Jetpack Compose

Room Database

Retrofit

Kotlin Coroutines

Flow + StateFlow

MVVM Architecture

🎨 Screenshots

<img width="1080" height="2400" alt="Screenshot_20251114_213723" src="https://github.com/user-attachments/assets/b381d3a1-b318-4458-8b27-36be72a3e112" />
<img width="1080" height="2400" alt="Screenshot_20251114_210459" src="https://github.com/user-attachments/assets/b5f52eda-6142-421d-8292-b129a5b3ceed" />
<img width="1080" height="2400" alt="Screenshot_20251114_210336" src="https://github.com/user-attachments/assets/e0ff33d6-610c-49c7-a692-94b4414707fe" />
<img width="1080" height="2400" alt="Screenshot_20251114_210255" src="https://github.com/user-attachments/assets/3aed2dfc-edf7-4856-8395-9b35b8659fbe" />
<img width="1080" height="2400" alt="Screenshot_20251114_210145" src="https://github.com/user-attachments/assets/4eee5d37-3e25-445c-8fa8-a434e190ac88" />
<img width="1080" height="2400" alt="Screenshot_20251114_210025" src="https://github.com/user-attachments/assets/344da87c-3d3a-4bf3-8f45-6fced6b2770c" />

