package ObserverPattern.Observer;

import ObserverPattern.Observable.StocksObservable;

public class EmailAlertObserverImpl implements NotificationAlertObserver{
    public StocksObservable observable;
    public String email;

    public EmailAlertObserverImpl(StocksObservable observableObj, String emailObj){
        observable = observableObj;
        email = emailObj;
    }

    @Override
    public void update() {
        sendEmail(email);
    }

    public void sendEmail(String email){
        System.out.println("Email Alert sent on " + email + ": Item is in stock");
    }
}
