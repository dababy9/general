import subprocess
import requests
import time
import socket
import pytest
import json



# fixture to last throughout all tests
@pytest.fixture(scope="module")
def server():

    # Start the server and give some time for it to spin up
    process = subprocess.Popen(["python", "server.py", "8080"], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    time.sleep(1)

    # create spotipy access key thing
    # spin up a fake device (or real one)
    # add a couple songs to the queue

    # wait until all tests are done, then terminate server
    yield process
    process.terminate()
    process.wait()



# test to make sure root endpoint serves html page
def test_root_endpoint(server):
    
    # get root endpoint
    response = requests.get("http://" + socket.gethostbyname(socket.gethostname()) + ":8080/")

    # check the status code is 200 (OK)
    assert response.status_code == 200

    # check if response is correct html page (checking title)
    assert "<title>Class Jukebox</title>" in response.text



# test that request endpoint processes POST request and responds with good response
def test_request_endpoint(server):

    # send POST request to 'request' endpoint
    response = requests.post("http://" + socket.gethostbyname(socket.gethostname()) + ":8080/request", json={'text':'test'})
    
    # check that the response code is good
    assert response.status_code == 200

    # check that the response is expected JSON object
    assert 'message' in json.loads(response.text)



# test that search endpoint processes POST request and responds with good response
def test_search_endpoint(server):

    # send POST request to 'search' endpoint
    response = requests.post("http://" + socket.gethostbyname(socket.gethostname()) + ":8080/search", json={'text':'test'})
    
    # check that the response code is good
    assert response.status_code == 200

    # check that the response is expected JSON object
    assert 'message' in json.loads(response.text)
