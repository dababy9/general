import subprocess
import requests
import time
import socket



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
