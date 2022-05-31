from mesa import Agent, Model
from mesa.time import RandomActivation
from mesa.space import MultiGrid
from mesa.datacollection import DataCollector
import matplotlib.pyplot as plt

class AnimalAgent(Agent):
    
    def __init__(self, unique_id, model):
        super().__init__(unique_id, model)
        self.energy = 10
        
    def step(self):
        self.move()
        self.energy -= 1
        print("I'm " + str(self.unique_id) + " and I'm in " + str(self.pos))
        
    def move(self):
        possible_steps = self.model.grid.get_neighborhood(
            self.pos,
            moore=False,
            include_center=False)
        new_position = self.random.choice(possible_steps)
        self.model.grid.move_agent(self, new_position)


class AnimalModel(Model):
    
    def __init__(self, N, width, height):
        self.num_agents = N
        self.schedule = RandomActivation(self)
        self.grid = MultiGrid(width, height, False)
        for i in range(self.num_agents):
            a = AnimalAgent(i, self)
            self.schedule.add(a)
            
            x = self.random.randrange(self.grid.width)
            y = self.random.randrange(self.grid.height)
            self.grid.place_agent(a, (x, y))
            
        self.datacollector = DataCollector(agent_reporters={"agent_energy": "energy"})
            
    def step(self):
        self.datacollector.collect(self)
        self.schedule.step()

model = AnimalModel(20, 10, 10)

for i in range(20):
    model.step()
    
agent_energy = model.datacollector.get_agent_vars_dataframe()
agent_energy = agent_energy.xs(5, level="AgentID")
agent_energy.plot()

plt.show()




