from mesa import Agent, Model
from mesa.time import RandomActivation
from mesa.space import MultiGrid
from mesa.datacollection import DataCollector
import matplotlib.pyplot as plt
from FoodAgent import FoodAgent

class AnimalAgent(Agent):
    
    def __init__(self, unique_id, model, size, fast, perceptible):
        super().__init__(unique_id, model)
        self.energy = 30
        self.previousPos = (-1, -1)
        self.amountEaten = 0
        self.size = size
        self.fast = fast
        self.perceptible = perceptible
        
    def step(self):
        self.move()
        if (self.canEat()):
            self.amountEaten += 1         
        self.energy -= 1 * self.size # first move consumes 1 energy
        
        if (self.energy < 0 or self.amountEaten == 2):
            self.nextGeneration()
            return
        
        if (self.fast):
            self.move()
            if (self.canEat()):
                self.amountEaten += 1         
            self.energy -= 1.2 * self.size # second move consumes more energy 1.2 > 1
        
        if (self.energy < 0 or self.amountEaten == 2):
            self.nextGeneration()
        
    def move(self):
        possible_steps = self.getSurroundings()
        if (self.previousPos in possible_steps):
            possible_steps.remove(self.previousPos)
            
        new_position = self.random.choice(possible_steps)
        if (self.perceptible):
            for position in possible_steps:
                flag = False
                cellmates = self.model.grid.get_cell_list_contents([position])
                for mate in cellmates:
                    if (isinstance(mate, FoodAgent)):
                        new_position = position
                        flag = True
                        break
                if (flag):
                    break
            
        self.previousPos = self.pos
        self.model.grid.move_agent(self, new_position)
        
    def canEat(self):
        cellmates = self.model.grid.get_cell_list_contents([self.pos])
        for agent in cellmates:
            if (isinstance(agent, FoodAgent)):
                self.model.grid.remove_agent(agent)
                self.model.foodAgents.remove(agent)
                return True
            elif (isinstance(agent, AnimalAgent) and (self.size > (agent.size * 1.2))):
                self.model.grid.remove_agent(agent)
                self.model.schedule.remove(agent)
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
    
    def nextGeneration(self):
        if (self.amountEaten == 1):
            self.model.addSurvival([self.size, self.fast, self.perceptible])
        elif (self.amountEaten == 2):
            self.model.addSurvival([self.size, self.fast, self.perceptible])
            self.model.addReproducer([self.size, self.fast, self.perceptible])
        self.model.grid.remove_agent(self)
        self.model.schedule.remove(self)
        
