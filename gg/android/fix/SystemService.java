package android.fix;

import android.view.LayoutInflater;

public class SystemService {
    public static Object wrap(Object service) {
        return service != null && ContextWrapper.isUseFix() && service instanceof LayoutInflater && !(service instanceof android.fix.LayoutInflater) ? new android.fix.LayoutInflater(((LayoutInflater)service)) : service;
    }
}

