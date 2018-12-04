package jmwurst.FinalsCalculator;

public class Class {
    public String name;
    public double currentAvg, finalWeight;

    public Class(String name, double currentAvg, double finalWeight) {
        this.name = name;
        this.currentAvg = currentAvg;
        this.finalWeight = finalWeight / 100.0;
    }

    @Override
    public String toString() {
        return name;
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
                    && (this.currentAvg == oC.currentAvg)
                    && (this.finalWeight == oC.finalWeight);
        }
    }
}
