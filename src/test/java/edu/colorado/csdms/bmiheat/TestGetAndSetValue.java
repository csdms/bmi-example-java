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
  private static final int NROWS = 4;
  private static final int NCOLS = 3;

  private Double delta; // maximum difference to be considered equal
  private String varName;
  private String varUnits;
  private String varType;
  private Double initialTempMin;
  private Double initialTempMax;
  private double[] array1D = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
  private double[][] array2D = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    delta = 0.1;
    varName = "plate_surface__temperature";
    varUnits = "K";
    varType = "double";
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
    double[] varCpy = component.getValue(varName);
    assertEquals(SIZEOF_DOUBLE * varCpy.length, component.getVarNbytes(varName));
  }

  /**
   * Test method for {@link BmiHeat#getValue(java.lang.String)}.
   */
  @Test
  public final void testGetValue() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    double[] varCpy1 = component.getValue(varName);
    double[] varCpy2 = component.getValue(varName);

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

    double[] varCpy = component.getValue(varName);

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

    double[] varRef = component.getValueRef(varName);
    double[] varNew1 = new double[varRef.length];
    varNew1[0] = 5.0;

    component.setValue(varName, varNew1);

    double[] varNew2 = component.getValueRef(varName);

    assertEquals(varRef, varNew2);
    assertNotSame(varNew1, varNew2);
    assertArrayEquals(varNew2, varNew1, delta);
  }

  /**
   * Test method for {@link BmiHeat#setValueAtIndices(String, int[], Object)}.
   */
  @Test
  public final void testSetValueAtIndices() {
    return; // Not implemented
  }

  /**
   * Test that a flattened 2D array can be redimensionalized with
   * {@link BmiHeat#unflattenArray2D(double[], int, int)}.
   */
  @Test
  public final void testUnflatten2dArray() {
    BmiHeat component = new BmiHeat();

    double[][] new2D = new double[NROWS][NCOLS];
    new2D = component.unflattenArray2D(array1D, NROWS, NCOLS);
    assertArrayEquals(array2D[0], new2D[0], delta);
  }

  /**
   * Test that a 2D array can be flattened with
   * {@link BmiHeat#flattenArray2D(double[][])}.
   */
  @Test
  public final void testFlatten2dArray() {
    BmiHeat component = new BmiHeat();

    double[] new1D = new double[NROWS*NCOLS];
    new1D = component.flattenArray2D(array2D);
    assertArrayEquals(array1D, new1D, delta);
  }
}
