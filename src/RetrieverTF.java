package retriever.src;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class RetrieverTF implements TerminalFunction {
  
  @Override
  public boolean isTerminal(State state) {
    RetrieverState s = (RetrieverState) state;
	return s.agent.imDone;
  }
}
