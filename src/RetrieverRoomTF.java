package retriever.src;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class RetrieverRoomTF implements TerminalFunction {
  Room goalRoom;
  
  public RetrieverRoomTF(Room goalRoom) {
	this.goalRoom = goalRoom;
  }
  
  public RetrieverRoomTF() {
  }
  
  @Override
  public boolean isTerminal(State state) {
	RetrieverState s = (RetrieverState) state;
	if (s.whichRoom(s.agent.x, s.agent.y).equals(this.goalRoom)) {
	  return true;
	}
	return false;
  }
}
