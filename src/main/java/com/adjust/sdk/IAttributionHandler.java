package com.adjust.sdk;

public interface IAttributionHandler {
    void checkSessionResponse(SessionResponseData sessionResponseData);

    void getAttribution();

    void init(IActivityHandler iActivityHandler, ActivityPackage activityPackage, boolean z);

    void pauseSending();

    void resumeSending();

    void teardown();
}
