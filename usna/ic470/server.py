from fastapi import FastAPI
from fastapi.responses import HTMLResponse
import uvicorn
import socket
from pydantic import BaseModel

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
    return {"message": "/search WORKED!"}

# define main method to run server
if __name__ == "__main__":
    hostname = socket.gethostname()
    ip_address = socket.gethostbyname(hostname)
    uvicorn.run(app, host=ip_address, port=8080)


