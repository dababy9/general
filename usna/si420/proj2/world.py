import json



class World:

    def __init__(self, fName):
        with open(fName, 'r') as filehandle:
            self.dict = json.load(filehandle)

    