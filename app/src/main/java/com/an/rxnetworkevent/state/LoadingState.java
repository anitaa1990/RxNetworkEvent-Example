package com.an.rxnetworkevent.state;

public class LoadingState {

    private boolean isLoading;
    private boolean isLoaded;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
