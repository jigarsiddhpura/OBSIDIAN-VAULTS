package ObserverPattern.Observer;

import ObserverPattern.Observable.StocksObservable;

public class MobileAlertObserverImpl implements NotificationAlertObserver {
    public StocksObservable observable;
    public String username;

    public MobileAlertObserverImpl(StocksObservable observableObj, String usernameObj){
        observable = observableObj;
        username = usernameObj;
    }

    @Override
    public void update() {
        sendMobileAlert(username);
    }

    public void sendMobileAlert(String username){ 
        System.out.println("Mobile Alert sent to " + username + ": Item is in stock");
    }
}
