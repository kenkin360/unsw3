package unsw.blackout;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class DesktopDevice extends Device {

    private double maxRange = 200000;
    /**
     * Constructor for DesktopDevice.
     * @param deviceId
     * @param type
     * @param position
     */
    public DesktopDevice(String deviceId, String type, Angle position) {
        super(deviceId, "DesktopDevice", position);
        //TODO Auto-generated constructor stub
    }

    /**
     * Display all information of DesktopDevice.
     */
    @Override
    public EntityInfoResponse getDeviceInfo() {
        // TODO Auto-generated method stub
        String id = super.getId();
        Angle position = super.getPosition();
        double height = RADIUS_OF_JUPITER;
        String type = super.getType();
        
        List<File> listFiles = super.getFileList();
        String filename;
        String data;
        int fileSize;
        boolean hasTransferCompleted = true;

        FileInfoResponse fileInfo;
        EntityInfoResponse deviceInfo = null;
        /** 
         * Check if the files are added to the device or not.
         * If no, then display the information without the information of files.
         * Otherwise, add all information of the files to the device.
         */
        if (listFiles.size() == 0) {
            deviceInfo = new EntityInfoResponse(id, position, height, type);
        }
        else {
            Map<String, FileInfoResponse> mapFiles = new HashMap<String, FileInfoResponse>();
            Iterator<File> itr = listFiles.iterator();
            while(itr.hasNext()) {
                File file = itr.next();
                filename = file.getFilename();
                data = file.getContent();
                fileSize = data.length();
                fileInfo = new FileInfoResponse(filename, data, fileSize, hasTransferCompleted);
                mapFiles.put(filename, fileInfo);
            }
            deviceInfo = new EntityInfoResponse(id, position, height, type, mapFiles);
        }
        return deviceInfo;
    }

    /**
     * List all the satellites that DesktopDevice can communicate with in the certain range.
     */
    @Override
    public List<String> updateListOfCommunicableEntities(BlackoutController controller) {
        // TODO Auto-generated method stub
        List<String> listCommunicableEntities = new ArrayList<String>();
        List<Satellite> satelliteList = new ArrayList<Satellite>();
        satelliteList = controller.getSatelliteList();
        for (Satellite satellite : satelliteList) {
            if (satellite.getType().equals("ShrinkingSatellite") || satellite.getType().equals("RelaySatellite")) {
                if (checkDistance(satellite) == true && checkVisibilty(satellite) == true) {
                    listCommunicableEntities.add(satellite.getId());
                }
            } 
        }
        return listCommunicableEntities;
    }

    // Helper functions

    /**
     * Check if the satellite is in the range of the device.
     * @param satellite
     * @return true if it is in range, otherwise false.
     */
    public boolean checkDistance(Satellite satellite) {
        if (MathsHelper.getDistance(satellite.getHeight(), satellite.getPosition(), super.getPosition()) <= maxRange) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Check if the satellite is visible from the device.
     * @param satellite
     * @return true if it is visible, otherwise false.
     */
    public boolean checkVisibilty(Satellite satellite) {
        if (MathsHelper.isVisible(satellite.getHeight(), satellite.getPosition(), super.getPosition())) {
            return true;
        }
        else {
            return false;
        }
    }
}
