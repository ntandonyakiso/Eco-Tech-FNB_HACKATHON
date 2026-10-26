# Eco-Tech

TO be honest i have tried to develop this app/project alone and it was hard cause it was my first time doing it, but here is the outcome and it's currently not working but atleast i have something and this is what it was suppose to be: 

Eco-Tech is a community reporting application designed to help residents track, report, and manage local community issues across municipal wards in South Africa.

Why I Built It

I built Eco-Tech to make local government service delivery and issue tracking simpler and more accessible. By organizing reports by ward numbers and offering multi-language support, the goal is to bridge the communication gap between local communities and service managers using accessible web and mobile technology.

Key Features

Ward Management: Support for local wards (Wards 1–60, including areas like KwaMashu, Umlazi, and Inanda) with spatial coordinates and location tracking.

Multi-Language Support (i18n): Support for 5 local South African languages (English, isiZulu, isiXhosa, Afrikaans, and Sesotho).

Cross-Platform: Available as both a Web application and a mobile Android APK built with Capacitor.

External Integrations: Built-in infrastructure for Telegram bot notifications and EskomSePush load shedding updates.

Tech Stack

Backend: Java, Spring Boot, PostgreSQL (Supabase), Flyway Database Migrations

Frontend: React, Vite, Capacitor (for Android mobile wrapper)

Database: Supabase PostgreSQL

Project Setup

Environment Configuration

Create a .env file in the backend/ directory with the following variables:

DATABASE_URL=jdbc:postgresql://<your-supabase-db-url>:5432/postgres
DB_USERNAME=<your-db-username>
DB_PASSWORD=<your-db-password>

TELEGRAM_BOT_TOKEN=<your-telegram-bot-token>
ESKOM_SE_PUSH_API_KEY=<your-eskom-se-push-key>


Note: Telegram and EskomSePush keys are optional. If left blank, the app gracefully falls back to console logging.

Running the Application

1. Backend (Spring Boot)

Navigate to the backend folder and run:

./mvnw spring-boot:run


2. Frontend (React Web)

Navigate to the frontend folder and run:

npm install
npm run dev


The web app will run locally on port 5173.

3. Building the Android App

Navigate to the frontend/android folder and run:

./gradlew assembleDebug


The generated APK will be located at:
frontend/android/app/build/outputs/apk/debug/app-debug.apk

