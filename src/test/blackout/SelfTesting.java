package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

// import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

/**
 * Self testing to check the logics are correct in the design.
 */
@TestInstance(value = Lifecycle.PER_CLASS)
public class SelfTesting {
    @Test
    public void testCreate() {
        // Task 1: create objects to the Blackout system
        BlackoutController controller = new BlackoutController();

        // Creates 3 satellite and 3 devices
        controller.createSatellite("USA", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createSatellite("TAIWAN", "ShrinkingSatellite", 200 + RADIUS_OF_JUPITER, Angle.fromDegrees(270));
        controller.createSatellite("AUSTRALIA", "RelaySatellite", 300 + RADIUS_OF_JUPITER, Angle.fromDegrees(165));
        controller.createDevice("ASUS", "HandheldDevice", Angle.fromDegrees(60));
        controller.createDevice("MSI", "LaptopDevice", Angle.fromDegrees(190));
        controller.createDevice("RAZER", "DesktopDevice", Angle.fromDegrees(345));

        assertListAreEqualIgnoringOrder(Arrays.asList("USA", "TAIWAN", "AUSTRALIA"), controller.listSatelliteIds());
        assertListAreEqualIgnoringOrder(Arrays.asList("ASUS", "MSI", "RAZER"), controller.listDeviceIds());

        assertEquals(new EntityInfoResponse("USA", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("USA"));
        assertEquals(new EntityInfoResponse("TAIWAN", Angle.fromDegrees(270), 200 + RADIUS_OF_JUPITER, "ShrinkingSatellite"), controller.getInfo("TAIWAN"));
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(165), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));

        assertEquals(new EntityInfoResponse("ASUS", Angle.fromDegrees(60), RADIUS_OF_JUPITER, "HandheldDevice"), controller.getInfo("ASUS"));
        assertEquals(new EntityInfoResponse("MSI", Angle.fromDegrees(190), RADIUS_OF_JUPITER, "LaptopDevice"), controller.getInfo("MSI"));
        assertEquals(new EntityInfoResponse("RAZER", Angle.fromDegrees(345), RADIUS_OF_JUPITER, "DesktopDevice"), controller.getInfo("RAZER"));

    }

    @Test
    public void testDelete() {
        // Task 1: delete objects from the Blackout system
        BlackoutController controller = new BlackoutController();

        // Creates 3 satellite and 3 devices and deletes them
        controller.createSatellite("USA", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createSatellite("TAIWAN", "ShrinkingSatellite", 200 + RADIUS_OF_JUPITER, Angle.fromDegrees(270));
        controller.createSatellite("AUSTRALIA", "RelaySatellite", 300 + RADIUS_OF_JUPITER, Angle.fromDegrees(165));
        controller.createDevice("ASUS", "HandheldDevice", Angle.fromDegrees(60));
        controller.createDevice("MSI", "LaptopDevice", Angle.fromDegrees(190));
        controller.createDevice("RAZER", "DesktopDevice", Angle.fromDegrees(345));

        assertListAreEqualIgnoringOrder(Arrays.asList("USA", "TAIWAN", "AUSTRALIA"), controller.listSatelliteIds());
        assertListAreEqualIgnoringOrder(Arrays.asList("ASUS", "MSI", "RAZER"), controller.listDeviceIds());

        controller.removeDevice("ASUS");
        controller.removeDevice("MSI");
        controller.removeDevice("RAZER");

        controller.removeSatellite("USA");
        controller.removeSatellite("TAIWAN");
        controller.removeSatellite("AUSTRALIA");

        assertListAreEqualIgnoringOrder(Arrays.asList(), controller.listSatelliteIds());
        assertListAreEqualIgnoringOrder(Arrays.asList(), controller.listDeviceIds());
    }

    @Test
    public void testCheckFileAdded() {
        // Task 1: add a file to a device.
        BlackoutController controller = new BlackoutController();

        // Creates 1 device and add a file to it. Check if the file is stored in the device or not.
        controller.createDevice("ASUS", "HandheldDevice", Angle.fromDegrees(60));
        assertListAreEqualIgnoringOrder(Arrays.asList("ASUS"), controller.listDeviceIds());
        assertEquals(new EntityInfoResponse("ASUS", Angle.fromDegrees(60), RADIUS_OF_JUPITER, "HandheldDevice"), controller.getInfo("ASUS"));

        controller.addFileToDevice("ASUS", "Hello World", "COMP2511!");
        Map<String, FileInfoResponse> expected1 = new HashMap<>();
        expected1.put("Hello World", new FileInfoResponse("Hello World", "COMP2511!", "COMP2511!".length(), true));
        assertEquals(new EntityInfoResponse("ASUS", Angle.fromDegrees(60), RADIUS_OF_JUPITER, "HandheldDevice", expected1), controller.getInfo("ASUS"));

        // Create a new device and add multiple files to a device. Check if the files are stored in the device or not.
        controller.createDevice("RAZER", "DesktopDevice", Angle.fromDegrees(345));
        assertListAreEqualIgnoringOrder(Arrays.asList("ASUS", "RAZER"), controller.listDeviceIds());
        assertEquals(new EntityInfoResponse("RAZER", Angle.fromDegrees(345), RADIUS_OF_JUPITER, "DesktopDevice"), controller.getInfo("RAZER"));

        controller.addFileToDevice("RAZER", "First Course", "COMP2511!");
        controller.addFileToDevice("RAZER", "Second Course", "COMP3601!");
        controller.addFileToDevice("RAZER", "Third Course", "MATH2069!");
        Map<String, FileInfoResponse> expected2 = new HashMap<>();
        expected2.put("First Course", new FileInfoResponse("First Course", "COMP2511!", "COMP2511!".length(), true));
        expected2.put("Second Course", new FileInfoResponse("Second Course", "COMP3601!", "COMP3601!".length(), true));
        expected2.put("Third Course", new FileInfoResponse("Third Course", "MATH2069!", "MATH2069!".length(), true));
        assertEquals(new EntityInfoResponse("RAZER", Angle.fromDegrees(345), RADIUS_OF_JUPITER, "DesktopDevice", expected2), controller.getInfo("RAZER"));
    }

    @Test
    public void testChangeSatellitePosition() {
        // Task 2: simulate all actions in the Blackout system.
        BlackoutController controller = new BlackoutController();

        // Creates a StandardSatellite and ShrinkingSatellite.
        controller.createSatellite("USA", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createSatellite("TAIWAN", "ShrinkingSatellite", 200 + RADIUS_OF_JUPITER, Angle.fromDegrees(270));

        // moves in anticlockwise direction.
        assertEquals(new EntityInfoResponse("USA", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("USA"));
        assertEquals(new EntityInfoResponse("TAIWAN", Angle.fromDegrees(270), 200 + RADIUS_OF_JUPITER, "ShrinkingSatellite"), controller.getInfo("TAIWAN"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("USA", Angle.fromDegrees(342.05), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("USA"));
        assertEquals(new EntityInfoResponse("TAIWAN", Angle.fromDegrees(270.82), 200 + RADIUS_OF_JUPITER, "ShrinkingSatellite"), controller.getInfo("TAIWAN"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("USA", Angle.fromDegrees(344.10), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("USA"));
        assertEquals(new EntityInfoResponse("TAIWAN", Angle.fromDegrees(271.64), 200 + RADIUS_OF_JUPITER, "ShrinkingSatellite"), controller.getInfo("TAIWAN"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("USA", Angle.fromDegrees(346.15), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("USA"));
        assertEquals(new EntityInfoResponse("TAIWAN", Angle.fromDegrees(272.86), 200 + RADIUS_OF_JUPITER, "ShrinkingSatellite"), controller.getInfo("TAIWAN"));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("USA", Angle.fromDegrees(356.39), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("USA"));
        assertEquals(new EntityInfoResponse("TAIWAN", Angle.fromDegrees(276.56), 200 + RADIUS_OF_JUPITER, "ShrinkingSatellite"), controller.getInfo("TAIWAN")); 
    }

    @Test
    public void testChangeRelaySatellitePosition() {
        // Task 2: simulate all actions in the Blackout system.
        BlackoutController controller = new BlackoutController();

        // Create a RelaySatellite.
        controller.createSatellite("AUSTRALIA", "RelaySatellite", 300 + RADIUS_OF_JUPITER, Angle.fromDegrees(130));

        // When it moves to the region between 140° and 190°, it will move back and forth within the region.
        // moves forward with anticlockwise direction.
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(130), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(131.23), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(132.46), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(133.69), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
        
        // edge case
        controller.simulate(45);
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(189.01), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(190.24), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
        
        // goes backward with clockwise direction.
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(189.01), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(182.86), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));

        // edge case
        controller.simulate(34);
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(141.06), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(139.83), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));

        // goes backward with anticlockwise direction.
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(141.06), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(147.21), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
    }

    @Test
    public void testEntitiesInRange() {
        // Task 2: check all possible objects in range of the chosen object.
        BlackoutController controller = new BlackoutController();

        // Creates 3 satellite and 3 devices
        controller.createSatellite("USA", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(300));
        controller.createSatellite("TAIWAN", "ShrinkingSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createSatellite("AUSTRALIA", "RelaySatellite", 2000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("ASUS", "HandheldDevice", Angle.fromDegrees(310));
        controller.createDevice("MSI", "LaptopDevice", Angle.fromDegrees(320));
        controller.createDevice("RAZER", "DesktopDevice", Angle.fromDegrees(315));

        assertListAreEqualIgnoringOrder(Arrays.asList("ASUS", "AUSTRALIA"), controller.communicableEntitiesInRange("USA"));
        assertListAreEqualIgnoringOrder(Arrays.asList("AUSTRALIA"), controller.communicableEntitiesInRange("TAIWAN"));
        assertListAreEqualIgnoringOrder(Arrays.asList("ASUS", "MSI", "RAZER", "USA", "TAIWAN"), controller.communicableEntitiesInRange("AUSTRALIA"));

        assertListAreEqualIgnoringOrder(Arrays.asList("USA", "AUSTRALIA"), controller.communicableEntitiesInRange("ASUS"));
        assertListAreEqualIgnoringOrder(Arrays.asList("AUSTRALIA"), controller.communicableEntitiesInRange("MSI"));
        assertListAreEqualIgnoringOrder(Arrays.asList("AUSTRALIA"), controller.communicableEntitiesInRange("RAZER"));
    }
}
