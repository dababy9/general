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
    song = body.dict()['text']
    return {"message": "/request WORKED!"}

# define search endpoint (user searching for song)
@app.post("/search")
async def search_song(body: Text):
    text = body.dict()['text']
    return {"message": "/search WORKED!", "songs": ["Careless Whisper", "505", "Flashing Lights", "Kiss of Life", "Love", "Don't", "Kill Bill", "It's A Wrap", "If I Ain't Got You", "Iris", "How to Save a Life", "Breakeven", "Stop And Stare", "Drinkin' Problem", "Neon Moon", "Tennessee Whiskey", "P.Y.T. (Pretty Young Thing)", "Isn't She Lovely", "Tunnel Vision", "Hotline Bling", "Topanga", "Sunday Morning"]}

# define main method to run server
if __name__ == "__main__":
    hostname = socket.gethostname()
    ip_address = socket.gethostbyname(hostname)
    # connected to Anuj's spotify account now 
    #sp = spotipy.Spotify(auth_manager=SpotifyOAuth(client_id="142b6b91ca9c4fff8492e3e562cfcc3d",
                                               #client_secret="6093e73128bf48f9bbcab1288cc531c0",
                                               #redirect_uri="http://localhost/8000",
                                               #scope="user-library-read,user-library-read,user-read-recently-played,user-read-currently-playing,user-read-playback-state,user-modify-playback-state"))
    
    #pprint(sp.me())
    uvicorn.run(app, host=ip_address, port=8080)
    


