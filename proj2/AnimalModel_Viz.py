from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer
from AnimalAgent import AnimalAgent
from AnimalModel import AnimalModel
from FoodAgent import FoodAgent
from mesa.visualization.modules import ChartModule

def agent_portrayal(agent):
    portrayal = {}
    if(isinstance(agent, AnimalAgent)):  
        if (agent.fast):  
            portrayal = {"Shape": "circle",
                        "Color": "red",
                        "Filled": "false",
                        "Layer": 0,
                        "r": agent.size/3}
        else:
            portrayal = {"Shape": "circle",
                        "Color": "green",
                        "Filled": "false",
                        "Layer": 0,
                        "r": agent.size/3}
        if (agent.perceptible):
            portrayal["Shape"] = "rect"
            portrayal["w"] = (agent.size/3)
            portrayal["h"] = (agent.size/3)
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

chartFast = ChartModule([{"Label": "Fast",
                      "Color": "Red"}, {"Label": "Not Fast",
                      "Color": "Green"}],
                    data_collector_name='datacollector')

chartPerceptible = ChartModule([{"Label": "Perceptible",
                      "Color": "Red"}, {"Label": "Not Perceptible",
                      "Color": "Green"}],
                    data_collector_name='datacollector')

server = ModularServer(AnimalModel,
                       [grid, chartNum, chartSize, chartFast, chartPerceptible],
                       "Animal Model",
                       {"numAnimals":30, "numFood":100, "width":30, "height":30})
server.port = 8521 # The default
server.launch()


