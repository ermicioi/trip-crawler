# About
The _Trip Crawler_ application is capable to search accommodation offers on the various site based on you request. 
The request can include a period with max length of stay. 
The result will be saved into CSV file from which you can create varios Pivot Tables in order to find the best 
accommodation which suites your needs.

# Usage
Following command will search for accommodation
* between 1st July 2020 and 31st August 2020
* in Prague
* For max 7 days
* One room
* For 2 adults
* And a child of 2 years and another of 3 years old

`java -jar trip-crawer-<version>.jar Prague 2020-07-01 2020-08-31 7 1 2 2 3`

The schema for command is:

`java -jar trip-crawer-<version>.jar <location> <start period> <end period> <length of stay> <rooms> <adults> <list of  children ages separated by space>`

# Releases
No release

# Parties
The application is scanning following providers:
* Booking.com