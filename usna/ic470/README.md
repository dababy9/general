# OnAux: A Class Jukebox!

### Overview:
OnAux is a program that is designed to allow a professor to have Spotify running in class while students add to the queue! The teacher will spin up the server and initially connect to it, which should prompt a Spotify login. This will be the only time a login should be required because of the Authorization Flow from the [Spotipy](https://github.com/spotipy-dev/spotipy) API. After that, the teacher just needs to provide the students with the IP address and port number that the application is running on, and the students should be able to connect and add songs!

### Server Setup:
The server can be run either as a Docker container or in a virtual environment - in either case, the application must have access to the libraries in `requirements.txt`. One important thing to note is that **the server will be unable to retrieve or add to the queue unless the teacher's Spotify account is currently playing music.** This is a limitation from the Spotify API, but shouldn't be a huge deal on the user's end - just make sure music is playing before students connect to the server.

#### Running Server in virtual environment:
`python3 -m venv myenv`
`source myenv/bin/activate`
`pip install requirements.txt`
`python3 server.py`

#### Running Server in Docker container:
`docker build -t app .`
`docker run -d -p 80:8888 app`