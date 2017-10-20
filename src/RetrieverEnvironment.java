package retriever.src;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.oo.OOSADomain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static retriever.src.RetrievableObject.NOT_PRESENT;

public class RetrieverEnvironment extends SimulatedEnvironment {
  
  // Mapping from names of shelves to names of the objects they contain
  Map<String, List<String>> shelfMap = new HashMap<>();
  
  public RetrieverEnvironment(OOSADomain domain, State initialState) {
	super(domain, initialState);
  }
  
  public void addShelf(String shelfName, List<String> objectsInShelf) {
	this.shelfMap.put(shelfName, objectsInShelf);
  }
  
  @Override
  public EnvironmentOutcome executeAction(Action action) {
  
	Action a = action.copy();
 
	EnvironmentOutcome eo;
 
	if (this.allowActionFromTerminalStates || !this.isInTerminalState()) {
	  do {
		eo = model.sample(this.curState, a);
	  } while (checkObjects((RetrieverState) eo.op));
	} else {
	  eo = new EnvironmentOutcome(this.curState, a, this.curState.copy(), 0., true);
	}
	
	this.lastReward = eo.r;
	this.terminated = eo.terminated;
	this.curState = eo.op;
	
	return eo;
	
  }
  
  private boolean checkObjects(RetrieverState curState) {
	Set<String> keys = this.shelfMap.keySet();
	int outerCounter = 0;
	for (String key : keys) {
	  List<String> actualObjects = this.shelfMap.get(key);
	  Shelf curShelf = curState.getShelf(key);
	  List<String> believedObjects = curShelf.keysToObjects;
	  int counter = 0;
	  for (String realObject : actualObjects) {
		for (String hypotheticalObject : believedObjects) {
		  if (realObject.equals(hypotheticalObject)) {
		  	// TODO WEREN'T THERE COMMENTS HERE BEFORE?
			if (curState.presentObject(realObject) == NOT_PRESENT) {
			  return false;
			} else {
			  counter++;
			}
			
		  }
		}
	  }
	  if (counter == actualObjects.size()) {
	    outerCounter++;
	  } else {
	    return false;
	  }
	}
	return (outerCounter == keys.size());
  }
  
  
}
