# My_CAR car service app

## Overview 
   This project is a mobile android app for following the car maintenance like
the service book, and collecting data to predict the next maintenance from previous
failures.

   The app has two parts: an android mobile app and an online server, the mobile
app for manage the user cars maintenance, and the server for authenticating the users
and syncing the local data, and more important
for the application of the algorithms of machine learning.

   MyCAR app uses the concept of offline sync, it means that the mobile app works in local (offline) with a local database that saves all local data (cars, service centers,
maintenances…).

## Features 
   * User authentication and email verification.
   * Manage cars maintenance (verifications and reparations) and service centers.
   * Notification of predictive car maintenance based on previous failure.
   * Offline data syncronisation.
   * Admin dashboard.
   
## Requirements and tools
   * Android Studio (mobile app development)
   * Django (server and RestAPI development)
   * MySQL Workbansh (server database)
   * SQLite (mobile app local database)
   * Firebase (Firebase cloud messaging for recommendations notification)
   * Postman (API test)

