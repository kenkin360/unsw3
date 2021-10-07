package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public abstract class Device {

    private String deviceId;
    private String type;
    private Angle position;
    private List<File> fileList = new ArrayList<File>();
    
    /**
     * Constructors for the device.
     * @param deviceId
     * @param type
     * @param position
     */     
    public Device (String deviceId, String type, Angle position) {
        this.deviceId = deviceId;
        this.type = type;
        this.position = position;
    }

    /**
     * getter for device name.
     * @return
     */
    public String getId() {
        return this.deviceId;
    }

    /**
     * getter for device type.
     * @return a string of device type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * getter for device angular position.
     * @return
     */
    public Angle getPosition() {
        return this.position;
    }

    /**
     * Add a file to the device.
     * @param file
     */
    public void addFile(File file) {
        fileList.add(file);
    }

    /**
     * list all the files in the device.
     * @return a list of files.
     */
    public List<File> getFileList() {
        return fileList;
    }

    /**
     * Display all information of the device.
     * @return a map of all details of the device.
     */
    public abstract EntityInfoResponse getDeviceInfo();

    /**
     * List all the satellites that the device can communicate with in the certain range.
     * @param controller
     * @return a list of string for all possible objects are visible from the chosen device.
     */
    public abstract List<String> updateListOfCommunicableEntities(BlackoutController controller);
}
