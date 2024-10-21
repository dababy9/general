from fastapi import FastAPI
from fastapi.responses import HTMLResponse
import uvicorn
import socket
from pydantic import BaseModel

import spotipy 
from spotipy.oauth2 import SpotifyOAuth
import spotipy.util as util
from pprint import pprint
import random

# create app
app = FastAPI()

# define pydantic object for endpoint POST body inputs
class Text(BaseModel):
    text: str

# define root endpoint (serve html page)
@app.get("/")
async def home():
    f = open("index.html")
    return HTMLResponse(content=f.read(), status_code=200)

# define request endpoint (user requesting to add song to queue)
@app.post("/request")
async def store_song(body: Text):
    try:

        # retrieve the song name
        song = body.model_dump()['text']

        # add song to queue if user actually entered text
        if song != "":
            sp.add_to_queue(sp.search(song)['tracks']['items'][1]['uri'], None)

        # retrieve queue (song name and artist name) and return it to user
        print(sp.queue()['queue'][0].keys())
        queue = [{key: x[key] for key in ['name', 'artist']} for x in sp.queue()['queue']]
        queue.insert(0, {key: sp.queue()['currently_playing'][key] for key in ['name', 'artist']})
        return {"message": "OK", "queue": queue}
    
    # return error message if something went wrong
    except Exception as error:
        print(error)
        return {"message": "NOT OK"}

# define search endpoint (user searching for song)
@app.post("/search")
async def search_song(body: Text):
    try:

        # get current text in user search box
        text = body.model_dump()['text']

        # return search of spotify
        songs = [x['name'] for x in sp.search(text, None)['tracks']['items'][:10]]
        print(songs)
        return {"message": "OK", "songs": songs}
    except Exception as error:
        print(error)
        return {"message": "NOT OK"}

# define main method to run server
if __name__ == "__main__":
    hostname = socket.gethostname()
    ip_address = socket.gethostbyname(hostname)
    # connected to Anuj's spotify account now 
    sp = spotipy.Spotify(auth_manager=SpotifyOAuth(client_id="02044de1754544a993a1e1f8dcefad93",
                                               client_secret="48bc19fb38e1452cb7c90b9a40eb9464",
                                               redirect_uri="http://localhost:9000",
                                               scope="user-library-read,user-read-recently-played,user-read-currently-playing,user-read-playback-state,user-modify-playback-state"))
    print("HERE")
    pprint(sp.me())
    uvicorn.run(app, host=ip_address, port=8080)
    