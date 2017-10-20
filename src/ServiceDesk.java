package retriever.src;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.annotations.DeepCopyState;

import java.util.Arrays;
import java.util.List;

import static retriever.src.RetrieverDomain.VAR_X;
import static retriever.src.RetrieverDomain.VAR_Y;

@DeepCopyState
public class ServiceDesk implements ObjectInstance {
  public String name;
  
  public int x, y;
  
  public ServiceDesk(String name, int x, int y) {
	this.name = name;
	this.x = x;
	this.y = y;
  }
  
  public ServiceDesk() {
  }
  
  public final static List<Object> keys = Arrays.asList(VAR_X, VAR_Y);
  
  @Override
  public String className() {
	return RetrieverDomain.CLASS_SERVICE_DESK;
  }
  
  @Override
  public String name() {
	return name;
  }
  
  @Override
  public ObjectInstance copyWithName(String s) {
	return new ServiceDesk(s, x, y);
  }
  
  @Override
  public List<Object> variableKeys() {
	return keys;
  }
  
  @Override
  public Object get(Object variableKey) {
	if (!(variableKey instanceof String)) {
	  throw new RuntimeException("Variable Key needs to be a String for the Service Desk object.");
	}
	String key = (String) variableKey;
	if (key.equals(VAR_X)) {
	  return x;
	}
	if (key.equals(VAR_Y)) {
	  return y;
	}
	throw new RuntimeException("Variable Key not found for Service Desk object: " + key);
  }
  
  @Override public State copy() {
	return new ServiceDesk(name, x, y);
  }
}
