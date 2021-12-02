package edu.colorado.csdms.bmiheat;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the getter and setter methods of the {@link BmiHeat} class.
 */
public class TestGetAndSetValue {

  private Double delta; // maximum difference to be considered equal
  private String varName;
  private Integer gridSize;
  private Double initialTemp;

  @Before
  public void setUp() throws Exception {
    delta = 0.1;
    varName = "plate_surface__temperature";
    gridSize = 48;
    initialTemp = 0.0;
  }

  @After
  public void tearDown() throws Exception {
  }

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

  @Test
  public final void testGetValuePtr() {
    return; // Not implemented
  }

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
    assertEquals(initialTemp, varCpy[0], delta);
    assertEquals(initialTemp, varCpy[gridSize - 1], delta);
  }

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

  @Test
  public final void testSetValueAtIndices() {
    return; // Not implemented
  }
}
