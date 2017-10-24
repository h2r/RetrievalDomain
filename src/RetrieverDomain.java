package retriever.src;

import baxterPickingAMDP2L0.src.BaxterPickingAMDP2L0Agent;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.planning.stochastic.rtdp.BoundedRTDP;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.mdp.singleagent.oo.ObjectParameterizedActionType;
import burlap.shell.visual.VisualExplorer;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;

import java.util.Arrays;
import java.util.List;

import static retriever.src.RetrieverState.getOffset;

public class RetrieverDomain implements DomainGenerator {
  public static final String CLASS_ROOM = "classRoom";
  public static final String CLASS_DOOR = "classDoor";
  public static final String CLASS_AGENT = "classAgent";
  public static final String CLASS_RETRIEVABLE_OBJECTS = "classRetrievableObjects";
  public static final String CLASS_SHELF = "classShelf";
  public static final String CLASS_SERVICE_DESK = "classServiceDesk";
  
  public static final String VAR_X = "x";
  public static final String VAR_Y = "y";
//  public static final String VAR_ROOM = "room";
  public static final String VAR_DIRECTION = "direction";
  
  public static final String ACTION_NORTH = "north";
  public static final String ACTION_SOUTH = "south";
  public static final String ACTION_EAST = "east";
  public static final String ACTION_WEST = "west";
  public static final String ACTION_PICK = "pick";
  public static final String ACTION_DONE = "done";
  private RewardFunction rf;
  private TerminalFunction tf;
  
  public RetrieverDomain(RewardFunction rf, TerminalFunction tf) {
	this.rf = rf;
	this.tf = tf;
  }
  
  public RetrieverDomain() {
    this.rf = null;
    this.tf = null;
  }
  
  @Override
  public OOSADomain generateDomain() {
	OOSADomain domain = new OOSADomain();
 
	domain.addStateClass(CLASS_AGENT, BaxterPickingAMDP2L0Agent.class);
	domain.addStateClass(CLASS_DOOR, Door.class);
	domain.addStateClass(CLASS_RETRIEVABLE_OBJECTS, RetrievableObject.class);
	domain.addStateClass(CLASS_ROOM, Room.class);
	domain.addStateClass(CLASS_SERVICE_DESK, ServiceDesk.class);
	domain.addStateClass(CLASS_SHELF, Shelf.class);
 
	domain.addActionTypes(new DoneActionType(ACTION_DONE, new String[]{CLASS_AGENT}),
						  new PickActionType(ACTION_PICK),
						  new NorthActionType(ACTION_NORTH, new String[]{CLASS_AGENT}),
						  new SouthActionType(ACTION_SOUTH, new String[]{CLASS_AGENT}),
						  new EastActionType(ACTION_EAST, new String[]{CLASS_AGENT}),
						  new WestActionType(ACTION_WEST, new String[]{CLASS_AGENT}));
//	domain.addActionType(new PickObjectParameterizedAction())
	
	RetrieverModel smodel = new RetrieverModel();
	RewardFunction rf = this.rf;
	TerminalFunction tf = this.tf;
	
	if (rf == null) {
	  rf = new RetrieverRF(); // TODO NEEDS A PARAMETER THAT'S THE OBJECT IT'S LOOKING FOR
	}
	if (tf == null) {
	  tf = new RetrieverTF();
	}
 
	FactoredModel model = new FactoredModel(smodel, rf, tf);
	domain.setModel(model);
	return domain;
  }
  
	
  
  public static class DoneActionType extends ObjectParameterizedActionType {
    public DoneActionType(String name, String[] params) { super(name, params); }
  
	@Override
	protected boolean applicableInState(State state, ObjectParameterizedAction action) {
	  RetrieverState s = (RetrieverState) state;
	  int[] offset = getOffset(s.agent.curDirection);
//	  return (s.agent.imDone && ((s.agent.x + offset[0]) == s.serviceDesk.x) &&
//			  ((s.agent.y + offset[1]) == s.serviceDesk.y));
	  return (((s.agent.x + offset[0]) == s.serviceDesk.x) && ((s.agent.y + offset[1]) == s
			  .serviceDesk.y));
	}
  }
  
  public static class PickActionType extends ObjectParameterizedActionType {
//    public PickActionType(String name, String[] params) { super(name, params); }
 
	public  PickActionType(String name) {
	  super(name, new String[]{CLASS_RETRIEVABLE_OBJECTS});
	}
	
	@Override
	protected boolean applicableInState(State state, ObjectParameterizedAction action) {
	  RetrieverState s = (RetrieverState) state;
		
	  String object = action.getObjectParameters()[0]; // The object name
	  
//	  if (s.agent.curDirection.equals(ACTION_NORTH) || s.agent.curDirection.equals(ACTION_SOUTH) ||
//		  s.agent.curDirection.equals(ACTION_EAST) || s.agent.curDirection.equals(ACTION_WEST)) {
//		int[] offset = getOffset(s.agent.curDirection);
//		return (s.isShelf(s.agent.x + offset[0], s.agent.y + offset[1]));
//	  }
//	  throw new RuntimeException("Something went wrong in PickActionType.");
	  int[] offset = getOffset(s.agent.curDirection);
	  Shelf shelf = s.whichShelf(s.agent.x + offset[0], s.agent.y + offset[1]);
	  if (shelf == null) {
	    return false;
	  }
	  
	  return shelf.keysToObjects.contains(object);
	}

	
	
  }
  
//  public static class PickObjectParameterizedAction implements ObjectParameterizedAction {
//    // TODO FIGURE OUT HOW TO PASS THIS INTO THE DOMAIN SO THEY KNOW TO USE IT
//	List<String> objectsToPick;
//
//	public PickObjectParameterizedAction(List<String> objectsToPick) {
//	  this.objectsToPick = objectsToPick;
//	}
//
//	public PickObjectParameterizedAction() {
//	}
//
//	@Override
//	public String[] getObjectParameters() {
//	  // TODO Question: Is this what this method is supposed to be
//	  return new String[]{CLASS_RETRIEVABLE_OBJECTS};
//	}
//
//	// NOTE: Setting new object parameters clears the original ones.
//	@Override
//	public void setObjectParameters(String[] strings) {
//	  this.objectsToPick.clear();
//	  for (String object : strings) {
//		this.objectsToPick.add(object);
//	  }
//	}
//
//	@Override
//	public String actionName() {
//	  return ACTION_PICK;
//	}
//
//	@Override
//	public Action copy() {
//	  return new PickObjectParameterizedAction(objectsToPick);
//	}
//  }
  
  public static class NorthActionType extends ObjectParameterizedActionType {
    public NorthActionType(String name, String[] params) { super(name, params); }
  
	@Override
	protected boolean applicableInState(State s, ObjectParameterizedAction a) {
      // TODO FILL IN LATER
	  return true;
	}
  }
  
  public static class SouthActionType extends ObjectParameterizedActionType {
	public SouthActionType(String name, String[] params) { super(name, params); }
	
	@Override
	protected boolean applicableInState(State s, ObjectParameterizedAction a) {
	  // TODO FILL IN LATER
	  return true;
	}
  }
  
  public static class EastActionType extends ObjectParameterizedActionType {
	public EastActionType(String name, String[] params) { super(name, params); }
	
	@Override
	protected boolean applicableInState(State s, ObjectParameterizedAction a) {
	  // TODO FILL IN LATER
	  return true;
	}
  }
  public static class WestActionType extends ObjectParameterizedActionType {
	public WestActionType(String name, String[] params) { super(name, params); }
	
	@Override
	protected boolean applicableInState(State s, ObjectParameterizedAction a) {
	  // TODO FILL IN LATER
	  return true;
	}
  }
  
  public static void main(String[] args) {
	RetrieverDomain domain = new RetrieverDomain();
	String goalRoom = "room2";
//	RetrieverDomain domain = new RetrieverDomain(new RetrieverRoomRF(goalRoom), new RetrieverRoomTF
//			(goalRoom));
	OOSADomain d = domain.generateDomain();
	List<Room> rooms = Arrays.asList(
						       new Room("room1", 6, 11, 10, 0, "red"),
							   new Room("room2", 0, 5, 5, 0, "green"),
							   new Room("room3", 0, 5, 10, 6, "blue"));
	List<Shelf> shelves = Arrays.asList(new Shelf("shelf1", 0, 0, Arrays.asList("bucket",
																				"book")));
	List<Door> doors = Arrays.asList(new Door("door1", 3, 5, false),
									 new Door("door2", 7, 5, false));
	ServiceDesk serviceDesk = new ServiceDesk("serviceDesk1", 10, 11);
	List<RetrievableObject> objects = Arrays.asList(
			new RetrievableObject("bucket", "type", "brown", "shelf1",
								  RetrievableObject.UNINITIALIZED),
			new RetrievableObject("book", "type", "red", "shelf1",
								  RetrievableObject.UNINITIALIZED));
	RetrieverAgent agent = new RetrieverAgent("agent1", 0, 11, ACTION_EAST, Arrays.asList(),
											  false);
	State s = new RetrieverState(agent, doors, rooms, serviceDesk, shelves, objects);
 
//	Visualizer v = RetrieverVisualizer.getVisualizer(10, 11);
	Visualizer v = RetrieverVisualizer.getVisualizer(11, 12);
	
	VisualExplorer exp = new VisualExplorer((SADomain) d, v, s);
 
	RetrieverEnvironment env = new RetrieverEnvironment(d, s);
	
//	ConstantValueFunction heuristic = new ConstantValueFunction(1.);
//	BoundedRTDP brtp = new BoundedRTDP(d, 0.99, new SimpleHashableStateFactory(false),
//									   new ConstantValueFunction(0.0), heuristic, 0.01, 500);
//	brtp.setMaxRolloutDepth(50);
//	brtp.toggleDebugPrinting(false);
//	Policy p = brtp.planFromState(s);
//
//
//	Episode ea = PolicyUtils.rollout(p, env, 100);
//	new EpisodeSequenceVisualizer(v, d, Arrays.asList(ea));
	
	exp.addKeyAction("s", ACTION_NORTH, "");
	exp.addKeyAction("a", ACTION_WEST, "");
	exp.addKeyAction("w", ACTION_SOUTH, "");
	exp.addKeyAction("d", ACTION_EAST, "");
	exp.addKeyAction("p", ACTION_PICK, "bucket");
	exp.addKeyAction("o", ACTION_PICK, "book");
	exp.addKeyAction("l", ACTION_DONE, "");
 
	exp.initGUI();
  }
  
  
  
}
