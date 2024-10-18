# using the Spotipy library 
# 1. authenticate (log in)
# 2. constent data stream (reading of it)
# 3. search for songs and retrieve URLs
# 4. add songs to queue
# 5. be able to return the queue
# 6. delete from queue
# functions:
# queue() -> gets the current user's queue 
# add_to_queue() -> takes song URL and adds it to the queue

import spotipy 
from spotipy.oauth2 import SpotifyOAuth
import spotipy.util as util
from pprint import pprint
import random

     

#"log in"


# connected to Anuj's spotify account now 
sp = spotipy.Spotify(auth_manager=SpotifyOAuth(client_id="142b6b91ca9c4fff8492e3e562cfcc3d",
                                               client_secret="6093e73128bf48f9bbcab1288cc531c0",
                                               redirect_uri="http://localhost/8000",
                                               scope="user-library-read,user-library-read,user-read-recently-played,user-read-currently-playing,user-read-playback-state,user-modify-playback-state"))
pprint(sp.me())
#track_uri = "spotify:track:YOUR_TRACK_URI"
#sp.add_to_queue(track_uri)
#sp = spotipy.Spotify(auth_manager=SpotifyOAuth())
'''
scope = 'user-library-read'
def show_tracks(results):
    for item in results['items']:
        track = item['track']
        print("%32.32s %s" % (track['artists'][0]['name'], track['name']))

results = sp.current_user_saved_tracks()
show_tracks(results)

while results['next']:
    results = sp.next(results)
    show_tracks(results)
    '''
users_queue = sp.queue()

currently_playing_track_or_episode = users_queue['currently_playing']
print(f"Currently playing: {currently_playing_track_or_episode['name']}")

# resume from here
#track_uri = 'spotify:track:6THqWFqcUAAB7At66622Wr'
#sp.add_to_queue(track_uri)



for track_or_episode in users_queue['queue']:
    print(f"Queued: {track_or_episode['name']}")

sp.current_playback()
devices = sp.devices()
print(devices)

def search_song():
    print("what song?")
    name = input()
    result = sp.search(name)

    # first thing to pop up 
    print(result['tracks']['items'][1]['name'])
    uri = result['tracks']['items'][1]['uri']
    return uri
    


#def add_to_queue(uri):


#def rem_from_queue(uri):


# main
#while(true):
    # read in inputs

    #If input === 
uri_song = search_song()
print(uri_song)
sp.add_to_queue(uri_song, None)

