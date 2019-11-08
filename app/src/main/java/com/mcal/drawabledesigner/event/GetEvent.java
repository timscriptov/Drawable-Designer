package com.mcal.drawabledesigner.event;

public class GetEvent<T> {
    public ReceiveCallback receiveCallback;

    public GetEvent(ReceiveCallback receiveCallback) {
        this.receiveCallback = receiveCallback;
    }

    public interface ReceiveCallback<T> {
        void receive(T t);
    }
}