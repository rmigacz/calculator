package pl.rmigacz.calculator.model;

import pl.rmigacz.calculator.view.Observer;

import java.util.ArrayList;
import java.util.List;

public class Model implements Subject {

    private final StateMachine stateMachine = new StateMachine();
    private final Actions actions = new Actions();
    private final Context context = new Context();
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    public String getDisplay() {
        return context.getDisplay();
    }

    public void onCommand(Command command) {
        State current = stateMachine.getState();
        actions.apply(command, current, context);
        stateMachine.onCommand(command);
        notifyObservers();
    }

    // test hook
    State getState() {
        return stateMachine.getState();
    }
}
