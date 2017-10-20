package retriever.src;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.annotations.DeepCopyState;

import java.util.Arrays;
import java.util.List;

import static retriever.src.RetrieverDomain.VAR_DIRECTION;
import static retriever.src.RetrieverDomain.VAR_X;
import static retriever.src.RetrieverDomain.VAR_Y;

@DeepCopyState
public class RetrieverAgent implements ObjectInstance {
  public String name;
  
  public int x, y;
  
  public String curDirection;
  
//  public List<RetrievableObject> objectsInPocket;
  public List<String> objectsInPocket;
  
  public boolean imDone;
  
//  public RetrieverAgent(String name, int x, int y, String curDirection, List<RetrievableObject>
//		  objectsInPocket, boolean imDone) {
//	this.name = name;
//	this.x = x;
//	this.y = y;
//	this.curDirection = curDirection;
//	this.objectsInPocket = objectsInPocket;
//	this.imDone = imDone;
//  }
  
  
  public RetrieverAgent(String name, int x, int y, String curDirection,
						List<String> objectsInPocket, boolean imDone) {
	this.name = name;
	this.x = x;
	this.y = y;
	this.curDirection = curDirection;
	this.objectsInPocket = objectsInPocket;
	this.imDone = imDone;
  }
  
  public RetrieverAgent() {
  }
  
  public final static String VAR_OBJECTS_IN_POCKET = "objectsInPocket";
  
  public static final List<Object> keys = Arrays.asList(VAR_X, VAR_Y, VAR_DIRECTION,
														VAR_OBJECTS_IN_POCKET);
  
  public RetrieverAgent copyWithDirection(String direction) {
	return new RetrieverAgent(this.name, this.x, this.y, direction, this.objectsInPocket, this.imDone);
  }
  
  @Override
  public String className() {
	return RetrieverDomain.CLASS_AGENT;
  }
  
  @Override
  public String name() {
	return this.name;
  }
  
  @Override
  public ObjectInstance copyWithName(String s) {
	return new RetrieverAgent(s, x, y, curDirection, objectsInPocket, imDone);
  }
  
  @Override
  public List<Object> variableKeys() {
	return keys;
  }
  
  @Override
  public Object get(Object variableKey) {
	if (!(variableKey instanceof String)) {
	  throw new RuntimeException("Variable key given to Retriever Agent needs to be a String.");
	}
	String key = (String) variableKey;
	if (key.equals(VAR_X)) {
	  return x;
	}
	if (key.equals(VAR_Y)) {
	  return y;
	}
	if (key.equals(VAR_DIRECTION)) {
	  return curDirection;
	}
	if (key.equals(VAR_OBJECTS_IN_POCKET)) {
	  return this.objectsInPocket;
	}
	throw new RuntimeException("No variable key in Retriever Agent by the name: " + key);
  }
  
  @Override
  public State copy() {
	return new RetrieverAgent(name, x, y, curDirection, objectsInPocket, imDone);
  }
  
}
