package retriever.src;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.annotations.DeepCopyState;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.Arrays;
import java.util.List;

import static retriever.src.RetrieverDomain.VAR_X;
import static retriever.src.RetrieverDomain.VAR_Y;

@DeepCopyState
public class Shelf implements ObjectInstance {
  public String name;
  
  public int x, y;
  
//  public List<RetrievableObject> objects;
  List<String> keysToObjects;
  
//  public Shelf(String name, int x, int y, List<RetrievableObject> objects) {
//	this.name = name;
//	this.x = x;
//	this.y = y;
//	this.objects = objects;
//  }
  
  
  public Shelf(String name, int x, int y, List<String> keysToObjects) {
	this.name = name;
	this.x = x;
	this.y = y;
	this.keysToObjects = keysToObjects;
  }
  
  public Shelf() {
  }
  
  public final static String VAR_OBJECTS_ON_SHELF = "objectsOnShelf";
  
  public final static List<Object> key =
		  Arrays.asList(VAR_X, VAR_Y, VAR_OBJECTS_ON_SHELF);
  
  @Override
  public String className() {
	return RetrieverDomain.CLASS_SHELF;
  }
  
  @Override
  public String name() {
	return name;
  }
  
  @Override
  public ObjectInstance copyWithName(String s) {
	return new Shelf(s, x, y, keysToObjects);
  }
  
  @Override
  public List<Object> variableKeys() {
	return key;
  }
  
  @Override
  public Object get(Object variableKey) {
	if (!(variableKey instanceof String)) {
	  throw new RuntimeException("Variable Key for Shelf object must be a String.");
	}
	String key = (String) variableKey;
	if (key.equals(VAR_OBJECTS_ON_SHELF)) {
//	  return objects;
	  return keysToObjects;
	}
	if (key.equals(VAR_X)) {
	  return x;
	}
	if (key.equals(VAR_Y)) {
	  return y;
	}
	throw new RuntimeException("Variable Key not found for Shelf: " + key);
  }
  
  @Override
  public State copy() {
	return new Shelf(name, x, y, keysToObjects);
  }
  
}
