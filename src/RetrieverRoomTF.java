package retriever.src;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class RetrieverRoomTF implements TerminalFunction {
  private String roomName;
//  Room goalRoom;
  
//  public RetrieverRoomTF(Room goalRoom) {
//	this.goalRoom = goalRoom;
//  }
  
  public RetrieverRoomTF(String roomName) {
    this.roomName = roomName;
  }
  public RetrieverRoomTF() {
  }
  
  @Override
  public boolean isTerminal(State state) {
	RetrieverState s = (RetrieverState) state;
//	if (s.whichRoom(s.agent.x, s.agent.y).equals(this.goalRoom)) {
	if (s.whichRoom(s.agent.x, s.agent.y).name().equals(roomName)) {
	  return true;
	}
	return false;
  }
}
