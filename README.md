Overview: Food Saver is an Android app that helps you keep track of what you ordered at restaurants.

If you've ever gone to a restaurant, loved what you ate, returned months later only to forget what you ordered the previous time, this app helps solve that problem. This happens to me all the time, so I made it for myself.

The basic flow is as follows:
-Home screen presents two large buttons, "My Food" and "Save Food"
-Choosing "Save Food" launches an activity showing nearby restaurants using the user's GPS coordinates (powered by Google Places API)
-If the user's restaurant is not in the list, they have the option to search for a restaurant in the Action Bar (also powered by Google Places API)
-Upon choosing a restaurant, a new activity is launched allowing the user to enter details including what they ate, a RatingBar with 5 stars, and comments. Then they Save their entry
and are returned to the home screen automatically
-Choosing "My Food" launches an activity displaying the names of all the restaurants where the user saved entries
-Tapping on a restaurant in the list launches an activity showing the details of that item. Now you won't forget what you ordered!

Technical details: Last updated 8/20/2014. Compiles with Android KitKat (API 19). I think the minimum SDK is Jelly Bean (API 17).

-Uses the Google Places API to retrieve restaurants near the user's current location. 
-Uses the Google Places API to power the app's search functionality. 
-Detects user location using the GPS (permission declared in manifest) 
-Data is stored locally in a SQLite database 
-JSON parsing to pull the relevant information from the API 
-Content provider created to make for easy data interaction - inserting, querying, and deleting data 
-Uses custom cursor and array adapters, for the list of locally stored entires and API results, respectively 
-Alternate two-pane tablet UI created for MainActivity (home screen of the app)
-JUnit tests implemented to test the database and content provider
