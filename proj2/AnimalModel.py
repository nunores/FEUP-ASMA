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
        self.schedule = RandomActivation(self)
        self.grid = MultiGrid(width, height, False)
        
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
            
        self.datacollector = DataCollector(model_reporters={"Num": "num_animals"}, agent_reporters={"agent_energy": "energy"})
            
    def step(self):
        self.datacollector.collect(self)
        print(self.schedule.agents)
        self.schedule.step()