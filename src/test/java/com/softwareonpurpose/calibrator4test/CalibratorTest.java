package com.softwareonpurpose.calibrator4test;

import com.softwareonpurpose.calibrator4test.mock.AnObject;
import com.softwareonpurpose.calibrator4test.mock.AnObjectCalibrator;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class CalibratorTest {

    @Test
    public void nodeCalibrator_pass() {
        AnObject expected = AnObject.getInstance(true, 9, "String");
        AnObject actual = AnObject.getInstance(true, 9, "String");
        Calibrator calibrator = AnObjectCalibrator.getInstance(expected, actual);
        Assert.assertEquals(calibrator.calibrate(), Calibrator.SUCCESS);
    }

    @Test
    public void nodeCalibrator_fail() {
        AnObject expected = AnObject.getInstance(true, 9, "String");
        AnObject actual = AnObject.getInstance(false, 0, "Not String");
        Calibrator calibrator = AnObjectCalibrator.getInstance(expected, actual);
        Assert.assertEquals(calibrator.calibrate(), "CALIBRATION FAILED: \r\n" +
                "    AnObjectCalibrator: Boolean -- Expected: true  Actual: false\r\n" +
                "    AnObjectCalibrator: Integer -- Expected: 9  Actual: 0\r\n" +
                "    AnObjectCalibrator: String -- Expected: String  Actual: Not String\r\n\r\n");
    }

    @Test
    public void parentChildCalibrators_pass() {
        AnObject expectedParent = AnObject.getInstance(true, 9, "parent");
        AnObject actualParent = AnObject.getInstance(true, 9, "parent");
        AnObject expectedChild = AnObject.getInstance(true, 9, "child");
        AnObject actualChild = AnObject.getInstance(true, 9, "child");
        AnObjectCalibrator calibrator = AnObjectCalibrator.getInstance(expectedParent, actualParent);
        calibrator.addChildCalibrator(AnObjectCalibrator.getInstance(expectedChild, actualChild));
        Assert.assertEquals(calibrator.calibrate(), Calibrator.SUCCESS);
    }

    @Test
    public void parentChildCalibrators_rootFail() {
        AnObject object_1 = AnObject.getInstance(true, 5, "Child String");
        AnObject object_2 = AnObject.getInstance(false, 9, "String");
        Calibrator parent = AnObjectCalibrator.getInstance(object_1, object_2);
        parent.addChildCalibrator(AnObjectCalibrator.getInstance(object_1, object_1));
        String expected = "CALIBRATION FAILED: \r\n" +
                "    AnObjectCalibrator: Boolean -- Expected: true  Actual: false\r\n" +
                "    AnObjectCalibrator: Integer -- Expected: 5  Actual: 9\r\n" +
                "    AnObjectCalibrator: String -- Expected: Child String  Actual: String\r\n" +
                "\r\n";
        String actual = parent.calibrate();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void parentChildCalibrators_childFail() {
        AnObject object_1 = AnObject.getInstance(true, 5, "Child String");
        AnObject object_2 = AnObject.getInstance(false, 9, "String");
        Calibrator parent = AnObjectCalibrator.getInstance(object_1, object_1);
        parent.addChildCalibrator(AnObjectCalibrator.getInstance(object_1, object_2));
        String expected = "CALIBRATION FAILED: \r\n" +
                "      AnObjectCalibrator: Boolean -- Expected: true  Actual: false\r\n" +
                "      AnObjectCalibrator: Integer -- Expected: 5  Actual: 9\r\n" +
                "      AnObjectCalibrator: String -- Expected: Child String  Actual: String\r\n" +
                "\r\n";
        String actual = parent.calibrate();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void parentChildCalibrators_bothFail() {
        AnObject object_1 = AnObject.getInstance(true, 5, "Child String");
        AnObject object_2 = AnObject.getInstance(false, 9, "String");
        Calibrator parent = AnObjectCalibrator.getInstance(object_1, object_2);
        parent.addChildCalibrator(AnObjectCalibrator.getInstance(object_1, object_2));
        String expected = "CALIBRATION FAILED: \r\n" +
                "    AnObjectCalibrator: Boolean -- Expected: true  Actual: false\r\n" +
                "    AnObjectCalibrator: Integer -- Expected: 5  Actual: 9\r\n" +
                "    AnObjectCalibrator: String -- Expected: Child String  Actual: String\r\n" +
                "      AnObjectCalibrator: Boolean -- Expected: true  Actual: false\r\n" +
                "      AnObjectCalibrator: Integer -- Expected: 5  Actual: 9\r\n" +
                "      AnObjectCalibrator: String -- Expected: Child String  Actual: String\r\n" +
                "\r\n";
        String actual = parent.calibrate();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void parentGrandchildCalibrators_pass(){
        AnObject expectedParent = AnObject.getInstance(true, 9, "parent");
        AnObject actualParent = AnObject.getInstance(true, 9, "parent");
        AnObject expectedChild = AnObject.getInstance(false, 9, "child");
        AnObject actualChild = AnObject.getInstance(false, 9, "child");
        AnObject expectedGrandchild = AnObject.getInstance(true, 1, "grandchild");
        AnObject actualGrandchild = AnObject.getInstance(true, 1, "grandchild");
        AnObjectCalibrator calibrator = AnObjectCalibrator.getInstance(expectedParent, actualParent);
        AnObjectCalibrator childCalibrator = AnObjectCalibrator.getInstance(expectedChild, actualChild);
        AnObjectCalibrator grandchildCalibrator = AnObjectCalibrator.getInstance(expectedGrandchild, actualGrandchild);
        childCalibrator.addChildCalibrator(grandchildCalibrator);
        calibrator.addChildCalibrator(childCalibrator);
        Assert.assertEquals(calibrator.calibrate(), Calibrator.SUCCESS);
    }

    @Test
    public void expectedNull() {
        AnObject nullObject = null;
        AnObject anObject = AnObject.getInstance(true, 9, "String");
        //noinspection ConstantConditions
        AnObjectCalibrator calibrator = AnObjectCalibrator.getInstance(nullObject, anObject);
        String actual = calibrator.calibrate();
        String expected = "CALIBRATION FAILED: \r\n" +
                "    AnObjectCalibrator: Object exists -- Expected: false  Actual: true\r\n" +
                "\r\n";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void actualNull() {
        AnObject anObject = AnObject.getInstance(true, 9, "String");
        AnObject nullObject = null;
        //noinspection ConstantConditions
        AnObjectCalibrator calibrator = AnObjectCalibrator.getInstance(anObject, nullObject);
        String actual = calibrator.calibrate();
        String expected = "CALIBRATION FAILED: \r\n" +
                "    AnObjectCalibrator: Object exists -- Expected: true  Actual: false\r\n" +
                "\r\n";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void bothNull() {
        AnObject nullObject = null;
        //noinspection ConstantConditions
        AnObjectCalibrator calibrator = AnObjectCalibrator.getInstance(nullObject, nullObject);
        String actual = calibrator.calibrate();
        String expected = Calibrator.SUCCESS;
        Assert.assertEquals(actual, expected);
    }
}
