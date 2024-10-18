import subprocess
import requests
import time
import socket
<<<<<<< HEAD
import pytest
import json



# fixture to last throughout all tests
@pytest.fixture(scope="module")
def server():

    # Start the server and give some time for it to spin up
    process = subprocess.Popen(["python", "server.py", "8080"], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    time.sleep(1)

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
=======



def test_root_endpoint():
    # Start a server process we can test
    process = subprocess.Popen(["python", "server.py", "8080"], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    
    # The server might take a second to start
    time.sleep(1)
    
    try:
        hostname = socket.gethostname()
        ip_address = socket.gethostbyname(hostname)
        response = requests.get("http://" + ip_address + ":8080/")
        
        # The assert command throws an error if a False value is passed to it.
        # This is the command you will use to get your pytest work done.
        assert response.status_code == 200
    finally:
        # Terminate the server process after the test
        process.terminate()

def test_request_endpoint():
    # Start a server process we can test
    process = subprocess.Popen(["python", "server.py", "8080"], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    
    # The server might take a second to start
    time.sleep(1)
    
    try:
        hostname = socket.gethostname()
        ip_address = socket.gethostbyname(hostname)
        myobj = {'song_name' : 'test'}
        response = requests.post("http://" + ip_address + ":8080/request/", json=myobj)
        # The assert command throws an error if a False value is passed to it.
        # This is the command you will use to get your pytest work done.
        print(response.text)
        assert response.status_code == 200
    finally:
        # Terminate the server process after the test
        process.terminate()
>>>>>>> 8e92ee7788f86b60c5e6c16066a5d63cf0520301
