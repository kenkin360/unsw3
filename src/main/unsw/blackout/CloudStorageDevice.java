package unsw.blackout;

import java.util.List;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public class CloudStorageDevice extends Device {

    /**
     * 
     */
    public CloudStorageDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position);
        //TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    @Override
    public EntityInfoResponse getDeviceInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 
     */
    @Override
    public List<String> updateListOfCommunicableEntities(BlackoutController controller) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
