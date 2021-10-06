package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

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
    public void basicFileSupport() {
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
    public void changePosition() {
        // Task 2: simulate all actions in the Blackout system.
        BlackoutController controller = new BlackoutController();

        // Creates 3 satellites.
        controller.createSatellite("USA", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createSatellite("TAIWAN", "ShrinkingSatellite", 200 + RADIUS_OF_JUPITER, Angle.fromDegrees(270));
        controller.createSatellite("AUSTRALIA", "RelaySatellite", 300 + RADIUS_OF_JUPITER, Angle.fromDegrees(130));

        // moves in anticlockwise direction.
        assertEquals(new EntityInfoResponse("USA", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("USA"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("USA", Angle.fromDegrees(342.05), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("USA"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("USA", Angle.fromDegrees(344.10), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("USA"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("USA", Angle.fromDegrees(346.15), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("USA"));
        controller.simulate(20);

        // Special case for Relay Satellite. 
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
        controller.simulate(49);
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(189.01), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("AUSTRALIA", Angle.fromDegrees(190.24), 300 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("AUSTRALIA"));
        
        // goes backward with clockwise direction.
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(189.82), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(183.69), 100 + RADIUS_OF_JUPITER, "RelaySatellite"), controller.getInfo("Satellite1"));
    }
}
