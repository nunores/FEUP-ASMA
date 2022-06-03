from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer
from AnimalAgent import AnimalAgent
from AnimalModel import AnimalModel
from FoodAgent import FoodAgent
from mesa.visualization.modules import ChartModule

def agent_portrayal(agent):
    portrayal = {}
    if(isinstance(agent, AnimalAgent)):  
        portrayal = {"Shape": "circle",
                    "Color": "red",
                    "Filled": "true",
                    "Layer": 0,
                    "r": agent.size/5}
    elif(isinstance(agent, FoodAgent)):
        if(agent.eaten == False):
            portrayal = {"Shape": "circle",
                    "Color": "blue",
                    "Filled": "true",
                    "Layer": 1,
                    "r": 0.3}
        
    return portrayal

grid = CanvasGrid(agent_portrayal, 30, 30, 500, 500)

chartNum = ChartModule([{"Label": "Num",
                      "Color": "Black"}],
                    data_collector_name='datacollector')

chartSize = ChartModule([{"Label": "Size",
                      "Color": "Black"}],
                    data_collector_name='datacollector')

server = ModularServer(AnimalModel,
                       [grid, chartNum, chartSize],
                       "Animal Model",
                       {"numAnimals":30, "numFood":100, "width":30, "height":30})
server.port = 8521 # The default
server.launch()


