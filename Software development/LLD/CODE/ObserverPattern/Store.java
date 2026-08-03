package ObserverPattern;

import ObserverPattern.Observable.IphoneObservableImpl;
import ObserverPattern.Observable.StocksObservable;
import ObserverPattern.Observer.EmailAlertObserverImpl;
import ObserverPattern.Observer.MobileAlertObserverImpl;
import ObserverPattern.Observer.NotificationAlertObserver;

public class Store {
    public static void main(String[] args) {
        System.out.println("Welcome to the Store");

        StocksObservable observable = new IphoneObservableImpl();

        NotificationAlertObserver observer1 = new EmailAlertObserverImpl(observable, "email1@.com");
        NotificationAlertObserver observer2 = new MobileAlertObserverImpl(observable, "username1");

        observable.add(observer1);
        observable.add(observer2);

        observable.setStock(10);

    }
}
