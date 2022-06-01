from mesa import Agent, Model
from mesa.time import RandomActivation
from mesa.space import MultiGrid
from mesa.datacollection import DataCollector
import matplotlib.pyplot as plt
from AnimalAgent import AnimalAgent
from FoodAgent import FoodAgent
import random

class AnimalModel(Model):
    
    def __init__(self, numAnimals, numFood, width, height):
        self.running = True
        self.width = width
        self.height = height
        self.num_animals = numAnimals
        self.num_food = numFood
        self.foodAgents = []
        self.schedule = RandomActivation(self)
        self.grid = MultiGrid(width, height, False)
        self.survivors = []
        self.reproducers = []
        
        for i in range(self.num_animals):
            a = AnimalAgent(i, self)
            self.schedule.add(a)
            if (bool(random.getrandbits(1))):
                x = self.random.randrange(0, self.grid.width)
                y = self.random.choice([0, self.grid.height - 1])
            else:
                y = self.random.randrange(0, self.grid.height)
                x = self.random.choice([0, self.grid.width - 1])
            self.grid.place_agent(a, (x, y))
            
        for i in range(self.num_food):
            f = FoodAgent(i, self)
            while(True):   
                x = self.random.randrange(1, self.grid.width - 1)
                y = self.random.randrange(1, self.grid.height - 1)
                cellmates = self.grid.get_cell_list_contents((x, y))
                repeat = False
                for agent in cellmates:
                    if(isinstance(agent, FoodAgent)):
                        repeat = True
                        break
                if(not repeat):
                    break
            
            self.grid.place_agent(f, (x, y))
            self.foodAgents.append(f)
            
        self.datacollector = DataCollector(model_reporters={"Num": getActiveAnimals}, agent_reporters={"agent_energy": "energy"})
            
    def step(self):
        self.datacollector.collect(self)
        self.schedule.step()
        if (len(self.schedule.agents) == 0):
            self.removeAllFood()
            self.startNextGeneration()
        
    def addSurvival(self):
        self.survivors.append([])
        
    def addReproducer(self):
        self.reproducers.append([])
        
    def removeAllFood(self):
        for foodAgent in self.foodAgents:
            self.grid.remove_agent(foodAgent)
        self.foodAgents = []
        
    def startNextGeneration(self):
        for i in range(len(self.survivors)):
            a = AnimalAgent(i, self)
            self.schedule.add(a)
            if (bool(random.getrandbits(1))):
                x = self.random.randrange(0, self.grid.width)
                y = self.random.choice([0, self.grid.height - 1])
            else:
                y = self.random.randrange(0, self.grid.height)
                x = self.random.choice([0, self.grid.width - 1])
            self.grid.place_agent(a, (x, y))
        for i in range(len(self.survivors), len(self.survivors) + len(self.reproducers)):
            a = AnimalAgent(i, self)
            self.schedule.add(a)
            if (bool(random.getrandbits(1))):
                x = self.random.randrange(0, self.grid.width)
                y = self.random.choice([0, self.grid.height - 1])
            else:
                y = self.random.randrange(0, self.grid.height)
                x = self.random.choice([0, self.grid.width - 1])
            self.grid.place_agent(a, (x, y))
        for i in range(self.num_food):
            f = FoodAgent(i, self)
            while(True):   
                x = self.random.randrange(1, self.grid.width - 1)
                y = self.random.randrange(1, self.grid.height - 1)
                cellmates = self.grid.get_cell_list_contents((x, y))
                repeat = False
                for agent in cellmates:
                    if(isinstance(agent, FoodAgent)):
                        repeat = True
                        break
                if(not repeat):
                    break
            self.grid.place_agent(f, (x, y))
            self.foodAgents.append(f)
        self.survivors = []
        self.reproducers = []
        

    def placeAgent(amount):
        for i in range(len(amount)):
            a = AnimalAgent(i, self)
            self.schedule.add(a)
            if (bool(random.getrandbits(1))):
                x = self.random.randrange(0, self.grid.width)
                y = self.random.choice([0, self.grid.height - 1])
            else:
                y = self.random.randrange(0, self.grid.height)
                x = self.random.choice([0, self.grid.width - 1])
            self.grid.place_agent(a, (x, y))

def getActiveAnimals(model):
    return len(model.schedule.agents) + len(model.survivors)