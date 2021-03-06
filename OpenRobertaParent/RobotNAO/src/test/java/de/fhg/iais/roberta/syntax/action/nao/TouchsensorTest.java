package de.fhg.iais.roberta.syntax.action.nao;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.nao.Helper;

public class TouchsensorTest {
    Helper h = new Helper();

    @Test
    public void make_ByDefault_ReturnInstanceOfTouchsensorsClass() throws Exception {
        String expectedResult = "BlockAST [project=[[Location [x=63, y=63], " + "MainTask []], " + "[Location [x=87, y=113], " + "Touchsensors [HAND, LEFT]]]]";
        
        String result = this.h.generateTransformerString("/action/touchsensor.xml");

        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        this.h.assertTransformationIsOk("/action/touchsensor.xml");
    }
}