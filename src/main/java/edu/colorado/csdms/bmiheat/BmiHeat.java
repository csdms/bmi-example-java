/**
 * Basic Model Interface implementation for the 2D heat model.
 */
package edu.colorado.csdms.bmiheat;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.colorado.csdms.bmi.BMI;
import edu.colorado.csdms.heat.Heat;

/**
 * BMI methods that wrap the {@link edu.colorado.csdms.heat.Heat} class.
 *
 * @author mpiper
 * @version $Id: $Id
 */
public class BmiHeat implements BMI {
  
  /** Constant <code>MODEL_NAME="The 2D Heat equation"</code> */
  public static final String MODEL_NAME = "The 2D Heat equation"; 
  /** Constant <code>INPUT_VAR_NAMES="{plate_surface__temperature}"</code> */
  public static final String[] INPUT_VAR_NAMES = 
    {"plate_surface__temperature"};
  /** Constant <code>OUTPUT_VAR_NAMES="{plate_surface__temperature}"</code> */
  public static final String[] OUTPUT_VAR_NAMES = 
    {"plate_surface__temperature"};
  
  private Heat model;
  private HashMap<String, String> varUnits;
  private HashMap<Integer, String> grids;
  private HashMap<Integer, String> gridType;
  
  /**
   * Creates a new BmiHeat model that is ready for initialization.
   */
  public BmiHeat() {
    model = null;
    varUnits = new HashMap<String, String>();
    grids = new HashMap<Integer, String>();
    gridType = new HashMap<Integer, String>();
  }
  
  /** {@inheritDoc} */
  @Override
  public void initialize(String configFile) {
    File theFile = new File(configFile);
    if (theFile.exists()) {
      model = new Heat(configFile);
      initializeHelper();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void initialize() {
    model = new Heat();
    initializeHelper();
  }

  /**
   * Initializes BmiHeat properties using properties from the enclosed Heat
   * instance.
   */
  private void initializeHelper() {
    varUnits.put(INPUT_VAR_NAMES[0], "K");
    grids.put(0, INPUT_VAR_NAMES[0]);
    gridType.put(0, "uniform_rectilinear_grid");
  }
  
  /**
   * A helper that converts a 2D array of doubles into a 1D array.
   *
   * @param array2D a 2D array of doubles
   * @return a 1D array of doubles
   */
  protected double[] flattenArray2D(double[][] array2D) {
	int nRows = array2D.length, nCols = array2D[0].length;
	double[] array1D = new double[(nRows*nCols)];
      for ( int i = 0; i < nRows; i++ ) {
	    System.arraycopy(array2D[i], 0, array1D, (i*nCols), nCols);
      }
    return array1D;
  }

  /**
   * A helper that converts a 1D array of doubles into a 2D array using the
   * shape of the model grid.
   *
   * @param array1D a 1D array of doubles
   * @param nRows the desired number of rows of the 2D array
   * @param nCols the desired number of columns of the 2D array
   * @return a 2D array of doubles
   */
  protected double[][] unflattenArray2D(double[] array1D, int nRows, int nCols) {
    double[][] array2D = new double[nRows][nCols];
    for (int i = 0; i < nRows; i++) {
        System.arraycopy(array1D, (i*nCols), array2D[i], 0, nCols);
    }
    return array2D;
  }

  /** {@inheritDoc} */
  @Override
  public void update() {
    model.advanceInTime();
  }

  /** {@inheritDoc} */
  @Override
  public void updateUntil(double then) {
    Double nSteps = (then - getCurrentTime()) / getTimeStep();
    for (int i = 0; i < Math.floor(nSteps); i++) {
      update();
    }
    updateFrac(nSteps - Math.floor(nSteps));
  }

  /** {@inheritDoc} */
  @Override
  public void updateFrac(double timeFrac) {
    double timeStep = getTimeStep();
    model.setTimeStep(timeFrac * timeStep);
    update();
    model.setTimeStep(timeStep);
  }

  /** {@inheritDoc} */
  @Override
  public void finalize() {
    // Nothing to do.
  }

  /** {@inheritDoc} */
  @Override
  public String getComponentName() {
    return MODEL_NAME;
  }

  /** {@inheritDoc} */
  @Override
  public String[] getInputVarNames() {
    return INPUT_VAR_NAMES;
  }

  /** {@inheritDoc} */
  @Override
  public int getInputVarNameCount() {
    return INPUT_VAR_NAMES.length;
  }

  /** {@inheritDoc} */
  @Override
  public String[] getOutputVarNames() {
    return OUTPUT_VAR_NAMES;
  }

  /** {@inheritDoc} */
  @Override
  public int getOutputVarNameCount() {
    return OUTPUT_VAR_NAMES.length;
  }

  /** {@inheritDoc} */
  @Override
  public double getStartTime() {
    return 0;
  }

  /** {@inheritDoc} */
  @Override
  public double getCurrentTime() {
    return model.getTime();
  }

  /** {@inheritDoc} */
  @Override
  public double getEndTime() {
    return Double.MAX_VALUE;
  }

  /** {@inheritDoc} */
  @Override
  public double getTimeStep() {
    return model.getTimeStep();
  }

  /** {@inheritDoc} */
  @Override
  public String getTimeUnits() {
    return null; // Not implemented for Heat
  }

  /** {@inheritDoc} */
  @Override
  public String getVarType(String varName) {
    return values.get(varName).getClass().getName();
  }

  /** {@inheritDoc} */
  @Override
  public String getVarUnits(String varName) {
    return varUnits.get(varName);
  }

  /** {@inheritDoc} */
  @Override
  public int getVarItemsize(String varName) {
    int itemSize = 0;
    if (getVarType(varName).equals("[D")) {
      itemSize = 8;
    }
    return itemSize;
  }

  /** {@inheritDoc} */
  @Override
  public int getVarNbytes(String varName) {
    return getVarItemsize(varName) * values.get(varName).length;
  }

  /** {@inheritDoc} */
  @Override
  public int getVarGrid(String varName) {
    for (Map.Entry<Integer, String> entry : grids.entrySet()) {
      if (entry.getValue().equals(varName)) {
        return entry.getKey();
      }
    }
    return -1;
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public double[] getValue(String varName) {
	return flattenArray2D(model.getTemperature());
  }

  /** {@inheritDoc} */
  @Override
  public <T> T getValueRef(String varName) {
    return null; // Not implemented
  }

  /** {@inheritDoc} */
  @Override
  public <T> T getValueAtIndices(String varName, int[] indices) {
    return null; // Not implemented
  }

  /** {@inheritDoc} */
  @Override
  public int[] getGridShape(int gridId) {
    List<Integer> shapeAsList = model.getShape();
    int[] shapeAsArray = new int[shapeAsList.size()];
    for (int i = 0; i < shapeAsArray.length; i++) {
      shapeAsArray[i] = shapeAsList.get(i);
    }
    return shapeAsArray;
  }

  /** {@inheritDoc} */
  @Override
  public double[] getGridX(int gridId) {
    return null; // Not implemented for Heat
  }

  /** {@inheritDoc} */
  @Override
  public double[] getGridY(int gridId) {
    return null; // Not implemented for Heat
  }

  /** {@inheritDoc} */
  @Override
  public double[] getGridZ(int gridId) {
    return null; // Not implemented for Heat
  }

  /** {@inheritDoc} */
  @Override
  public int getGridRank(int gridId) {
    List<Integer> shapeAsList = model.getShape();
    return shapeAsList.size();
  }

  /** {@inheritDoc} */
  @Override
  public int getGridSize(int gridId) {
    List<Integer> shapeAsList = model.getShape();
    int product = 1;
    for (int i = 0; i < shapeAsList.size(); i++) {
      product *= shapeAsList.get(i);
    }
    return product;
  }

  /** {@inheritDoc} */
  @Override
  public String getGridType(int gridId) {
    return gridType.get(gridId);
  }

  /** {@inheritDoc} */
  @Override
  public double[] getGridSpacing(int gridId) {
    List<Double> spacingAsList = model.getSpacing();
    double[] spacingAsArray = new double[spacingAsList.size()];
    for (int i = 0; i < spacingAsArray.length; i++) {
      spacingAsArray[i] = spacingAsList.get(i);
    }
    return spacingAsArray;
  }

  /** {@inheritDoc} */
  @Override
  public double[] getGridOrigin(int gridId) {
    List<Double> originAsList = model.getOrigin();
    double[] originAsArray = new double[originAsList.size()];
    for (int i = 0; i < originAsArray.length; i++) {
      originAsArray[i] = originAsList.get(i);
    }
    return originAsArray;
  }

  /** {@inheritDoc} */
  @Override
  public int[] getGridConnectivity(int gridId) {
    return null; // Not implemented for Heat
  }

  /** {@inheritDoc} */
  @Override
  public int[] getGridOffset(int gridId) {
    return null; // Not implemented for Heat
  }

  /** {@inheritDoc} */
  @Override
  public void setValue(String varName, double[] src) {
    int nRows = getGridShape(getVarGrid(varName))[0];
    int nCols = getGridShape(getVarGrid(varName))[1];
    model.setTemperature(unflattenArray2D(src, nRows, nCols));
  }

  /** {@inheritDoc} */
  @Override
  public void setValue(String varName, int[] src) {
    return; // Not implemented for Heat
  }

  /** {@inheritDoc} */
  @Override
  public void setValue(String varName, String[] src) {
    return; // Not implemented for Heat
  }

  /** {@inheritDoc} */
  @Override
  public void setValueAtIndices(String varName, int[] indices, double[] src) {
    return; // Not implemented for Heat
  }

  /** {@inheritDoc} */
  @Override
  public void setValueAtIndices(String varName, int[] indices, int[] src) {
    return; // Not implemented for Heat
  }

  /** {@inheritDoc} */
  @Override
  public void setValueAtIndices(String varName, int[] indices, String[] src) {
    return; // Not implemented for Heat
  }

  public static void main(String[] args) {
    System.out.println("*\n* Example: Heat Model run through its BMI\n*");

    // Instantiate and initialize model.
    BmiHeat bmi = new BmiHeat();
    bmi.initialize("src/test/resources/data/heat.xml");

    String componentName = bmi.getComponentName();
    System.out.println("Model name: " + componentName);

    System.out.println("Start time: " + bmi.getStartTime());
    System.out.println("End time: " + bmi.getEndTime());
    System.out.println("Current time: " + bmi.getCurrentTime());
    System.out.println("Time step: " + bmi.getTimeStep());
    System.out.println("Time unit: " + bmi.getTimeUnits());

    System.out.println("Input variables: ");
    for (int i = 0; i < bmi.getInputVarNameCount(); i++) {
      System.out.println("- " + bmi.getInputVarNames()[i]);
    }
    System.out.println("Output variables: ");
    for (int i = 0; i < bmi.getOutputVarNameCount(); i++) {
      System.out.println("- " + bmi.getOutputVarNames()[i]);
    }

    // Get the grid and variable info for the temperature variable.
    String var_name = bmi.getOutputVarNames()[0];
    System.out.println("Variable: " + var_name);
    Integer grid_id = bmi.getVarGrid(var_name);
    System.out.println("- grid_id: " + grid_id);
    System.out.println("- grid type: " + bmi.getGridType(grid_id));
    System.out.println("- grid rank: " + bmi.getGridRank(grid_id));
    System.out.println("- grid size: " + bmi.getGridSize(grid_id));
    System.out.println("- grid shape:");
    int[] shape = bmi.getGridShape(grid_id);
    for (int i = 0; i < bmi.getGridRank(grid_id); i++) {
      System.out.println("  - " + shape[i]);
    }
    System.out.println("- grid spacing:");
    for (int i = 0; i < bmi.getGridRank(grid_id); i++) {
      System.out.println("  - " + bmi.getGridSpacing(grid_id)[i]);
    }
    System.out.println("- grid origin:");
    for (int i = 0; i < bmi.getGridRank(grid_id); i++) {
      System.out.println("  - " + bmi.getGridOrigin(grid_id)[i]);
    }
    System.out.println("- var type: " + bmi.getVarType(var_name));
    System.out.println("- var units: " + bmi.getVarUnits(var_name));
    System.out.println("- var itemsize: " + bmi.getVarItemsize(var_name));
    System.out.println("- var nbytes: " + bmi.getVarNbytes(var_name));

    // Add an impulse to the default initial temperature field.
    double[] temp0 = bmi.getValue(var_name);
    temp0[3*shape[1] + 2] = 100.0;
    bmi.setValue(var_name, temp0);

    // Advance the model over several time steps.
    Double currentTime = bmi.getCurrentTime();
    while (currentTime < 1.0) {
      System.out.println("time = " + currentTime.toString());
      System.out.println("temperature =");
      double[] temp = bmi.getValue(var_name);
      for (int j = 0; j < shape[0]; j++) {
        for (int i = 0; i < shape[1]; i++) {
          System.out.format("%7.2f", temp[j*shape[1] + i]);
        }
        System.out.print("\n");
      }
      bmi.update();
      currentTime = bmi.getCurrentTime();
    }

    bmi.finalize();
  }
}
