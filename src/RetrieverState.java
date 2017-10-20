package retriever.src;

import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.StateUtilities;
import burlap.mdp.core.state.annotations.ShallowCopyState;
import gridWorldExample.src.ExampleGridWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static retriever.src.RetrieverDomain.*;

@ShallowCopyState
public class RetrieverState implements MutableOOState {
  public RetrieverAgent agent;
  
  ///////////////////////////////////////////
  // TODO: PROBABLY BEST TO USE HASHTABLES INSTEAD OF LISTS
  public List<Door> doors;
  public List<Room> rooms;
  public ServiceDesk serviceDesk;
  // TODO Nakul wants shelves not to contain Retrievable Objects but Strings of the names of the
  // TODO objects.  And the objects contain the string of the name of its shelf.
  public List<Shelf> shelves;
  // TODO Nakul wants the main RetrieverState to contain a list of the RetrievableObjects themselves
//  public RetrievableObject objectLookingFor;
  public List<RetrievableObject> objects;
  ///////////////////////////////////////////
  
  
  public RetrieverState() {
  }
  
  public static int[] getOffset(String dir) {
	if (dir.equals(ACTION_EAST)) {
	  return new int[]{1, 0};
	} else if (dir.equals(ACTION_SOUTH)) {
	  return new int[]{0, 1};
	} else if (dir.equals(ACTION_WEST)) {
	  return new int[]{-1, 0};
	} else if (dir.equals(ACTION_NORTH)) {
	  return new int[]{0, -1};
	} else {
	  throw new RuntimeException("Given string is not a direction: " + dir);
	}
  }
  
//  public RetrieverState(RetrieverAgent agent) {
//    this.agent = agent;
//  }
  
//  public RetrieverState(RetrieverAgent agent, List<Door> doors,
//						List<Room> rooms,
//						ServiceDesk serviceDesk,
//						List<Shelf> shelves
//					   ) {
//	this.agent = agent;
//	this.doors = doors;
//	this.rooms = rooms;
//	this.serviceDesk = serviceDesk;
//	this.shelves = shelves;
//  }
  
//  public RetrieverState(RetrieverAgent agent, List<Door> doors, List<Room> rooms,
//						ServiceDesk serviceDesk,
//						List<Shelf> shelves,
//						RetrievableObject objectLookingFor, List<RetrievableObject> objects) {
//    this.agent = agent;
//    this.doors = doors;
//    this.rooms = rooms;
//    this.serviceDesk = serviceDesk;
//    this.shelves = shelves;
//    this.objectLookingFor = objectLookingFor;
//    this.objects = objects;
//  }
  
  
  public RetrieverState(RetrieverAgent agent, List<Door> doors,
						List<Room> rooms,
						ServiceDesk serviceDesk,
						List<Shelf> shelves,
						List<RetrievableObject> objects) {
	this.agent = agent;
	this.doors = doors;
	this.rooms = rooms;
	this.serviceDesk = serviceDesk;
	this.shelves = shelves;
	this.objects = objects;
  }
  
  
  
  public static boolean isWithin(int x, int y, Room room) {
    // Note the less than/greater than or EQUAL to
	return (x <= room.right && x >= room.left && y >= room.top && y <= room.bottom);
  }
  
  public Room whichRoom(int x, int y) {
	for (Room r : rooms) {
	  if (isWithin(x, y, r)) {
	    return r;
	  }
	}
	throw new RuntimeException("Given x and y are not contained by any room: (" + x + ", " + y +
							   ')');
  }
  
  public int presentObject(String objectName) {
	for (RetrievableObject object : this.objects) {
	  if (object.name().equals(objectName)) {
	    return object.present;
	  }
	}
//	return -10;
	throw new RuntimeException("Object does not exist by this name: " + objectName);
  }
  
  //  public Shelf whichShelf(int x, int y) {
  public boolean isShelf(int x, int y) {
	for (Shelf shelf : shelves) {
	  if (x == shelf.x && y == shelf.y) {
//	    return shelf;
		return true;
	  }
	}
	return false;
  }
  
  public Shelf getShelf(String shelfName) {
	for (Shelf shelf : shelves) {
	  if (shelf.name().equals(shelfName)) {
	    return shelf;
	  }
	}
	throw new RuntimeException("Shelf now found by the following name: " + shelfName);
  }
  
  public Shelf whichShelf(int x, int y) {
	for (Shelf shelf : shelves) {
	  if (x == shelf.x && y == shelf.y)
	    return shelf;
	}
	return null;
  }
  
  public boolean isDoor(int x, int y) {
	for (Door door : doors) {
	  if (x == door.x && y == door.y) {
	    return true;
	  }
	}
	return false;
  }
  
  
  @Override
  public MutableOOState addObject(ObjectInstance objectInstance) {
	if (objectInstance instanceof Room) {
	  Room newRoom = (Room) objectInstance;
	  // Note making a copy of the list.
	  List<Room> roomList = new ArrayList<>();
	  for (Room room : rooms) {
		roomList.add(room);
	  }
	  roomList.add(newRoom);
	  return new RetrieverState(agent, doors, roomList, serviceDesk, shelves, objects);
	}
	if (objectInstance instanceof Shelf) {
	  Shelf newShelf = (Shelf) objectInstance;
	  // Note making a copy of the list.
	  List<Shelf> shelfList = new ArrayList<>();
	  for (Shelf shelf : shelves) {
		shelfList.add(shelf);
	  }
	  shelfList.add(newShelf);
	  return new RetrieverState(agent, doors, rooms, serviceDesk, shelfList, objects);
	}
	if (objectInstance instanceof Door) {
	  Door newDoor = (Door) objectInstance;
	  // Note making a copy of the list.
	  List<Door> doorList = new ArrayList<>();
	  for (Door door : doors) {
		doorList.add(door);
	  }
	  doorList.add(newDoor);
	  return new RetrieverState(agent, doorList, rooms, serviceDesk, shelves,
								objects);
	}
	if (objectInstance instanceof RetrieverAgent) {
	  RetrieverAgent newAgent = (RetrieverAgent) objectInstance;
	  return new RetrieverState(newAgent, doors, rooms, serviceDesk, shelves, objects);
	}
	if (objectInstance instanceof ServiceDesk) {
	  ServiceDesk newServiceDesk = (ServiceDesk) objectInstance;
	  return new RetrieverState(agent, doors, rooms, newServiceDesk, shelves,
								objects);
	}
//	if (objectInstance instanceof RetrievableObject) {
//	  RetrievableObject newObj = (RetrievableObject) objectInstance;
//	  return new RetrieverState(agent, doors, rooms, serviceDesk, shelves, newObj, objects);
//	}
	if (objectInstance instanceof RetrievableObject) {
	  RetrievableObject newObj = (RetrievableObject) objectInstance;
	  List<RetrievableObject> objList = new ArrayList<>();
	  for (RetrievableObject object : objects) {
	    objList.add(object);
	  }
	  objList.add(newObj);
	  return new RetrieverState(agent, doors, rooms, serviceDesk, shelves, objList);
	}
	throw new RuntimeException(
			"Object not an acceptable type in Retriever State: " + objectInstance.name());
  }
  
  @Override
  public MutableOOState removeObject(String s) {
    // Note making a copy of the list.
	if (s.equals(agent.name())) {
	  return new RetrieverState(null, doors, rooms, serviceDesk, shelves, objects);
	}
	if (s.equals(serviceDesk.name())) {
	  return new RetrieverState(agent, doors, rooms, null, shelves, objects);
	}
//	if (s.equals(objectLookingFor.name())) {
//	  return new RetrieverState(agent, doors, rooms, serviceDesk, shelves, null);
//	}
	for (RetrievableObject object : objects) {
	  if (s.equals(object.name())) {
		List<RetrievableObject> objList = new ArrayList<>();
		for (RetrievableObject object2 : objects) {
		  if (!object2.name().equals(s)) {
			objList.add(object2);
		  }
		}
		return new RetrieverState(agent, doors, rooms, serviceDesk, shelves, objList);
	  }
	}
	for (Room room : rooms) {
	  if (s.equals(room.name())) {
		List<Room> roomList = new ArrayList<>();
		for (Room room2 : rooms) {
		  if (!room2.name().equals(s)) {
			roomList.add(room2);
		  }
		}
		return new RetrieverState(agent, doors, roomList, serviceDesk, shelves, objects);
	  }
	}
	for (Door door : doors) {
	  if (s.equals(door.name())) {
		List<Door> doorList = new ArrayList<>();
		for (Door door2 : doors) {
		  if (!door2.name().equals(s)) {
			doorList.add(door2);
		  }
		}
		return new RetrieverState(agent, doorList, rooms, serviceDesk, shelves, objects);
	  }
	}
	for (Shelf shelf : shelves) {
	  if (s.equals(shelf.name())) {
		List<Shelf> shelfList = new ArrayList<>();
		for (Shelf shelf2 : shelves) {
		  if (!shelf2.name().equals(s)) {
			shelfList.add(shelf2);
		  }
		}
		return new RetrieverState(agent, doors, rooms, serviceDesk, shelfList, objects);
	  }
	}
	throw new RuntimeException("Nothing with the given string found in the state: " + s);
	 
  }
  
  @Override
  public MutableOOState renameObject(String s, String s1) {
	throw new RuntimeException("Should not be renaming objects");
  }
  
  @Override
  public int numObjects() {
    int count = 0;
	if (agent != null) {
	  count++;
	}
	if (serviceDesk != null) {
	  count++;
	}
	for (Room room : rooms) {
	  if (room != null) {
	    count++;
	  }
	}
	for (Door door : doors) {
	  if (door != null) {
	    count++;
	  }
	}
	for (Shelf shelf : shelves) {
	  if (shelf != null) {
	    count++;
	  }
	}
	return count;
  }
  
  @Override
  public ObjectInstance object(String oName) {
	if (oName.equals(agent.name())) {
	  return agent;
	}
	if (oName.equals(serviceDesk.name)) {
	  return serviceDesk;
	}
	for (RetrievableObject object : objects) {
	  if (object.name().equals(oName)) {
	    return object;
	  }
	}
	for (Room room : rooms) {
	  if (oName.equals(room.name)) {
	    return room;
	  }
	}
	for (Door door : doors) {
	  if (oName.equals(door.name)) {
	    return door;
	  }
	}
	for (Shelf shelf : shelves) {
	  if (oName.equals(shelf.name)) {
	    return shelf;
	  }
	}
	throw new RuntimeException("Object not found: " + oName);
  }
  
  @Override
  public List<ObjectInstance> objects() {
	List<ObjectInstance> objList = new ArrayList<>();
	objList.add(agent);
//	objList.add(objectLookingFor);
	objList.addAll(objects);
	objList.add(serviceDesk);
	objList.addAll(doors);
	objList.addAll(shelves);
	objList.addAll(rooms);
	return objList;
  }
  
  @Override
  public List<ObjectInstance> objectsOfClass(String s) {
	List<ObjectInstance> listObj = new ArrayList<>();
	if (s.equals(CLASS_ROOM)) {
	  listObj.addAll(rooms);
	  return listObj;
	}
	if (s.equals(CLASS_AGENT)) {
	  listObj.add(agent);
	  return listObj;
	}
	if (s.equals(CLASS_DOOR)) {
	  listObj.addAll(doors);
	  return listObj;
	}
	if (s.equals(CLASS_SERVICE_DESK)) {
	  listObj.add(serviceDesk);
	  return listObj;
	}
	if (s.equals(CLASS_SHELF)) {
	  listObj.addAll(shelves);
	  return listObj;
	}
	if (s.equals(CLASS_RETRIEVABLE_OBJECTS)) {
//	  listObj.add(objectLookingFor);
	  listObj.addAll(objects);
	  return listObj;
	}
	throw new RuntimeException("Class name not found: " + s);
  }
  
  @Override
  public MutableState set(Object variableKey, Object value) {
	if (!(variableKey instanceof String)) {
	  throw new RuntimeException("The given variable key needs to be of type String.");
	}
	String key = (String) variableKey;
	if (key.equals(VAR_X)) {
	  int newX = (Integer) StateUtilities.stringOrNumber(value);
	  return new RetrieverState(new RetrieverAgent(agent.name(), newX, agent.y, agent
			  .curDirection, agent.objectsInPocket, agent.imDone), doors, rooms, serviceDesk, shelves,
								objects);
	}
	if (key.equals(VAR_Y)) {
	  int newY = (Integer) StateUtilities.stringOrNumber(value);
	  return new RetrieverState(new RetrieverAgent(agent.name(), agent.x, newY, agent
			  .curDirection, agent.objectsInPocket, agent.imDone), doors, rooms, serviceDesk, shelves,
								objects);
	}
	if (key.equals(VAR_DIRECTION)) {
	  if (!(value instanceof String)) {
		throw new RuntimeException("To change the direction you must give a String");
	  }
	  String newDir = (String) value;
	}
 
	throw new RuntimeException("Should not be trying to set variable: " + key);
  }
  
  @Override
  public List<Object> variableKeys() {
	return Arrays.asList(VAR_X, VAR_Y, VAR_DIRECTION);
  }
  
  @Override
  public Object get(Object variableKey) {
	if (!(variableKey instanceof String)) {
	  throw new RuntimeException("The given variable key needs to be of type String.");
	}
	String key = (String) variableKey;
	if (key.equals(VAR_X)) {
	  return agent.x;
	}
	if (key.equals(VAR_Y)) {
	  return agent.y;
	}
	if (key.equals(VAR_DIRECTION)) {
	  return agent.curDirection;
	}
 
	throw new RuntimeException("Should not be trying to get variable: " + key);
  }
  
  @Override
  public State copy() {
    // NOTE: Copies agent as well.
	return new RetrieverState((RetrieverAgent) agent.copy(), doors, rooms, serviceDesk, shelves,
							  objects);
  }
  

}
