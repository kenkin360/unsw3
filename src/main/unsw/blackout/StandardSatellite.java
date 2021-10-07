package unsw.blackout;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class StandardSatellite extends Satellite {

    private double maxRange = 150000;
    private double linearVelocity = 2500;
    private Angle radianChange = Angle.fromRadians(linearVelocity / RADIUS_OF_JUPITER);
    
    /**
     * Constructor for StandardSatellite.
     * @param satelliteId
     * @param type
     * @param height
     * @param position
     */
    public StandardSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, "StandardSatellite", height, position);
        //TODO Auto-generated constructor stub
    }

    /**
     * Fast forward the angular position of StandardSatellite.
     */
    @Override
    public void updatePosition() {
        // TODO Auto-generated method stub
        Angle oldPosition = super.getPosition();
        Angle newPosition = radianChange.add(oldPosition);
        super.setPosition(newPosition);
    }

    /**
     * Display all information of StandardSatellite.
     */
    @Override
    public EntityInfoResponse getSatelliteInfo() {
        // TODO Auto-generated method stub
        String id = super.getId();
        Angle position = super.getPosition();
        double height = super.getHeight();
        String type = super.getType();

        List<File> listFiles = super.getFileList();
        String filename;
        String data;
        int fileSize;
        boolean hasTransferCompleted = true;

        FileInfoResponse fileInfo;
        EntityInfoResponse satelliteInfo = null;
        /** 
         * Check if the files are added to the device or not.
         * If no, then display the information without the information of files.
         * Otherwise, add all information of the files to the device.
         */
        if (listFiles.size() == 0) {
            satelliteInfo = new EntityInfoResponse(id, position, height, type);
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
            satelliteInfo = new EntityInfoResponse(id, position, height, type, mapFiles);
        }
        return satelliteInfo;
    }

    /**
     * List all possible objects that StandardSatellite can communicate with in the certain range.
     */
    @Override
    public List<String> updateListOfCommunicableEntities(BlackoutController controller) {
        // TODO Auto-generated method stub
        List<String> listCommunicableEntities = new ArrayList<String>();
        List<Device> deviceList = new ArrayList<Device>();
        List<Satellite> satelliteList = new ArrayList<Satellite>();
        deviceList = controller.getDeviceList();
        satelliteList = controller.getSatelliteList();
        for (Satellite satellite : satelliteList) {
            if (satellite.getId().equals(super.getId())) {
                // Skip the targeted satellite itself
            }
            else {
                if (checkSatelliteDistance(satellite) == true && checkSatelliteVisibilty(satellite) == true) {
                    listCommunicableEntities.add(satellite.getId());
                }
            }
        }
        for (Device device : deviceList) {
            if (device.getType().equals("HandheldDevice") || device.getType().equals("LaptopDevice")) {
                if (checkDeviceDistance(device) == true && checkDeviceVisibilty(device) == true) {
                    listCommunicableEntities.add(device.getId());
                }
            }
        }
        return listCommunicableEntities;
    }
    
    // Helper functions

    /**
    * Check if the satellite is in the range of the chosen satellite.
    * @param satellite
    * @return true if it is in range, otherwise false.
    */
    public boolean checkSatelliteDistance(Satellite satellite) {
        if (MathsHelper.getDistance(super.getHeight(), super.getPosition(), satellite.getHeight(), satellite.getPosition()) <= maxRange) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
    * Check if the satellite is visible from the chosen satellite.
    * @param satellite
    * @return true if it is visible, otherwise false.
    */
    public boolean checkSatelliteVisibilty(Satellite satellite) {
        if (MathsHelper.isVisible(super.getHeight(), super.getPosition(), satellite.getHeight(), satellite.getPosition())) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
    * Check if the device is in the range of the satellite.
    * @param device
    * @return true if it is in range, otherwise false.
    */
    public boolean checkDeviceDistance(Device device) {
        if (MathsHelper.getDistance(super.getHeight(), super.getPosition(), device.getPosition()) <= maxRange) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
    * Check if the device is visible from the satellite.
    * @param device
    * @return true if it is visible, otherwise false.
    */
    public boolean checkDeviceVisibilty(Device device) {
        if (MathsHelper.isVisible(super.getHeight(), super.getPosition(), device.getPosition())) {
            return true;
        }
        else {
            return false;
        }
    }
}
