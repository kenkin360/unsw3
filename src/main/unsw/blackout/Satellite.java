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
     * 
     */
    public Satellite (String satelliteId, String type, double height, Angle position) {
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;
    }

    /**
     * 
     */
    public String getSatelliteId() {
        return this.satelliteId;
    }

    /**
     * 
     */
    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    /**
     * 
     */
    public String getType() {
        return this.type;
    }

    /**
     * 
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * 
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * 
     */
    public Angle getPosition() {
        return this.position;
    }

    /**
     * 
     */
    public void setPosition(Angle position) {
        this.position = position;
    }

    /**
     * 
     */
    public void addFile(File file) {
        fileList.add(file);
    }

    /**
     * 
     */
    public List<File> getFileList() {
        return fileList;
    }

    /**
     * 
     */
    public abstract EntityInfoResponse getSatelliteInfo();
    
    /**
     * 
     */
    public abstract void updatePosition();
    
    /**
     * 
     */
    public abstract List<String> updateListOfCommunicableEntities(BlackoutController controller);
}
