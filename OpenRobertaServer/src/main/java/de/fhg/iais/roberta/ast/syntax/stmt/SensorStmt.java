package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.sensor.Sensor;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Sensor} so they can be used as {@link Stmt} in statements.
 */
public class SensorStmt<V> extends Stmt<V> {
    private final Sensor<V> sensor;

    private SensorStmt(Sensor<V> sensor) {
        super(Phrase.Kind.SENSOR_STMT);
        Assert.isTrue(sensor != null && sensor.isReadOnly());
        this.sensor = sensor;
        setReadOnly();
    }

    /**
     * Create object of the class {@link SensorStmt}.
     * 
     * @param sensor that we want to wrap
     * @return statement with wrapped sensor inside
     */
    public static <V> SensorStmt<V> make(Sensor<V> sensor) {
        return new SensorStmt<V>(sensor);
    }

    /**
     * @return sensor that is wrapped in the statement
     */
    public Sensor<V> getSensor() {
        return this.sensor;
    }

    @Override
    public String toString() {
        return "\nSensorStmt " + this.sensor;
    }

    @Override
    protected V accept(Visitor<V> visitor) {
        return visitor.visitSensorStmt(this);
    }

}
