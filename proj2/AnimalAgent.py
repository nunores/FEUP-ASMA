from mesa import Agent, Model
from mesa.time import RandomActivation
from mesa.space import MultiGrid
from mesa.datacollection import DataCollector
import matplotlib.pyplot as plt
from FoodAgent import FoodAgent

class AnimalAgent(Agent):
    
    def __init__(self, unique_id, model):
        super().__init__(unique_id, model)
        self.energy = 10
        self.previousPos = (-1, -1)
        
    def step(self):
        self.move()
        if(self.canEat()):
            print("Welelelelele")
            
        self.energy -= 1
        
        print("I'm " + str(self.unique_id) + " and I'm in " + str(self.pos))
        
    def move(self):
        possible_steps = self.getSurroundings()
        if(self.previousPos in possible_steps):
            possible_steps.remove(self.previousPos)
        new_position = self.random.choice(possible_steps)
        self.previousPos = self.pos
        self.model.grid.move_agent(self, new_position)
        
    def canEat(self):
        cellmates = self.model.grid.get_cell_list_contents([self.pos])
        for agent in cellmates:
            if(isinstance(agent, FoodAgent)):
                agent.eaten = True
                return True
        return False
    
    def getSurroundings(self):
        possibleSteps = []
        if (not self.pos[0] - 1 < 0):
            possibleSteps.append((self.pos[0] - 1, self.pos[1]))
        if (not self.pos[0] + 1 > self.model.width - 1):
            possibleSteps.append((self.pos[0] + 1, self.pos[1]))
        if (not self.pos[1] - 1 < 0):
            possibleSteps.append((self.pos[0], self.pos[1] - 1))
        if (not self.pos[1] + 1 > self.model.height - 1):
            possibleSteps.append((self.pos[0], self.pos[1] + 1))
        return possibleSteps
        
        
        
        
