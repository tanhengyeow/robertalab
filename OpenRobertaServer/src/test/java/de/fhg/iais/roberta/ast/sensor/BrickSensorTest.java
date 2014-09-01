package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickKey;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickSensor;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;

public class BrickSensorTest {

    @Test
    public void main() throws Exception {
        String a = "BlockAST [project=[[BrickSensor [key=ENTER, mode=IS_PRESSED]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_brick1.xml"));
    }

    @Test
    public void getKey() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_brick1.xml");
        BrickSensor<Void> bs = (BrickSensor<Void>) transformer.getTree().get(0);
        Assert.assertEquals(BrickKey.ENTER, bs.getKey());
    }

    @Test
    public void getMode() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/sensors/sensor_brick1.xml");
        BrickSensor<Void> bs = (BrickSensor<Void>) transformer.getTree().get(0);
        Assert.assertEquals(BrickSensor.Mode.IS_PRESSED, bs.getMode());
    }

    @Test
    public void invalideMode() throws Exception {
        try {
            @SuppressWarnings("unused")
            BrickSensor<Void> va = BrickSensor.make(BrickSensor.Mode.valueOf("invalid"), null);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("No enum constant de.fhg.iais.roberta.ast.syntax.sensor.BrickSensor.Mode.invalid", e.getMessage());
        }

    }

    @Test
    public void sensorBrick() throws Exception {
        String a =
            "BlockAST [project=[[\n"
                + "if SensorExpr [TouchSensor [port=S1]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [BrickSensor [key=ENTER, mode=IS_PRESSED]]\n\n"
                + "Var [item] := SensorExpr [BrickSensor [key=LEFT, mode=WAIT_FOR_PRESS_AND_RELEASE]]\n\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_brick.xml"));
    }

    @Test
    public void sensorBrick2() throws Exception {
        String a = "BlockAST [project=[[BrickSensor [key=ENTER, mode=WAIT_FOR_PRESS_AND_RELEASE]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_brick2.xml"));
    }

}