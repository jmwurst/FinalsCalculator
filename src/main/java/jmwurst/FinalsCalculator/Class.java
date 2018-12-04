package jmwurst.FinalsCalculator;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Class {
    public SimpleStringProperty name;
    public SimpleDoubleProperty currentAvg, finalWeight;
    public SimpleDoubleProperty max, needA, needB, needC, needD, needF;

    public Class(String name, double currentAvg, double finalWeight) {
        double decWt = finalWeight / 100.0;

        this.name = new SimpleStringProperty(name);
        this.currentAvg = new SimpleDoubleProperty(currentAvg);
        this.finalWeight = new SimpleDoubleProperty(decWt);

        this.max = new SimpleDoubleProperty(Math.round(
                ((currentAvg * (1 - decWt)) + (100.0 * decWt)) * 100.0) / 100.0);
        this.needA = new SimpleDoubleProperty(Math.round(
                (((90 - (currentAvg * (1 - decWt))) / decWt) * 100.0)) / 100.0);
        this.needB = new SimpleDoubleProperty(Math.round(
                (((80 - (currentAvg * (1 - decWt))) / decWt) * 100.0)) / 100.0);
        this.needC = new SimpleDoubleProperty(Math.round(
                (((70 - (currentAvg * (1 - decWt))) / decWt) * 100.0)) / 100.0);
        this.needD = new SimpleDoubleProperty(Math.round(
                (((60 - (currentAvg * (1 - decWt))) / decWt) * 100.0)) / 100.0);
        this.needF = new SimpleDoubleProperty(Math.round(
                (((50 - (currentAvg * (1 - decWt))) / decWt) * 100.0)) / 100.0);
    }

    public String getName() {
        return name.get();
    }

    public double getCurrentAvg() {
        return currentAvg.get();
    }

    public double getFinalWeight() {
        return finalWeight.get();
    }

    public double getMax() {
        return Math.max(0, max.get());
    }

    public double getNeedA() {
        return Math.max(0, needA.get());
    }

    public double getNeedB() {
        return Math.max(0, needB.get());
    }

    public double getNeedC() {
        return Math.max(0, needC.get());
    }

    public double getNeedD() {
        return Math.max(0, needD.get());
    }

    public double getNeedF() {
        return Math.max(0, needF.get());
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof Class)) {
            return false;
        } else {
            Class oC = (Class) o;
            return (this.name.equals(oC.name))
                    && (this.currentAvg.equals(oC.currentAvg))
                    && (this.finalWeight.equals(oC.finalWeight));
        }
    }
}
