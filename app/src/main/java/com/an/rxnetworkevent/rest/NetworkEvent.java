package com.an.rxnetworkevent.rest;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class NetworkEvent {

    private static volatile NetworkEvent sSoleInstance;
    private NetworkEvent() {
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static NetworkEvent getInstance() {
        if (sSoleInstance == null) {
            synchronized (NetworkEvent.class) {
                if (sSoleInstance == null) sSoleInstance = new NetworkEvent();
            }
        }
        return sSoleInstance;
    }

    /*
     * Step 2: Create a PublishSubject instance
     * which we use to publish events to all
     * registered subscribers in the app.
     */
    private PublishSubject<NetworkState> subject;


    /*
     * Step 2: Create a method to fetch the Subject
     * or create it if it's not already in memory.
     */
    @NonNull
    private PublishSubject<NetworkState> getSubject() {
        if (subject == null) {
            subject = PublishSubject.create();
            subject.subscribeOn(AndroidSchedulers.mainThread());
        }
        return subject;
    }

    /*
     * Step 3: Create a CompositeDisposable map.
     * We use CompositeDisposable to maintain the list
     * of subscriptions in a pool. And also to so that
     * we can dispose them all at once.
     */
    private Map<Object, CompositeDisposable> compositeDisposableMap = new HashMap<>();


    /*
     * Step 3: Create a method to fetch the CompositeDisposable Map
     * or create it if it's not already in memory.
     * We pass a key (in this case our key is the Activity or fragment
     * instance).
     */
    @NonNull
    private CompositeDisposable getCompositeSubscription(@NonNull Object object) {
        CompositeDisposable compositeSubscription = compositeDisposableMap.get(object);
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeDisposable();
            compositeDisposableMap.put(object, compositeSubscription);
        }
        return compositeSubscription;
    }



    /*
     * Step 4: Use this method to Publish the NetworkState
     * to all the specified subscribers of the subject.
     */
    public void publish(@NonNull NetworkState networkState) {
        new Handler(Looper.getMainLooper())
                .post(() -> getSubject().onNext(networkState));
    }


    /*
     * Step 4: Use this method to subscribe to the specified subject
     * and listen for updates on that subject.
     * Pass in an object (in this case the activity or fragment instance)
     * to associate the registration, so that we can unsubscribe later.
     */
    public void register(@NonNull Object lifecycle, @NonNull Consumer<NetworkState> action) {
        Disposable disposable = getSubject().subscribe(action);
        getCompositeSubscription(lifecycle).add(disposable);
    }


    /*
     * Step 4: Use this method to Unregister this particular object
     * from the bus, removing all subscriptions.
     * This should be called in order to avoid memory leaks
     */
    public void unregister(@NonNull Object lifecycle) {
        //We have to remove the composition from the map, because once you unsubscribe it can't be used anymore
        CompositeDisposable compositeSubscription = compositeDisposableMap.remove(lifecycle);
        if (compositeSubscription != null) {
            compositeSubscription.dispose();
        }
    }
}
