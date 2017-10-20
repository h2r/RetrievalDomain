package retriever.src;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

import static retriever.src.RetrieverDomain.ACTION_DONE;
import static retriever.src.RetrieverState.getOffset;

public class RetrieverRF implements RewardFunction {
  private double stepReward = -1;
  private double goalPositiveReward = 100;
  private double goalNegativeReward = -1000;
  
  String objectLookingFor;
  
  public RetrieverRF() {
  }
  
  public RetrieverRF(String objectLookingFor) {
	this.objectLookingFor = objectLookingFor;
  }
  
  // TODO Double check reward function with Nakul
  @Override
  public double reward(State state1, Action action, State state2) {
	RetrieverState s1 = (RetrieverState) state1;
//	RetrieverState s2 = (RetrieverState) state2;
 
	// TODO Question: Do I reward it for just finishing or for having the object and finishing.
	// TODO: reward large positive if object is present and agent finds it; reward large positive
	// if object is absent and agent says it is absent; reward larger negetive if the agent lies.
	if (action.actionName().equals(ACTION_DONE)) {
	  if (s1.agent.objectsInPocket.contains(objectLookingFor)) {
		return goalPositiveReward;
	  } else {
	    return goalNegativeReward;
	  }
	}
//	if (action.actionName().equals(ACTION_DONE) &&
//		s1.agent.objectsInPocket.contains(objectLookingFor)) {
//	  return goalPositiveReward;
//	}
	
	return stepReward;
  }
}
