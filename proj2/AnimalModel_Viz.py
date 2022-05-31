from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer
from AnimalAgent import AnimalAgent
from AnimalModel import AnimalModel
from FoodAgent import FoodAgent

def agent_portrayal(agent):
    portrayal = {}
    if(isinstance(agent, AnimalAgent)):  
        portrayal = {"Shape": "circle",
                    "Color": "red",
                    "Filled": "true",
                    "Layer": 0,
                    "r": 0.5}
    elif(isinstance(agent, FoodAgent)):
        if(agent.eaten == False):
            portrayal = {"Shape": "circle",
                    "Color": "blue",
                    "Filled": "true",
                    "Layer": 1,
                    "r": 0.3}
        
    return portrayal

grid = CanvasGrid(agent_portrayal, 30, 30, 500, 500)

server = ModularServer(AnimalModel,
                       [grid],
                       "Animal Model",
                       {"numAnimals":30, "numFood":15, "width":30, "height":30})
server.port = 8521 # The default
server.launch()


