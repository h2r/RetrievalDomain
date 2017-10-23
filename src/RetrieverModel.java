package retriever.src;

import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;

import java.util.ArrayList;
import java.util.List;

import static retriever.src.RetrievableObject.PRESENT;
import static retriever.src.RetrieverDomain.*;
import static retriever.src.RetrieverState.getOffset;

public class RetrieverModel implements FullStateModel {
  
  @Override
  public List<StateTransitionProb> stateTransitions(State s, Action a) {
	return FullStateModel.Helper.deterministicTransition(this, s, a);
  }
  
  public RetrieverModel() {
  }
  
  @Override
  public State sample(State state, Action action) {
    RetrieverState s = (RetrieverState) state;
  
    // TODO ACTIONS SHOULD RETURN THE SAME STATE IF HITTING A WALL.  CHANGE THIS
	// TODO: 1) Sampler returns the state of the object as visible when the agent is in front of
	// TODO: the object otherwise the location of the object is unknown
	//TODO: pick needs a conditional check for object present
	//TODO: need a terminal action -> when no object agents gets to stop searching!!
	
	// TODO Nakul says you need to list all objects are present when you look at a shelf.  Remove
	// TODO listing items as PRESENT in PICK and just put them whenever you look at the shelf.
	if (action.actionName().equals(ACTION_WEST) || action.actionName().equals(ACTION_NORTH) ||
		action.actionName().equals(ACTION_SOUTH) || action.actionName().equals(ACTION_EAST)) {
	  int[] offset = getOffset(action.actionName());
//	  int[] offset = getOffset(s.agent.curDirection);
	  
	  // TODO THIS SHOULD BE DONE AFTER FINDING THE AGENT'S NEW LOCATION.  STOP IT FROM RUNNING
	  // TODO INTO SHELF
	  
	  
	  boolean isShelf = s.isShelf(s.agent.x + offset[0], s.agent.y + offset[1]);
//	  Shelf shelf = s.whichShelf(s.agent.x + offset[0], s.agent.y + offset[1]);
//	  boolean isShelf = (shelf != null);
	  int newX = s.agent.x;
	  int newY = s.agent.y;
	  
	  // TODO PUT THIS ABOVE CHECKING FOR THE SHELF.  CHECK IF LEGAL MOVE FIRST!
	  if ((s.isWithin(s.agent.x + offset[0], s.agent.y + offset[1], s.whichRoom(s.agent.x, s
			  .agent.y)) && !isShelf && !(s.serviceDesk.x == s.agent.x + offset[0] &&
										  s.serviceDesk.y == s.agent.y + offset[1])) ||
		  s.isDoor(s.agent.x + offset[0], s.agent.y + offset[1]) || s.isDoor(s.agent.x, s.agent.y)) { // NOTE: WORKS IF FACING OR STANDING ON A DOOR
		newX += offset[0];
		newY += offset[1];
//		return new RetrieverState(
//				new RetrieverAgent(s.agent.name(), s.agent.x + offset[0], s.agent.y + offset[1],
//								   action.actionName(), s.agent.objectsInPocket,
//								   s.agent.imDone), s.doors, s.rooms, s.serviceDesk, s.shelves,
//				s.objects);
//	  } else {
//		return new RetrieverState(s.agent.copyWithDirection(action.actionName()), s.doors, s
//				.rooms, s.serviceDesk, s.shelves, s.objects);
	  }
	  
	  // Resetting the boolean to be the current thing we're facing
	  Shelf shelf = s.whichShelf(newX + offset[0], newY + offset[1]);
	  isShelf = (shelf != null);
	  
	  if (isShelf) {
		// If you look at a shelf at all, it will mark those items as present
//		Shelf shelf = s.whichShelf(s.agent.x + offset[0], s.agent.y + offset[1]);
		for (String key : shelf.keysToObjects) {
		  for (RetrievableObject object : s.objects) {
			if (key.equals(object.name())) {
//			  System.out.print(object.name + " present?: " + object.present + '\n');
			  object.present = PRESENT;
			  
			}
		  }
		}
	  }
	  
	  return new RetrieverState(new RetrieverAgent(s.agent.name(), newX, newY, action.actionName
			  (), s.agent.objectsInPocket, s.agent.imDone), s.doors, s.rooms, s.serviceDesk, s
										.shelves, s.objects);
	  
	  
	}
 
	if (action.actionName().equals(ACTION_PICK)) {
	  ObjectParameterizedAction a = (ObjectParameterizedAction) action;
	  String objectToGet = a.getObjectParameters()[0];
	  int[] offset = getOffset(s.agent.curDirection);
	  Shelf shelf = s.whichShelf(s.agent.x + offset[0], s.agent.y + offset[1]);
	  
	  // Making a new list of shelf objects.
	  List<String> newShelfObjs = new ArrayList<>();
	  for (String objectInShelf : shelf.keysToObjects) {
		if (!objectInShelf.equals(objectInShelf)) {
		  newShelfObjs.add(objectInShelf);
		}
	  }
	  List<Shelf> newShelves = new ArrayList<>();
	  for (Shelf shelf1 : s.shelves) {
		if (!shelf.name().equals(shelf1.name())) {
		  newShelves.add(shelf1);
		}
	  }
	  newShelves.add(shelf);
	  
	  
	  // Making a new list of agent objects.
	  List<String> newAgentObjs = new ArrayList<>();
	  for (String agentObject : s.agent.objectsInPocket) {
		newAgentObjs.add(agentObject);
	  }
	  newAgentObjs.add(objectToGet);
	  
	  return new RetrieverState(new RetrieverAgent(s.agent.name(), s.agent.x, s.agent.y, s.agent
			  .curDirection, newAgentObjs, s.agent.imDone), s.doors, s.rooms, s.serviceDesk,
								newShelves, s.objects);
	  
	}
	
//	if (action.actionName().equals(ACTION_PICK)) {
//	  // TODO FIX THIS BY GETTING THE OBJECT NAME FROM THE ACITON.  AND TRADING IT WITH THE SHELVES.
////	  PickObjectParameterizedAction a = (PickObjectParameterizedAction) action;
//
//	  int[] offset = getOffset(s.agent.curDirection);
//	  int newX = s.agent.x + offset[0];
//	  int newY = s.agent.y + offset[1];
//	  Shelf foundShelf = s.whichShelf(newX, newY);
//	  if (foundShelf != null) {
//		List<String> newAgentObjs = new ArrayList<>();
//		newAgentObjs.addAll(s.agent.objectsInPocket);
//		List<String> newShelfObjs = new ArrayList<>();
//		// NOTE POTENTIAL BUG: IF THE AGENT STARTS FACING A SHELF, THOSE OBJECTS WILL NOT BE
//		// MARKED AS PRESENT
//		for (String objectName : a.objectsToPick) {
//		  for (String objects : foundShelf.keysToObjects) {
//			if (objectName.equals(objects)) {
//			  newAgentObjs.add(objectName);
//			} else {
//			  newShelfObjs.add(objectName);
//			}
//		  }
//		}
//		List<Shelf> newShelfList = new ArrayList<>();
//		for (Shelf shelf : s.shelves) {
//		  if (shelf.name().equals(foundShelf.name())) {
//			Shelf newShelf = new Shelf(shelf.name(), shelf.x, shelf.y, newShelfObjs);
//			newShelfList.add(newShelf);
//		  } else {
//			newShelfList.add(shelf);
//		  }
//
//		}
//		return new RetrieverState(new RetrieverAgent(s.agent.name, s.agent.x, s.agent.y, s.agent
//				.curDirection, newAgentObjs, s.agent.imDone), s.doors, s.rooms, s.serviceDesk,
//								  newShelfList, s.objects);
//	  } else {
//		return s.copy();
//	  }
//	}
 
//	if (action.actionName().equals(ACTION_PICK)) {
	if (action.actionName().equals(ACTION_DONE)) {
	  // Done action type checks if valid.
	  return new RetrieverState(new RetrieverAgent(s.agent.name(), s.agent.x, s.agent.y, s.agent
			  .curDirection, s.agent.objectsInPocket, true), s.doors, s.rooms, s.serviceDesk, s
			  .shelves, s.objects);
	}
 
//	if (action.actionName().equals(ACTION_DONE)) {
//	  return new RetrieverState(new RetrieverAgent(s.agent.name(), s.agent.x, s.agent.y, s.agent
//			  .curDirection, s.agent.objectsInPocket, ))
//	}
	
	throw new RuntimeException("Action not found: " + action.actionName());
  }
}

//	if (action.actionName().equals(ACTION_EAST)) {
//	  boolean isShelf = s.isShelf(s.agent.x + 1, s.agent.y);
//	  if (s.isWithin(s.agent.x + 1, s.agent.y, s.whichRoom(s.agent.x, s.agent.y)) &&
//			  !s.isShelf(s.agent.x + 1, s.agent.y)) {
//		return new RetrieverState(new RetrieverAgent(s.agent.name, s.agent.x + 1, s.agent.y,
//													 ACTION_EAST, s.agent.objectsInPocket, s
//															 .agent.imDone), s
//				.doors, s.rooms, s.serviceDesk, s.shelves, s.objectLookingFor);
//	  } else {
//		return new RetrieverState(s.agent.copyWithDirection(ACTION_EAST), s.doors, s.rooms, s
//				.serviceDesk, s.shelves, s.objects);
//	  }
//	}
//	if (action.actionName().equals(RetrieverDomain.ACTION_NORTH)) {
//	  if (s.isWithin(s.agent.x, s.agent.y - 1, s.whichRoom(s.agent.x, s.agent.y)) &&
//			  !s.isShelf(s.agent.x, s.agent.y - 1)) {
//		return new RetrieverState(new RetrieverAgent(s.agent.name, s.agent.x, s.agent.y - 1,
//													 ACTION_NORTH, s.agent.objectsInPocket, s
//															 .agent.imDone), s
//				.doors, s.rooms, s.serviceDesk, s.shelves, s.objects);
//	  } else {
//		return new RetrieverState(s.agent.copyWithDirection(ACTION_NORTH), s.doors, s.rooms, s
//				.serviceDesk, s.shelves, s.objects);
//	  }
//	}
//	if (action.actionName().equals(RetrieverDomain.ACTION_SOUTH)) {
//	  if (s.isWithin(s.agent.x, s.agent.y + 1, s.whichRoom(s.agent.x, s.agent.y)) &&
//			  !s.isShelf(s.agent.x, s.agent.y + 1)) {
//		return new RetrieverState(new RetrieverAgent(s.agent.name, s.agent.x, s.agent.y + 1,
//													 ACTION_SOUTH, s.agent.objectsInPocket, s
//															 .agent.imDone), s
//				.doors, s.rooms, s.serviceDesk, s.shelves, s.objects);
//	  } else {
//		return new RetrieverState(s.agent.copyWithDirection(ACTION_SOUTH), s.doors, s.rooms, s
//				.serviceDesk, s.shelves, s.objects);
//	  }
//	}
//	if (action.actionName().equals(RetrieverDomain.ACTION_WEST)) {
//	  if (s.isWithin(s.agent.x - 1, s.agent.y, s.whichRoom(s.agent.x, s.agent.y)) &&
//			  !s.isShelf(s.agent.x - 1, s.agent.y)) {
//		return new RetrieverState(new RetrieverAgent(s.agent.name, s.agent.x - 1, s.agent.y,
//													 ACTION_WEST, s.agent.objectsInPocket, s
//															 .agent.imDone), s
//				.doors, s.rooms, s.serviceDesk, s.shelves, s.objects);
//	  } else {
//		return new RetrieverState(s.agent.copyWithDirection(ACTION_WEST), s.doors, s.rooms, s
//				.serviceDesk, s.shelves, s.objects);
//	  }
//	}



//	if (action.actionName().equals(ACTION_PICK)) {
//	  int newX = s.agent.x;
//	  int newY = s.agent.y;
//	  if (s.agent.curDirection.equals(ACTION_EAST)) {
//	    newX += 1;
//	  } else if (s.agent.curDirection.equals(ACTION_SOUTH)) {
//	    newY += 1;
//	  } else if (s.agent.curDirection.equals(ACTION_NORTH)) {
//	    newY -=1;
//	  } else if (s.agent.curDirection.equals(ACTION_WEST)) {
//	    newX -= 1;
//	  } else {
//		throw new RuntimeException("Current direction not set for agent");
//	  }
//	  Shelf foundShelf = s.whichShelf(newX, newY);
//	  if (foundShelf != null) {
//	    if (foundShelf.objects.contains(s.objectLookingFor)) {
//		  RetrieverAgent newAgent = (RetrieverAgent) s.agent.copy();
//		  for (RetrievableObject item : foundShelf.objects) {
//			item.present = PRESENT;
//			if (item.name().equals(s.objectLookingFor.name())) {
//			  List<RetrievableObject> objects = new ArrayList<>();
//			  objects.add(s.objectLookingFor);
//			  // NOTE: Resetting objects in pocket to one containing the object we're looking for.
//			  newAgent.objectsInPocket = objects;
//			}
//		  }
//		  foundShelf.objects.remove(s.objectLookingFor);
//		  return new RetrieverState(newAgent, s.doors, s.rooms, s.serviceDesk, s.shelves,
//									s.objectLookingFor);
//		}
//	  }
//	  return s.copy();
//	}