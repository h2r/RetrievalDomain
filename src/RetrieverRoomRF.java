package retriever.src;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

public class RetrieverRoomRF implements RewardFunction {
  private String roomName;
//  Room goalRoom;
  
//  public RetrieverRoomRF(Room goalRoom) {
//	this.goalRoom = goalRoom;
//  }
  
  public RetrieverRoomRF(String roomName) {
    this.roomName = roomName;
  }
  
  public RetrieverRoomRF() {
  }
  
  @Override
  public double reward(State state1, Action action, State state2) {
//	RetrieverState s1 = (RetrieverState) state1;
	RetrieverState s2 = (RetrieverState) state2;
//	if (s2.whichRoom(s2.agent.x, s2.agent.y).equals(goalRoom)) {
	if (s2.whichRoom(s2.agent.x, s2.agent.y).name().equals(roomName)) {
	  return 1.0;
	}
	return -1.0;
  }
}
