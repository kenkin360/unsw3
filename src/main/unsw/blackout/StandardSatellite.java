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

    /**
     * 
     */
    public StandardSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, "StandardSatellite", height, position);
        //TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    @Override
    public void updatePosition() {
        // TODO Auto-generated method stub
        Angle oldPosition = super.getPosition();
        Angle radianChange = Angle.fromRadians(2500 / RADIUS_OF_JUPITER);
        Angle newPosition = radianChange.add(oldPosition);
        super.setPosition(newPosition);
    }

    /**
     * 
     */
    @Override
    public EntityInfoResponse getSatelliteInfo() {
        // TODO Auto-generated method stub
        String id = super.getSatelliteId();
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
     * 
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
            if (satellite.getSatelliteId().equals(super.getSatelliteId())) {
                // Skip the targeted satellite itself
            }
            else {
                if (MathsHelper.isVisible(satellite.getHeight(), satellite.getPosition(), super.getHeight(), super.getPosition())) {
                    listCommunicableEntities.add(satellite.getSatelliteId());
                }
            }
        }
        for (Device device : deviceList) {
            if (device.getType().equals("HandheldDevice") || device.getType().equals("LaptopDevice")) {
                if (MathsHelper.isVisible(super.getHeight(), super.getPosition(), device.getPosition())) {
                    listCommunicableEntities.add(device.getDeviceId());
                }
            }
        }
        return listCommunicableEntities;
    }
}
