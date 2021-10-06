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
     * 
     */     
    public Device (String deviceId, String type, Angle position) {
        this.deviceId = deviceId;
        this.type = type;
        this.position = position;
    }

    /**
     * 
     */
    public String getDeviceId() {
        return this.deviceId;
    }

    /**
     * 
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
    public abstract EntityInfoResponse getDeviceInfo();

    /**
     * 
     */
    public abstract List<String> updateListOfCommunicableEntities(BlackoutController controller);
}
