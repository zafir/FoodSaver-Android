FoodSaver-Android
=================

Overview:
Food Saver is an Android app that helps you keep track of what you ordered at restaurants.

If you've ever gone to a restaurant, loved what you ate, returned months later only to forget what you
ordered the previous time, this app helps solve that problem. This happens to me all the time, so I made it
for myself. 

Technical details:
Last updated 8/20/2014. Compiles with Android KitKat (API 19). I think the minimum SDK is Jelly Bean (API 17). 

-Uses the Google Places API to retrieve restaurants near the user's current location. 
-Uses the Google Places API to power the app's search functionality. 
-Detects user location using the GPS (permission declared in manifest)
-Data is stored locally in a SQLite database
-JSON parsing to pull the relevant information from the API 
-Content provider created to make for easy data interaction - inserting, querying, and deleting data
-Uses custom cursor and array adapters, for the list of locally stored entires and API results, respectively
-Alternate two-pane tablet UI created, just for kicks
