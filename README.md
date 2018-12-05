# RxNetworkEvent-Example


I recently had a requirement to handle error scenarios in apis. I have rest apis in the app and needed to handle no internet/ no service available / unauthorised errors in a generic way. i.e. I wanted to display an error dialog every time any api in the app throws a 503 or 401 error response.

This is the scenario I had to implement.</br>
<img src="https://github.com/anitaa1990/RxNetworkEvent-Example/blob/master/media/1.gif" width="300" style="max-width:200%;">
</br>


There are 5 steps to this implementation.

<b>1. Create [NetworkState](https://github.com/anitaa1990/RxNetworkEvent-Example/blob/master/app/src/main/java/com/an/rxnetworkevent/rest/NetworkState.java) enum</b></br>
We are going to create an enum class that holds the different api error states that can happen. (In my use case, I only needed to handle these 3 error states but you could add states more if you like).

<b>2. Create [NetworkEvent](https://github.com/anitaa1990/RxNetworkEvent-Example/blob/master/app/src/main/java/com/an/rxnetworkevent/rest/NetworkEvent.java) class</b></br>
We create a new class NetworkEvent (ignore the name please :) ). This is our RxEventBus class. We are going to:
  * Make this class a Singleton. You can checkout more about how to implement a Singleton class from [here](https://github.com/anitaa1990/Today-I-Learned/blob/master/android/singleton.md).
  * Create a PublishSubject instance in the class: PublishSubject can be used to publish events to all registered subscribers in the app. This is a single instance since I have only one event I need to publish. If we have multiple events, we can create a map to keep track of the different types of subjects.
  * Create a CompositeDisposable map in the class: We use CompositeDisposable to maintain the list of subscriptions in a pool. And also to so that we can dispose them all at once. (For example, when an activity is destroyed, it no longer needs to subscribe to the rest events right? So we need to remove the subscription in order to avoid memory leaks). The reason we have a map of CompositeDisposable is so that we can keep track of all the subscriptions for a given object (typically an Activity or Fragment). So the key of the map will be the Activity or fragment instance and the value of the map will a new CompositeDisposable instance.
  * Create 3 methods in the class:
      * ```publish(NetworkState networkState)``` — Method to publish the NetworkState event to all the subscribers subscribed to the changes in this event.
      * ```register(Object lifecycle, Consumer<NetworkState> action)``` — Method to subscribe to the specified subject and listen for updates on that subject. Pass in an object (in this case the activity or fragment instance) to associate the registration, so that we can unsubscribe later.
      * ```unregister(Object lifecycle)``` — Method to unregister this particular object from the bus, removing all subscriptions. This should be called in order to avoid memory leaks.
      
<b> 3. Create an interceptor class — [NetworkInterceptor](https://github.com/anitaa1990/RxNetworkEvent-Example/blob/master/app/src/main/java/com/an/rxnetworkevent/rest/NetworkInterceptor.java)</b></br>
 This class will check the error response from all the api responses. If the response code is 401 or 503, then we are publishing the network event to the UI.
 We use the [ConnectivityStatus](https://github.com/anitaa1990/RxNetworkEvent-Example/blob/master/app/src/main/java/com/an/rxnetworkevent/rest/ConnectivityStatus.java) class to check if internet is available in the device. Note that you need to have ACCESS_NETWORK_STATE permission declared in the app is order to get updates about the network status in a device.
 
<b> 4. Add the interceptor to the [Retrofit service](https://github.com/anitaa1990/RxNetworkEvent-Example/blob/master/app/src/main/java/com/an/rxnetworkevent/rest/RestApiService.java)</b></br>
 
<b>5. Register & unregister a subscriber - [BaseActivity](https://github.com/anitaa1990/RxNetworkEvent-Example/blob/master/app/src/main/java/com/an/rxnetworkevent/activity/BaseActivity.java)</b></br>
So in my use case, I had only one subscriber. I had created a BaseActivity class that all activities inherit, which means, regardless of which screen the user is at, if an api fails, the subscriber will be notified of it and we can display our error message.


You can simply use this one [NetworkEvent](https://github.com/anitaa1990/RxNetworkEvent-Example/blob/master/app/src/main/java/com/an/rxnetworkevent/rest/NetworkEvent.java) class to communicate between different parts of your app. This solution might not be perfect but I felt it was a good compromise between complexity and ease of use. You can use this solution as is if you want, or tweak it to fit your needs.
 
