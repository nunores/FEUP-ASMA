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
        
    def step(self):
        self.move()
        if(self.canEat()):
            print("Welelelelele")
            
        self.energy -= 1
        
        print("I'm " + str(self.unique_id) + " and I'm in " + str(self.pos))
        
    def move(self):
        possible_steps = self.model.grid.get_neighborhood(
            self.pos,
            moore=False,
            include_center=False)
        new_position = self.random.choice(possible_steps)
        self.model.grid.move_agent(self, new_position)
        
    def canEat(self):
        cellmates = self.model.grid.get_cell_list_contents([self.pos])
        for agent in cellmates:
            if(isinstance(agent, FoodAgent)):
                agent.eaten = True
                return True
        return False
        
        
        
        
