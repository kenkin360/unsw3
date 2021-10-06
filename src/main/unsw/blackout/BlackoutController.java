package unsw.blackout;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {

    List<Device> devices = new ArrayList<Device>();
    List<Satellite> satellites = new ArrayList<Satellite>();

    /**
     * Create a new device to the Blackout system.
     */
    public void createDevice(String deviceId, String type, Angle position) {
        // TODO: Task 1a)
        if (type == "HandheldDevice") {
            devices.add(new HandheldDevice(deviceId, type, position));
        }
        else if (type == "LaptopDevice") {
            devices.add(new LaptopDevice(deviceId, type, position));
        }
        else if (type == "DesktopDevice") {
            devices.add(new DesktopDevice(deviceId, type, position));
        }        
    }

    /**
     * remove the targeted device.
     */
    public void removeDevice(String deviceId) {
        Iterator<Device> itr = devices.iterator();
        // TODO: Task 1b)
        while(itr.hasNext()) {
            Device device = itr.next(); 
            if (device.getDeviceId().equals(deviceId)) { 
                itr.remove(); 
            } 
        }
    }

    /**
     * Create a new satellite to the Blackout system.
     */
    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        // TODO: Task 1c)
        if (type == "StandardSatellite") {
            satellites.add(new StandardSatellite(satelliteId, type, height, position));
        }
        else if (type == "ShrinkingSatellite") {
            satellites.add(new ShrinkingSatellite(satelliteId, type, height, position));
        }
        else if (type == "RelaySatellite") {
            satellites.add(new RelaySatellite(satelliteId, type, height, position));
        }
        else {
            satellites.add(new ElephantSatellite(satelliteId, type, height, position));
        }
    }

    /**
     * remove the targeted satellite.
     */
    public void removeSatellite(String satelliteId) {
        // TODO: Task 1d)
        Iterator<Satellite> itr = satellites.iterator();
        while(itr.hasNext()) {
            Satellite satellite = itr.next();
            if (satellite.getSatelliteId().equals(satelliteId)) {
                itr.remove();
            }
        }
    }

    /**
     * List the name of all existing devices.
     */
    public List<String> listDeviceIds() {
        // TODO: Task 1e)
        List<String> deviceList = new ArrayList<String>();
        for (Device device : devices) {
            deviceList.add(device.getDeviceId());
        }
        return deviceList;
    }

    /**
     * List the name of all existing satellites.
     */
    public List<String> listSatelliteIds() {
        // TODO: Task 1f)
        List<String> satelliteList = new ArrayList<String>();
        for (Satellite satellite : satellites) {
            satelliteList.add(satellite.getSatelliteId());
        }
        return satelliteList;
    }

    /** 
     * Add a new file with the given name and contents to the selected device.
     */
    public void addFileToDevice(String deviceId, String filename, String content) {
        // TODO: Task 1g)
        File file = new File(filename, content);
        for (Device device : devices) {
            if (device.getDeviceId().equals(deviceId)) { 
                device.addFile(file);
            }
        }
    }

    /** 
     * Display all information of the selected object.
     */
    public EntityInfoResponse getInfo(String id) {
        // TODO: Task 1h)
        EntityInfoResponse mapInfo = null;

        for (Device device : devices) {
            if (device.getDeviceId().equals(id)) {
                mapInfo = device.getDeviceInfo();
            }
        }

        for (Satellite satellite : satellites) {
            if (satellite.getSatelliteId().equals(id)) {
                mapInfo = satellite.getSatelliteInfo();
            }
        }
        return mapInfo;
    }
    
    /**
     * Fast forward all the actions such as movement and transferring data in a certain period.
     */
    public void simulate() {
        // TODO: Task 2a)
        for (Satellite satellite : satellites) {
            satellite.updatePosition();
        }
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    /**
     * List all the objects which the selected object is communicable within its range.
     */
    public List<String> communicableEntitiesInRange(String id) {
        // TODO Task 2b)
        List<String> listCommunicableStrings = new ArrayList<String>();
        for (Satellite satellite : satellites) {
            if (satellite.getSatelliteId().equals(id)) {
                listCommunicableStrings = satellite.updateListOfCommunicableEntities(this);
            }
        }
        for (Device device : devices) {
            if (device.getDeviceId().equals(id)) {
                listCommunicableStrings = device.updateListOfCommunicableEntities(this);
            }
        }
        return listCommunicableStrings;
    }

    /**
     * Send the selected file from the selected object to the targeted object.
     */
    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        // TODO Task 2c)
        /** 
         * Given the sender Id, check if the selected file is stored in it or not.
         * If yes, then access the FileNotFoundException.
         */
        Map<String, FileInfoResponse> mapFile = getInfo(fromId).getFiles();
        for (Map.Entry<String, FileInfoResponse> entry : mapFile.entrySet()) {
            if (entry.getKey().equals(fileName)) {
                // The file is in the selected object.
            }
            else {
                try {
                    throw new FileNotFoundException(fileName);
                } 
                catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        /**
         * Given the receiver Id, check if the selected file already exists in it or not.
         * If yes, then access the FileAlreadyExistsException.
         */
        Map<String, FileInfoResponse> mapReceiverFile = getInfo(toId).getFiles();
        for (Map.Entry<String, FileInfoResponse> entry : mapReceiverFile.entrySet()) {
            if (entry.getKey().equals(fileName)) {
                try {
                    throw new FileAlreadyExistsException(fileName);
                } 
                catch (FileAlreadyExistsException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        for (Satellite satellite : satellites) {
            if (satellite.getSatelliteId().equals(toId)) {
                
            }
        }
    }
    
    // Helper Functions
    /** 
     * List all the existing devices the Blackout system.
     */
    public  List<Device> getDeviceList() {
        return devices;
    }

    /** 
     * List all the existing satellites in the Blackout system.
     */
    public  List<Satellite> getSatelliteList() {
        return satellites;
    }
}
