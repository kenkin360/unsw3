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

public class RelaySatellite extends Satellite {

    private boolean opposite = false;
    /**
     * 
     */
    public RelaySatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, "RelaySatellite", height, position);
        //TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    @Override
    public void updatePosition() {
        // TODO Auto-generated method stub
        Angle newPosition;
        Angle oldPosition = super.getPosition();
        Angle radianChange = Angle.fromRadians(1500 / RADIUS_OF_JUPITER);
        if (oldPosition.toDegrees() <= 140 || oldPosition.toDegrees() >= 345) {
            newPosition = oldPosition.add(radianChange);
            super.setPosition(newPosition);
            opposite = false;
        }
        else if (oldPosition.toDegrees() >= 190 && oldPosition.toDegrees() < 345) {
            newPosition = oldPosition.subtract(radianChange);
            super.setPosition(newPosition);
            opposite = true;
        }
        else {
            if (opposite == true) {
                newPosition = oldPosition.subtract(radianChange);
                super.setPosition(newPosition);
            }
            else {
                newPosition = oldPosition.add(radianChange);
                super.setPosition(newPosition);
            }
        }
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
            satelliteInfo = new EntityInfoResponse(id, position, height, type);
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
                // Ignore the selected satellite itself
            }
            else {
                if (MathsHelper.isVisible(super.getHeight(), super.getPosition(), satellite.getHeight(), satellite.getPosition())) {
                    listCommunicableEntities.add(satellite.getSatelliteId());
                }
            }
        }
        for (Device device : deviceList) {
            if (MathsHelper.isVisible(super.getHeight(), super.getPosition(), device.getPosition())) {
                listCommunicableEntities.add(device.getDeviceId());
            }
        }
        return listCommunicableEntities;
    }  
}
