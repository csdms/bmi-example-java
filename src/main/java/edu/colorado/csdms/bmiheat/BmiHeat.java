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
 */
public class BmiHeat implements BMI {

  public static final String MODEL_NAME = "The 2D Heat equation"; 
  public static final String[] INPUT_VAR_NAMES = 
    {"plate_surface__temperature"};
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

  @Override
  public void initialize(String configFile) {
    File theFile = new File(configFile);
    if (theFile.exists()) {
      model = new Heat(configFile);
      initializeHelper();
    }
  }

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

  @Override
  public void update() {
    model.advanceInTime();
  }

  @Override
  public void updateUntil(double then) {
    Double nSteps = (then - getCurrentTime()) / getTimeStep();
    for (int i = 0; i < Math.floor(nSteps); i++) {
      update();
    }
    updateFrac(nSteps - Math.floor(nSteps));
  }

  @Override
  public void updateFrac(double timeFrac) {
    double timeStep = getTimeStep();
    model.setTimeStep(timeFrac * timeStep);
    update();
    model.setTimeStep(timeStep);
  }

  @Override
  public void finalize() {
    // Nothing to do.
  }

  @Override
  public String getComponentName() {
    return MODEL_NAME;
  }

  @Override
  public String[] getInputVarNames() {
    return INPUT_VAR_NAMES;
  }

  @Override
  public int getInputVarNameCount() {
    return INPUT_VAR_NAMES.length;
  }

  @Override
  public String[] getOutputVarNames() {
    return OUTPUT_VAR_NAMES;
  }

  @Override
  public int getOutputVarNameCount() {
    return OUTPUT_VAR_NAMES.length;
  }

  @Override
  public double getStartTime() {
    return 0;
  }

  @Override
  public double getCurrentTime() {
    return model.getTime();
  }

  @Override
  public double getEndTime() {
    return Double.MAX_VALUE;
  }

  @Override
  public double getTimeStep() {
    return model.getTimeStep();
  }

  @Override
  public String getTimeUnits() {
    return null; // Not implemented for Heat
  }

  @Override
  public String getVarType(String varName) {
    if (varName == getOutputVarNames()[0]) {
      if (model.getTemperature().getClass().getName().contains("D")) {
        return "double";
      }
    }
    return null;
  }

  @Override
  public String getVarUnits(String varName) {
    return varUnits.get(varName);
  }

  @Override
  public int getVarItemsize(String varName) {
    int itemSize = 0;
    if (getVarType(varName).equalsIgnoreCase("double")) {
      itemSize = 8;
    }
    return itemSize;
  }

  @Override
  public int getVarNbytes(String varName) {
    if (varName == getOutputVarNames()[0]) {
      return getVarItemsize(varName) * getGridSize(getVarGrid(varName));
    } else {
      return -1;
    }
  }

  @Override
  public int getVarGrid(String varName) {
    for (Map.Entry<Integer, String> entry : grids.entrySet()) {
      if (entry.getValue().equals(varName)) {
        return entry.getKey();
      }
    }
    return -1;
  }

  @Override
  public void getValue(String varName, double[] dest) {
    int nRows = getGridShape(getVarGrid(varName))[0];
    int nCols = getGridShape(getVarGrid(varName))[1];
    for (int i = 0; i < nRows; i++) {
      System.arraycopy(model.getTemperature()[i], 0, dest, (i*nCols), nCols);
    }
  }

  @Override
  public <T> T getValueRef(String varName) {
    return null; // Not implemented
  }

  @Override
  public <T> T getValueAtIndices(String varName, int[] indices) {
    return null; // Not implemented
  }

  @Override
  public int[] getGridShape(int gridId) {
    List<Integer> shapeAsList = model.getShape();
    int[] shapeAsArray = new int[shapeAsList.size()];
    for (int i = 0; i < shapeAsArray.length; i++) {
      shapeAsArray[i] = shapeAsList.get(i);
    }
    return shapeAsArray;
  }

  @Override
  public double[] getGridX(int gridId) {
    return null; // Not implemented for Heat
  }

  @Override
  public double[] getGridY(int gridId) {
    return null; // Not implemented for Heat
  }

  @Override
  public double[] getGridZ(int gridId) {
    return null; // Not implemented for Heat
  }

  @Override
  public int getGridRank(int gridId) {
    List<Integer> shapeAsList = model.getShape();
    return shapeAsList.size();
  }

  @Override
  public int getGridSize(int gridId) {
    List<Integer> shapeAsList = model.getShape();
    int product = 1;
    for (int i = 0; i < shapeAsList.size(); i++) {
      product *= shapeAsList.get(i);
    }
    return product;
  }

  @Override
  public String getGridType(int gridId) {
    return gridType.get(gridId);
  }

  @Override
  public double[] getGridSpacing(int gridId) {
    List<Double> spacingAsList = model.getSpacing();
    double[] spacingAsArray = new double[spacingAsList.size()];
    for (int i = 0; i < spacingAsArray.length; i++) {
      spacingAsArray[i] = spacingAsList.get(i);
    }
    return spacingAsArray;
  }

  @Override
  public double[] getGridOrigin(int gridId) {
    List<Double> originAsList = model.getOrigin();
    double[] originAsArray = new double[originAsList.size()];
    for (int i = 0; i < originAsArray.length; i++) {
      originAsArray[i] = originAsList.get(i);
    }
    return originAsArray;
  }

  @Override
  public int[] getGridConnectivity(int gridId) {
    return null; // Not implemented for Heat
  }

  @Override
  public int[] getGridOffset(int gridId) {
    return null; // Not implemented for Heat
  }

  @Override
  public void setValue(String varName, double[] src) {
    int nRows = getGridShape(getVarGrid(varName))[0];
    int nCols = getGridShape(getVarGrid(varName))[1];
    double[][] temperature = model.getTemperature();
    for (int i = 0; i < nRows; i++) {
      System.arraycopy(src, (i*nCols), temperature[i], 0, nCols);
    }
  }

  @Override
  public void setValue(String varName, int[] src) {
    return; // Not implemented for Heat
  }

  @Override
  public void setValue(String varName, String[] src) {
    return; // Not implemented for Heat
  }

  @Override
  public void setValueAtIndices(String varName, int[] indices, double[] src) {
    return; // Not implemented for Heat
  }

  @Override
  public void setValueAtIndices(String varName, int[] indices, int[] src) {
    return; // Not implemented for Heat
  }

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
    double[] temp0 = new double[bmi.getGridSize(grid_id)];
    bmi.getValue(var_name, temp0);
    temp0[3*shape[1] + 2] = 100.0;
    bmi.setValue(var_name, temp0);

    // Advance the model over several time steps.
    Double currentTime = bmi.getCurrentTime();
    double[] temp = new double[bmi.getGridSize(grid_id)];
    while (currentTime < 1.0) {
      System.out.println("time = " + currentTime.toString());
      System.out.println("temperature =");
      bmi.getValue(var_name, temp);
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
