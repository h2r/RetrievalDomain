package retriever.src;

import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.visualizer.OOStatePainter;
import burlap.visualizer.ObjectPainter;
import burlap.visualizer.StateRenderLayer;
import burlap.visualizer.Visualizer;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;

import static retriever.src.RetrieverDomain.*;

public class RetrieverVisualizer {
  
//  public static Visualizer getVisualizer(String... agentImagePath) {
//	Visualizer v = new Visualizer(getStateRenderLayer(agentImagePath));
//	return v;
//  }
  
//  public static StateRenderLayer getStateRenderLayer(String[] agentImagePath) {
//    StateRenderLayer v = new StateRenderLayer();
//	OOStatePainter ooPainter = new OOStatePainter();
//
//	ooPainter.addObjectClassPainter(CLASS_ROOM, new RoomPainter());
//	ooPainter.addObjectClassPainter(CLASS_DOOR, new DoorPainter());
//	ooPainter.addObjectClassPainter(CLASS_SERVICE_DESK, new ServiceDeskPainter());
//	ooPainter.addObjectClassPainter(CLASS_SHELF, new ShelfPainter());
//	if (agentImagePath.length == 0) {
//	  ooPainter.addObjectClassPainter(CLASS_AGENT, new AgentPainter());
//	} else {
//	  ooPainter.addObjectClassPainter(CLASS_AGENT, new AgentPainterWithImages(agentImagePath[0]));
//	}
//
//	v.addStatePainter(ooPainter);
//
//	return v;
//
//  }
  
  
  public static Visualizer getVisualizer(int maxX, int maxY) {
	Visualizer v = new Visualizer(getStateRenderLayer(maxX, maxY));
	return v;
  }
  
  //  public static StateRenderLayer getStateRenderLayer(int maxX, int maxY, String... agentImagePath) {
  public static StateRenderLayer getStateRenderLayer(int maxX, int maxY) {
  
    StateRenderLayer v = new StateRenderLayer();
	OOStatePainter ooPainter = new OOStatePainter();
	
	ooPainter.addObjectClassPainter(CLASS_ROOM, new RoomPainter(maxX, maxY));
	ooPainter.addObjectClassPainter(CLASS_DOOR, new DoorPainter(maxX, maxY));
	ooPainter.addObjectClassPainter(CLASS_SERVICE_DESK, new ServiceDeskPainter(maxX, maxY));
	ooPainter.addObjectClassPainter(CLASS_SHELF, new ShelfPainter(maxX, maxY));
	ooPainter.addObjectClassPainter(CLASS_AGENT, new AgentPainter(maxX, maxY));
//	if (agentImagePath.length == 0) {
//	  ooPainter.addObjectClassPainter(CLASS_AGENT, new AgentPainter());
//	} else {
//	  ooPainter.addObjectClassPainter(CLASS_AGENT, new AgentPainterWithImages(agentImagePath[0],
//																			  maxX, maxY));
//	}
 
	v.addStatePainter(ooPainter);
 
	return v;
  }
  
  private static class RoomPainter implements ObjectPainter {
    protected int maxX, maxY;
  
//	public RoomPainter() {
//	  maxX = -1;
//	  maxY = -1;
//	}
 
	public RoomPainter(int maxX, int maxY) {
	  this.maxX = maxX;
	  this.maxY = maxY;
	}
 
 
	@Override
	public void paintObject(Graphics2D g2, OOState state, ObjectInstance objectInstance,
							float cWidth, float cHeight) {
	  
	  Room ob = (Room) objectInstance;
	  RetrieverState s = (RetrieverState) state;
	  
//	  float domainXScale = CleanupDomain.maxRoomXExtent(s) + 1f;
//	  float domainYScale = CleanupDomain.maxRoomYExtent(s) + 1f;
//
//	  if (maxX != -1) {
//	    domainXScale = maxX;
//	    domainYScale = maxY;
//	  }
	
	  // TODO: ASK NAKUL WHY WE HAVE TO CALCULATE THIS STUFF EVERYTIME
	  float domainXScale = maxX;
	  float domainYScale = maxY;
	  
	  // Determine the normalized width/height
	  float width = (1.0f / domainXScale) * cWidth;
	  float height = (1.0f / domainYScale) * cHeight;
	  
	  Color rcol = colorForName(ob.color);
	  float[] hsb = new float[3];
	  Color.RGBtoHSB(rcol.getRed(), rcol.getGreen(), rcol.getBlue(), hsb);
	  hsb[1] = 0.4f; // TODO: WHAT DOES THIS DO?
	  rcol = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
	  
	  for (int i = ob.left; i <= ob.right; i++) {
		for (int j = ob.bottom ; j <= ob.top; j++) {
		  
		  float rx = i*width;
		  float ry = cHeight - height - j*height;
		  
		  if ((i == ob.left || i == ob.right || j == ob.bottom || j == ob.top) && !s.isDoor(i, j)) {
			g2.setColor(Color.BLACK);
			g2.fill(new Rectangle2D.Float(rx, ry, width, height));
		  } else {
			g2.setColor(rcol);
//			g2.setColor(Color.red);
			g2.fill(new Rectangle2D.Float(rx, ry, width, height));
		  }
		}
	  }
	}
  }
  
  private static class DoorPainter implements ObjectPainter {
	protected int maxX, maxY;
 
	public DoorPainter(int maxX, int maxY) {
	  this.maxX = maxX;
	  this.maxY = maxY;
	}
 
//	public DoorPainter() {
//	}
 
	@Override
	public void paintObject(Graphics2D g2, OOState state, ObjectInstance objectInstance,
							float cWidth, float cHeight) {
	  
	  Door ob = (Door) objectInstance;
	  
	  //	  float domainXScale = CleanupDomain.maxRoomXExtent(s) + 1f;
//	  float domainYScale = CleanupDomain.maxRoomYExtent(s) + 1f;
//
//	  if (maxX != -1) {
//	    domainXScale = maxX;
//	    domainYScale = maxY;
//	  }
	  
	  float domainXScale = maxX;
	  float domainYScale = maxY;
	  
	  // Determine the normalized width/height
	  float width = (1.0f / domainXScale) * cWidth;
	  float height = (1.0f / domainYScale) * cHeight;
	  
//	  g2.setColor(Color.white);
	  g2.setColor(Color.black);
	  g2.fill(new Rectangle2D.Float(ob.x * width, cHeight - height - ob.y * height, width, height));
//	  g2.fill(new Line2D.Float(ob.x * width, cHeight - height - ob.y, width, height));
	  
	}
  }
  
  private static class ServiceDeskPainter implements ObjectPainter {
    protected int maxX, maxY;
    
	public ServiceDeskPainter(int maxX, int maxY) {
	  this.maxX = maxX;
	  this.maxY = maxY;
	}
 
	@Override
	public void paintObject(Graphics2D g2, OOState state, ObjectInstance objectInstance,
							float cWidth, float cHeight) {
	  
	  ServiceDesk ob = (ServiceDesk) objectInstance;
	  
	  //	  float domainXScale = CleanupDomain.maxRoomXExtent(s) + 1f;
//	  float domainYScale = CleanupDomain.maxRoomYExtent(s) + 1f;
//
//	  if (maxX != -1) {
//	    domainXScale = maxX;
//	    domainYScale = maxY;
//	  }
	  
	  float domainXScale = maxX;
	  float domainYScale = maxY;
	  
	  // Determine the normalized width/height
	  float width = (1.0f / domainXScale) * cWidth;
	  float height = (1.0f / domainYScale) * cHeight;
	  
	  g2.setColor(Color.blue);
	  g2.fill(new Rectangle2D.Float(ob.x * width, cHeight - height - ob.y * height, width, height));
	  
	}
  }
  
  private static class ShelfPainter implements ObjectPainter {
	protected int maxX, maxY;
	
	public ShelfPainter(int maxX, int maxY) {
	  this.maxX = maxX;
	  this.maxY = maxY;
	}
	
	@Override
	public void paintObject(Graphics2D g2, OOState state, ObjectInstance objectInstance,
							float cWidth, float cHeight) {
	  
	  Shelf ob = (Shelf) objectInstance;
	  
	  //	  float domainXScale = CleanupDomain.maxRoomXExtent(s) + 1f;
//	  float domainYScale = CleanupDomain.maxRoomYExtent(s) + 1f;
//
//	  if (maxX != -1) {
//	    domainXScale = maxX;
//	    domainYScale = maxY;
//	  }
	  
	  float domainXScale = maxX;
	  float domainYScale = maxY;
	  
	  // Determine the normalized width/height
	  float width = (1.0f / domainXScale) * cWidth;
	  float height = (1.0f / domainYScale) * cHeight;
	  
	  g2.setColor(Color.gray);
	  g2.fill(new Rectangle2D.Float(ob.x * width, cHeight - height - ob.y * height, width, height));
	  
	}
  }
  
  private static class AgentPainter implements ObjectPainter {
  	protected int maxX, maxY;
   
//	public AgentPainter() {
//	}
 
	public AgentPainter(int maxX, int maxY) {
	  this.maxX = maxX;
	  this.maxY = maxY;
	}
	
	@Override
	public void paintObject(Graphics2D g2, OOState s, ObjectInstance objectInstance,
							float cWidth, float cHeight) {
	  
	  RetrieverAgent ob = (RetrieverAgent) objectInstance;
	  
	  //	  float domainXScale = CleanupDomain.maxRoomXExtent(s) + 1f;
//	  float domainYScale = CleanupDomain.maxRoomYExtent(s) + 1f;
//
//	  if (maxX != -1) {
//	    domainXScale = maxX;
//	    domainYScale = maxY;
//	  }
	  
	  float domainXScale = maxX;
	  float domainYScale = maxY;
	  
	  // Determine the normalized width/height
	  float width = (1.0f / domainXScale) * cWidth;
	  float height = (1.0f / domainYScale) * cHeight;
	  
	  g2.setColor(Color.darkGray);
	  g2.fill(new Rectangle2D.Float(ob.x * width, cHeight - height - ob.y * height, width, height));
	  
	}
  }
  
//  private static class AgentPainterWithImages implements ObjectPainter {
//	public AgentPainterWithImages(String pathToImageDir) {
//	  if ()
//	}
//
//	public AgentPainterWithImages(String s, int maxX, int maxY) {
//	}
//  }
  
  protected static Color colorForName(String colorName) {
    Color col = Color.darkGray;
  
	Field field;
	try {
	  field = Class.forName("java.awt.Color").getField(colorName);
	  col = (Color) field.get(null);
	} catch (Exception e) {
	
	}
	
	return col;
  }
  
}
