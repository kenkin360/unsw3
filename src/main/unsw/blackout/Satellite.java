package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public abstract class Satellite {
    
    private String satelliteId;
    private String type;
    private double height;
    private Angle position;
    private List<File> fileList = new ArrayList<File>();

    /**
     * Constructor for a satellite.
     * @param satelliteId
     * @param type
     * @param height
     * @param position
     */
    public Satellite (String satelliteId, String type, double height, Angle position) {
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;
    }

    /**
     * getter for satellite name.
     * @return
     */
    public String getSatelliteId() {
        return this.satelliteId;
    }

    /**
     * setter for satellite name.
     * @param satelliteId
     */
    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    /**
     * getter for satellite type.
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     * setter for satellite type.
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter for satellite height.
     * @return
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * setter for satellite height.
     * @param height
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * getter for satellite angular position.
     * @return
     */
    public Angle getPosition() {
        return this.position;
    }

    /**
     * setter for satellite angular position.
     * @param position
     */
    public void setPosition(Angle position) {
        this.position = position;
    }

    /**
     * Add a file to the satellite.
     * @param file
     */
    public void addFile(File file) {
        fileList.add(file);
    }

    /**
     * list all the files in the satellite.
     * @return a list of files.
     */
    public List<File> getFileList() {
        return fileList;
    }

    /**
     * Display all information of the satellite.
     * @return a map of all details of the satellite.
     */
    public abstract EntityInfoResponse getSatelliteInfo();
    
    /**
     * Fast forward the angular position of the satellite.
     */
    public abstract void updatePosition();
    
    /**
     * List all the objects that the satellite can communicate with in the certain range.
     * @param controller
     * @return a list of string for all possible objects are visible from the chosen satellite.
     */
    public abstract List<String> updateListOfCommunicableEntities(BlackoutController controller);
}
