package retriever.src;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.annotations.DeepCopyState;

import java.util.Arrays;
import java.util.List;

@DeepCopyState
public class Room implements ObjectInstance {
  
  public String name;
  
  public int top, bottom, right, left;
  
  String color;
  
  public Room(String name, int top, int bottom, int right, int left, String color) {
	this.name = name;
	this.top = top;
	this.bottom = bottom;
	this.right = right;
	this.left = left;
	this.color = color;
  }
  
  public Room() {
  }
  
  public static final String VAR_TOP = "top";
  public static final String VAR_BOTTOM = "bottom";
  public static final String VAR_RIGHT = "right";
  public static final String VAR_LEFT = "left";
  
  public static final List<Object> keys = Arrays.asList(VAR_BOTTOM, VAR_LEFT, VAR_RIGHT, VAR_TOP);
  
  @Override
  public String className() {
	return RetrieverDomain.CLASS_ROOM;
  }
  
  @Override
  public String name() {
	return this.name;
  }
  
  @Override
  public ObjectInstance copyWithName(String s) {
	return new Room(s, top, bottom, right, left, color);
  }
  
  @Override
  public List<Object> variableKeys() {
	return keys;
  }
  
  @Override
  public Object get(Object variableKey) {
	if (!(variableKey instanceof String)) {
	  throw new RuntimeException("Room variable key must be a string");
	}
	String key = (String) variableKey;
	if (key.equals(VAR_TOP)) {
	  return this.top;
	}
	if (key.equals(VAR_BOTTOM)) {
	  return this.bottom;
	}
	if (key.equals(VAR_LEFT)) {
	  return this.left;
	}
	if (key.equals(VAR_RIGHT)) {
	  return this.right;
	}
	throw new RuntimeException("Variable key not found for Object door: " + key);
  }
  
  @Override
  public State copy() {
	return new Room(name, top, bottom, right, left, color);
  }
  
  @Override
  public boolean equals(Object o) {
	if (!(o instanceof Room)) {
	  return false;
	}
	Room that = (Room) o;
	return (that.name().equals(this.name()) &&
			that.top == this.top && that.bottom == this.bottom && that.right == this.right &&
			that.left == this.left && this.color.equals(that.color));
  }
  
}
