package edu.colorado.csdms.bmiheat;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the getter and setter methods of the {@link BmiHeat} class.
 */
public class TestGetAndSetValue {

  private static final int SIZEOF_DOUBLE = 8;

  private Double delta; // maximum difference to be considered equal
  private String varName;
  private String varUnits;
  private String varType;
  private Integer gridSize;
  private Double initialTempMin;
  private Double initialTempMax;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    delta = 0.1;
    varName = "plate_surface__temperature";
    varUnits = "K";
    varType = "double";
    gridSize = 48;
    initialTempMin = 0.0;
    initialTempMax = 20.0;
  }

  /**
   * @throws java.lang.Exception
   */
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

  /**
   * Test method for {@link BmiHeat#getValue(java.lang.String)}.
   */
  @Test
  public final void testGetValue() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    double[] varCpy1 = new double[gridSize];
    double[] varCpy2 = new double[gridSize];

    component.getValue(varName, varCpy1);
    component.getValue(varName, varCpy2);

    assertNotSame(varCpy1, varCpy2);
    assertArrayEquals(varCpy1, varCpy2, delta);
  }

  /**
   * Test method for {@link BmiHeat#getValueAtIndices(java.lang.String, int[])}.
   */
  @Test
  public final void testGetValueAtIndices() {
    return; // Not implemented
  }

  /**
   * Test the initial temperature values set in the component.
   */
  @Test
  public final void testGetInitialValue() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    double[] varCpy = new double[gridSize];
    component.getValue(varName, varCpy);

    Arrays.sort(varCpy);
    assertTrue(varCpy[0] >= initialTempMin);
    assertTrue(varCpy[varCpy.length - 1] <= initialTempMax);
  }

  /**
   * Test method for {@link BmiHeat#setValue(String, Object)}.
   */
  @Test
  public final void testSetValue() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    double[] varNew1 = new double[gridSize];
    component.getValue(varName, varNew1);
    varNew1[0] = 5.0;

    component.setValue(varName, varNew1);

    double[] varNew2 = new double[gridSize];
    component.getValue(varName, varNew2);

    assertNotSame(varNew1, varNew2);
    assertArrayEquals(varNew1, varNew2, delta);
  }

  /**
   * Test method for {@link BmiHeat#setValueAtIndices(String, int[], Object)}.
   */
  @Test
  public final void testSetValueAtIndices() {
    return; // Not implemented
  }
}
