package bank.model;

import java.io.Serializable;

public interface InterestBearing extends Serializable {
    double calculateInterest();
    void applyInterest();
}