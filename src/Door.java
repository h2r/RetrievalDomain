package retriever.src;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.annotations.DeepCopyState;

import java.util.Arrays;
import java.util.List;

import static retriever.src.RetrieverDomain.VAR_X;
import static retriever.src.RetrieverDomain.VAR_Y;

@DeepCopyState
public class Door implements ObjectInstance {
  public static final String VAR_LOCKABLE = "lockable";
  
  public String name;
  public int x, y;
  public boolean lockable;
  
  public Door(String name, int x, int y, boolean lockable) {
	this.name = name;
	this.x = x;
	this.y = y;
	this.lockable = lockable;
  }
  
  public Door() {
  }
  
  public final static List<Object> keys = Arrays.asList(VAR_LOCKABLE, VAR_X, VAR_Y);
  
  @Override
  public String className() {
	return RetrieverDomain.CLASS_DOOR;
  }
  
  @Override
  public String name() {
	return this.name;
  }
  
  @Override
  public ObjectInstance copyWithName(String s) {
	return new Door(s, x, y, lockable);
  }
  
  @Override
  public List<Object> variableKeys() {
	return keys;
  }
  
  @Override
  public Object get(Object variableKey) {
	if (!(variableKey instanceof String)) {
	  throw new RuntimeException("Door object variable key must be a string");
	}
	String key = (String) variableKey;
	if (key.equals(VAR_LOCKABLE)) {
	  return this.lockable;
	}
	if (key.equals(VAR_X)) {
	  return this.x;
	}
	if (key.equals(VAR_Y)) {
	  return this.y;
	}
	throw new RuntimeException("Unknown key for Door object: " + key);
  }
  
  @Override
  public State copy() {
	return new Door(name, x, y, lockable);
  }
}
