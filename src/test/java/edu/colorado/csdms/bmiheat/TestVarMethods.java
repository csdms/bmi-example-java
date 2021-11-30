package edu.colorado.csdms.bmiheat;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit tests for the getter and setter methods of the {@link BmiHeat} class.
 */
@Ignore
public class TestVarMethods {

  private static final int SIZEOF_DOUBLE = 8;

  private String varName;
  private String varUnits;
  private String varType;
  private Integer gridSize;

  @Before
  public void setUp() throws Exception {
    varName = "plate_surface__temperature";
    varUnits = "K";
    varType = "double";
    gridSize = 48;
  }

  @After
  public void tearDown() throws Exception {
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
}
