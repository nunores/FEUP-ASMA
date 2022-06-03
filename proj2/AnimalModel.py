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
            if (i == self.num_animals - 1):
                a = AnimalAgent(i, self, 1, True, False)
            elif (i == self.num_animals - 2):
                a = AnimalAgent(i, self, 1, False, True)
            else:
                a = AnimalAgent(i, self, 1, False, False)
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
            
        self.datacollector = DataCollector(model_reporters={"Num": getActiveAnimals, "Size": getMeanSize, "Fast": getNumFast, "Not Fast": getNumNotFast, "Perceptible": getNumPerceptible, "Not Perceptible": getNumNotPerceptible}, agent_reporters={"agent_energy": "energy"})
            
    def step(self):
        self.datacollector.collect(self)
        self.schedule.step()
        if (len(self.schedule.agents) == 0):
            self.removeAllFood()
            self.startNextGeneration()
        
    def addSurvival(self, parameterList):
        self.survivors.append(parameterList)
        
    def addReproducer(self, parameterList):
        self.reproducers.append(parameterList)
        
    def removeAllFood(self):
        for foodAgent in self.foodAgents:
            self.grid.remove_agent(foodAgent)
        self.foodAgents = []
        
    def startNextGeneration(self):
        for i in range(len(self.survivors)):
            a = AnimalAgent(i, self, self.survivors[i][0], self.survivors[i][1], self.survivors[i][2])
            self.schedule.add(a)
            if (bool(random.getrandbits(1))):
                x = self.random.randrange(0, self.grid.width)
                y = self.random.choice([0, self.grid.height - 1])
            else:
                y = self.random.randrange(0, self.grid.height)
                x = self.random.choice([0, self.grid.width - 1])
            self.grid.place_agent(a, (x, y))
        index = 0
        for i in range(len(self.survivors), len(self.survivors) + len(self.reproducers)):
            newSize = round(random.uniform(self.reproducers[index][0] - 0.2, self.reproducers[index][0] + 0.2), 2)
            a = AnimalAgent(i, self, newSize, self.reproducers[index][1], self.reproducers[index][2])
            self.schedule.add(a)
            if (bool(random.getrandbits(1))):
                x = self.random.randrange(0, self.grid.width)
                y = self.random.choice([0, self.grid.height - 1])
            else:
                y = self.random.randrange(0, self.grid.height)
                x = self.random.choice([0, self.grid.width - 1])
            self.grid.place_agent(a, (x, y))
            index += 1
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

def getMeanSize(model):
    sum = 0
    for agent in model.schedule.agents:
        sum += agent.size
    for agent in model.survivors:
        sum += agent[0]
    if ((len(model.schedule.agents) + len(model.survivors)) == 0):
        return 0
    return sum / (len(model.schedule.agents) + len(model.survivors))

def getNumFast(model):
    sum = 0
    for agent in model.schedule.agents:
        if (agent.fast):
            sum += 1
    for agent in model.survivors:
        if (agent[1]):
            sum += 1
    return sum

def getNumNotFast(model):
    sum = 0
    for agent in model.schedule.agents:
        if (not agent.fast):
            sum += 1
    for agent in model.survivors:
        if (not agent[1]):
            sum += 1
    return sum

def getNumPerceptible(model):
    sum = 0
    for agent in model.schedule.agents:
        if (agent.perceptible):
            sum += 1
    for agent in model.survivors:
        if (agent[2]):
            sum += 1
    return sum

def getNumNotPerceptible(model):
    sum = 0
    for agent in model.schedule.agents:
        if (not agent.perceptible):
            sum += 1
    for agent in model.survivors:
        if (not agent[2]):
            sum += 1
    return sum