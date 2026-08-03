package ObserverPattern.Observable;

import ObserverPattern.Observer.NotificationAlertObserver;

public interface StocksObservable {
    public void add(NotificationAlertObserver observerObj);
    public void remove(NotificationAlertObserver observerObj);
    public void notifyObservers();
    public int getStock();
    public void setStock(int newStockAdded);
}
