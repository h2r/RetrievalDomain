package retriever.src;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.annotations.DeepCopyState;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import java.util.Arrays;
import java.util.List;

@DeepCopyState
public class RetrievableObject implements ObjectInstance {
  public String name;
  
  public String type, color, location;
  public int present;
  
  public final static int UNINITIALIZED = -1;
  public final static int PRESENT = 1;
  public final static int NOT_PRESENT = 0;
  
  public RetrievableObject(String name, String type, String color, String location,
							int present) {
	this.name = name;
	this.type = type;
	this.color = color;
	this.location = location;
	this.present = present;
  }
  
  public RetrievableObject() {
  }
  
  public final static String VAR_TYPE = "type";
  public final static String VAR_COLOR = "color";
  public final static String VAR_LOCATION = "location";
  public final static String VAR_PRESENT = "present";
  
  public final static List<Object> keys =
		  Arrays.asList(VAR_TYPE, VAR_COLOR, VAR_LOCATION, VAR_PRESENT);
  
  @Override
  public String className() {
	return RetrieverDomain.CLASS_RETRIEVABLE_OBJECTS;
  }
  
  @Override
  public String name() {
	return this.name;
  }
  
  @Override
  public ObjectInstance copyWithName(String s) {
	return new RetrievableObject(s, type, color, location, present);
  }
  
  @Override
  public List<Object> variableKeys() {
	return keys;
  }
  
  @Override
  public Object get(Object variableKey) {
	if (!(variableKey instanceof String)) {
	  throw new RuntimeException("Variable key needs to be a String for Retrievable Object.");
	}
	String key = (String) variableKey;
	if (key.equals(VAR_COLOR)) {
	  return color;
	}
	if (key.equals(VAR_LOCATION)) {
	  return location;
	}
	if (key.equals(VAR_PRESENT)) {
	  return present;
	}
	if (key.equals(VAR_TYPE)) {
	  return type;
	}
	throw new RuntimeException("Retrievable Object variable key does not exist: " + key);
  }
  
  @Override
  public State copy() {
	return null;
  }
  
//  public void set(Object variableKey) {
//	if (!(variableKey instanceof String)) {
//	  throw new RuntimeException("Variable key not ")
//	}
//  }
}
