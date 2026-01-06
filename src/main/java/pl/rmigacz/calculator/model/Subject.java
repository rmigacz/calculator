package pl.rmigacz.calculator.model;

import pl.rmigacz.calculator.view.Observer;

public interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}
