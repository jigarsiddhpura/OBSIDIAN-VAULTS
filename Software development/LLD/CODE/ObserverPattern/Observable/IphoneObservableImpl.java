package ObserverPattern.Observable;

import java.util.ArrayList;
import java.util.List;

import ObserverPattern.Observer.NotificationAlertObserver;

public class IphoneObservableImpl implements StocksObservable {
    public int stock = 0;
    public List<NotificationAlertObserver> observers = new ArrayList<>();

    @Override
    public void add(NotificationAlertObserver observerObj) {
        observers.add(observerObj);
    }

    @Override
    public void remove(NotificationAlertObserver observerObj) {
        observers.remove(observerObj);
    }

    @Override
    public void notifyObservers() {
        for (NotificationAlertObserver observer: observers) {
            observer.update();
        }
    }

    @Override
    public void setStock(int newStockAdded) {
        if (stock == 0) {
            notifyObservers();
        }
        stock += newStockAdded;
    }

    @Override
    public int getStock(){
        return stock;
    }

}
