package edu.colorado.csdms.bmiheat;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the grid information methods of the {@link BmiHeat} class.
 */
public class TestGridMethods {
  
  private int gridId;
  private int[] shape;
  private int size;
  private String type;
  private double[] spacing;
  private double[] origin;
  private double delta;

  @Before
  public void setUp() throws Exception {
    gridId = 0;
    size = 48;
    type = "uniform_rectilinear";
    shape = new int[] {8, 6};
    spacing = new double[] {1.0, 1.0};
    origin = new double[] {0.0, 0.0};
    delta = 0.1;
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link BmiHeat#getGridRank(int)}.
   */
  @Test
  public final void testGetGridRank() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    assertEquals(shape.length, component.getGridRank(gridId));
  }

  /**
   * Test method for {@link BmiHeat#getGridSize(int)}.
   */
  @Test
  public final void testGetGridSize() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    assertEquals(size, component.getGridSize(gridId));
  }

  /**
   * Test method for {@link BmiHeat#getGridType(int)}.
   */
  @Test
  public final void testGetGridType() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    assertEquals(type, component.getGridType(gridId));
  }

  /**
   * Test method for {@link BmiHeat#getGridShape(int)}.
   */
  @Test
  public final void testGetGridShape() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    int[] gridShape = new int[shape.length];
    component.getGridShape(gridId, gridShape);

    assertArrayEquals(shape, gridShape);
  }

  /**
   * Test method for {@link BmiHeat#getGridSpacing(int)}.
   */
  @Test
  public final void testGetGridSpacing() {
    BmiHeat component = new BmiHeat();
    component.initialize();

    double[] gridSpacing = new double[spacing.length];
    component.getGridSpacing(gridId, gridSpacing);
    
    assertArrayEquals(spacing, gridSpacing, delta);
  }

  /**
   * Test method for {@link BmiHeat#getGridOrigin(int)}.
   */
  @Test
  public final void testGetGridOrigin() {
    BmiHeat component = new BmiHeat();
    component.initialize();
    
    double[] gridOrigin = new double[origin.length];
    component.getGridOrigin(gridId, gridOrigin);

    assertArrayEquals(origin, gridOrigin, delta);
  }

}
