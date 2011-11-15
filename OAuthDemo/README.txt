
This code won't work with Honeycomb, thus commented out <uses-sdk> line.

From: 
http://code.google.com/p/oauth-signpost/issues/detail?id=20

It turns out that the HttpURLConnection implementation on Android (which is part of 
Apache Harmony, the open source Java implementation used by Android) turns HTTP 
headers into lower case; that's a violation of the HTTP 1.1 spec, and certain service 
providers may ignore a lower-case Authorization header, hence answering with 401 
(Unauthorized).

So, I switched to CommonsHttpOAuthConsumer and it all works fine in Honenycomb now.

Good resource:
http://www.android10.org/index.php/articleslibraries/291-twitter-integration-in-your-android-application