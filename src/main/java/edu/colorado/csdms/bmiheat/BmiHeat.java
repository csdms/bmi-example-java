/**
 * Basic Model Interface implementation for the 2D heat model.
 */
package edu.colorado.csdms.bmiheat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import edu.colorado.csdms.bmi.BMI;
import edu.colorado.csdms.heat.Heat;

/**
 * BMI methods that wrap the {@link edu.colorado.csdms.heat.Heat} class.
 */
public class BmiHeat implements BMI {

  public static final String MODEL_NAME = "Heat"; 
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

  /*
   * Model control functions
   */

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
    gridType.put(0, "uniform_rectilinear");
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

  /**
   * A helper for updating a model to a fractional time step.
   * @param timeFrac
   */
  private void updateFrac(double timeFrac) {
    double timeStep = getTimeStep();
    model.setTimeStep(timeFrac * timeStep);
    update();
    model.setTimeStep(timeStep);
  }

  @Override
  public void finalize() {
    model = null;
  }

  /*
   * Model information functions
   */

  @Override
  public String getComponentName() {
    return MODEL_NAME;
  }

  @Override
  public int getInputItemCount() {
    return INPUT_VAR_NAMES.length;
  }

  @Override
  public int getOutputItemCount() {
    return OUTPUT_VAR_NAMES.length;
  }

  @Override
  public String[] getInputVarNames() {
    return INPUT_VAR_NAMES;
  }

  @Override
  public String[] getOutputVarNames() {
    return OUTPUT_VAR_NAMES;
  }

  /*
   * Variable information functions
   */

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
  public String getVarLocation(String varName) {
    return "node";
  }

  /*
   * Time functions
   */

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
  public String getTimeUnits() {
    return null; // Not implemented for Heat
  }

  @Override
  public double getTimeStep() {
    return model.getTimeStep();
  }

  /*
   * Getters and setters
   */

  @Override
  public void getValue(String varName, double[] dest) {
    int nRows = model.getShape().get(0);
    int nCols = model.getShape().get(1);
    for (int i = 0; i < nRows; i++) {
      System.arraycopy(model.getTemperature()[i], 0, dest, (i*nCols), nCols);
    }
  }

  @Override
  public void getValue(String varName, int[] dest) {
    return; // Not implemented for Heat
  }

  @Override
  public void getValue(String varName, String[] dest) {
    return; // Not implemented for Heat
  }

  @Override
  public <T> T getValuePtr(String varName) {
    return null; // Not implemented for Heat
  }

  @Override
  public void getValueAtIndices(String varName, double[] dest, int[] indices) {
    return; // Not implemented
  }

  @Override
  public void getValueAtIndices(String varName, int[] dest, int[] indices) {
    return; // Not implemented
  }

  @Override
  public void getValueAtIndices(String varName, String[] dest, int[] indices) {
    return; // Not implemented
  }

  @Override
  public void setValue(String varName, double[] src) {
    int nRows = model.getShape().get(0);
    int nCols = model.getShape().get(1);
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

  /*
   * Model grid functions
   */

  @Override
  public int getGridRank(int gridId) {
    return model.getShape().size();
  }

  @Override
  public int getGridSize(int gridId) {
    int product = 1;
    for (int i = 0; i < model.getShape().size(); i++) {
      product *= model.getShape().get(i);
    }
    return product;
  }

  @Override
  public String getGridType(int gridId) {
    return gridType.get(gridId);
  }

  @Override
  public void getGridShape(int gridId, int[] gridShape) {
    for (int i = 0; i < model.getShape().size(); i++) {
      gridShape[i] = model.getShape().get(i);
    }
  }

  @Override
  public void getGridSpacing(int gridId, double[] gridSpacing) {
    for (int i = 0; i < model.getSpacing().size(); i++) {
      gridSpacing[i] = model.getSpacing().get(i);
    }
  }

  @Override
  public void getGridOrigin(int gridId, double[] gridOrigin) {
    for (int i = 0; i < model.getOrigin().size(); i++) {
      gridOrigin[i] = model.getOrigin().get(i);
    }
  }

  @Override
  public void getGridX(int gridId, double[] gridX) {
    return; // Not implemented for Heat
  }

  @Override
  public void getGridY(int gridId, double[] gridY) {
    return; // Not implemented for Heat
  }

  @Override
  public void getGridZ(int gridId, double[] gridZ) {
    return; // Not implemented for Heat
  }

  @Override
  public int getGridNodeCount(int gridId) {
    return -1; // Not implemented for Heat
  }
  
  @Override
  public int getGridEdgeCount(int gridId) {
    return -1; // Not implemented for Heat
  }
  
  @Override
  public int getGridFaceCount(int gridId) {
    return -1; // Not implemented for Heat
  }

  @Override
  public void getGridEdgeNodes(int gridId, int[] edgeNodes) {
    return; // Not implemented for Heat
  }
  
  @Override
  public void getGridFaceEdges(int gridId, int[] faceEdges) {
    return; // Not implemented for Heat  
  }
  
  @Override
  public void getGridFaceNodes(int gridId, int[] faceNodes) {
    return; // Not implemented for Heat  
  }
  
  @Override
  public void getGridNodesPerFace(int gridId, int[] nodesPerFace) {
    return; // Not implemented for Heat  
  }


  /**
   * An example of running BmiHeat.
   * @param args
   */
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
    for (int i = 0; i < bmi.getInputItemCount(); i++) {
      System.out.println("- " + bmi.getInputVarNames()[i]);
    }
    System.out.println("Output variables: ");
    for (int i = 0; i < bmi.getOutputItemCount(); i++) {
      System.out.println("- " + bmi.getOutputVarNames()[i]);
    }

    // Get the grid and variable info for the temperature variable.
    String var_name = bmi.getOutputVarNames()[0];
    System.out.println("Variable: " + var_name);
    Integer gridId = bmi.getVarGrid(var_name);
    System.out.println("- grid_id: " + gridId);
    System.out.println("- grid type: " + bmi.getGridType(gridId));
    int gridRank = bmi.getGridRank(gridId);
    System.out.println("- grid rank: " + gridRank);
    System.out.println("- grid size: " + bmi.getGridSize(gridId));
    System.out.println("- grid shape:");
    int[] gridShape = new int[gridRank];
    bmi.getGridShape(gridId, gridShape);
    for (int i = 0; i < gridRank; i++) {
      System.out.println("  - " + gridShape[i]);
    }
    System.out.println("- grid spacing:");
    double[] gridSpacing = new double[gridRank];
    bmi.getGridSpacing(gridId, gridSpacing);
    for (int i = 0; i < gridRank; i++) {
      System.out.println("  - " + gridSpacing[i]);
    }
    System.out.println("- grid origin:");
    double[] gridOrigin = new double[gridRank];
    bmi.getGridOrigin(gridId, gridOrigin);
    for (int i = 0; i < gridRank; i++) {
      System.out.println("  - " + gridOrigin[i]);
    }
    System.out.println("- var type: " + bmi.getVarType(var_name));
    System.out.println("- var units: " + bmi.getVarUnits(var_name));
    System.out.println("- var itemsize: " + bmi.getVarItemsize(var_name));
    System.out.println("- var nbytes: " + bmi.getVarNbytes(var_name));

    // Add an impulse to the default initial temperature field.
    double[] temp0 = new double[bmi.getGridSize(gridId)];
    bmi.getValue(var_name, temp0);
    temp0[3*gridShape[1] + 2] = 100.0;
    bmi.setValue(var_name, temp0);

    // Advance the model over several time steps.
    Double currentTime = bmi.getCurrentTime();
    double[] temp = new double[bmi.getGridSize(gridId)];
    while (currentTime < 1.0) {
      System.out.println("time = " + currentTime.toString());
      System.out.println("temperature =");
      bmi.getValue(var_name, temp);
      for (int j = 0; j < gridShape[0]; j++) {
        for (int i = 0; i < gridShape[1]; i++) {
          System.out.format("%7.2f", temp[j*gridShape[1] + i]);
        }
        System.out.print("\n");
      }
      bmi.update();
      currentTime = bmi.getCurrentTime();
    }

    bmi.finalize();
  }
}
