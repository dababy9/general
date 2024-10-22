from fastapi import FastAPI
from fastapi.responses import HTMLResponse
from pydantic import BaseModel
from spotipy.oauth2 import SpotifyOAuth
import spotipy 
import uvicorn
import socket
import asyncio

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
        uri = body.model_dump()['text']

        # add song to queue if user actually entered text
        if uri != "":
            sp.add_to_queue(uri, None)

        # wait briefly to make sure the song is actually added
        await asyncio.sleep(0.15)

        # retrieve queue (song name and artist name) and return it to user
        result = sp.queue()
        queue = []
        for item in result['queue']:
            queue.append({'song': item['name'], 'artists': [x['name'] for x in item['artists']]})
        queue.insert(0, {'song': result['currently_playing']['name'], 'artists': [x['name'] for x in result['currently_playing']['artists']]})
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

        # return spotify search to client
        result = sp.search(text, None)['tracks']['items']
        songList = []
        for item in result[:10]:
            songList.append({'song': item['name'], 'artists': [x['name'] for x in item['artists']], 'uri': item['uri']})
        return {"message": "OK", "songs": songList}
        
    # return error message if something went wrong
    except Exception as error:
        print(error)
        return {"message": "NOT OK"}



# define main method to run server
if __name__ == "__main__":
    hostname = socket.gethostname()
    ip_address = socket.gethostbyname(hostname)
    sp = spotipy.Spotify(auth_manager=SpotifyOAuth(client_id="02044de1754544a993a1e1f8dcefad93",
                                               client_secret="48bc19fb38e1452cb7c90b9a40eb9464",
                                               redirect_uri="http://localhost:9000",
                                               scope="user-library-read,user-read-recently-played,user-read-currently-playing,user-read-playback-state,user-modify-playback-state"))
    uvicorn.run(app, host="0.0.0.0", port=8888)
