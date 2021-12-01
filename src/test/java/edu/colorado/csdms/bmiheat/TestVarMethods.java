package edu.colorado.csdms.bmiheat;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the getter and setter methods of the {@link BmiHeat} class.
 */
public class TestVarMethods {

  private static final int SIZEOF_DOUBLE = 8;

  private int gridId;
  private String varName;
  private String varUnits;
  private String varType;
  private String varLocation;
  private Integer gridSize;

  @Before
  public void setUp() throws Exception {
    gridId = 0;
    varName = "plate_surface__temperature";
    varUnits = "K";
    varType = "double";
    varLocation = "node";
    gridSize = 48;
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link BmiHeat#getVarGrid(java.lang.String)}.
   */
  @Test
  public final void testGetVarGrid() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    assertEquals(gridId, component.getVarGrid(varName));
  }

  /**
   * Test method for {@link BmiHeat#getVarType(java.lang.String)}.
   */
  @Test
  public final void testGetVarType() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    assertEquals(varType, component.getVarType(varName));
  }

  /**
   * Test method for {@link BmiHeat#getVarUnits(java.lang.String)}.
   */
  @Test
  public final void testGetVarUnits() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    assertEquals(varUnits, component.getVarUnits(varName));
  }

  /**
   * Test method for {@link BmiHeat#getVarItemsize(java.lang.String)}.
   */
  @Test
  public final void testGetVarItemsize() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    assertEquals(SIZEOF_DOUBLE, component.getVarItemsize(varName));
  }

  /**
   * Test method for {@link BmiHeat#getVarNbytes(java.lang.String)}.
   */
  @Test
  public final void testGetVarNbytes() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    double[] varCpy = new double[gridSize];
    component.getValue(varName, varCpy);

    assertEquals(SIZEOF_DOUBLE * varCpy.length, component.getVarNbytes(varName));
  }

  @Test
  public final void testGetVarLocation() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    assertEquals(varLocation, component.getVarLocation(varName));
  }

}
