# Popular-Movies-Stage2
This is the "Popular Movies" Android application. It shows the popular movies using tMDb API. The movies can be sorted on the basis of rating, favorites and popularity. Additionally, user can get more data about the movie such as average vote, release date, movie description, etc. Trailers or any other video and user reviews about the movie can also be seen in Movie details activity. 

The app also uses Room Persistene library to store user marked favorites movies.

This source code is the solution of Udacity's Popular Movies Stage1 project. People who want to run this app, need to generate API key from the link https://www.themoviedb.org/account/signup.

It stores user marked favorite movies in Room DB, uses Retrofit2 to fetch movies from the remote server via API.
The code also maintains to list of scroll of Recycler View upon device rotation of other configuration changes.
