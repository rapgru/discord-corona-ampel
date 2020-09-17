package com.rapgru.ampel.dao;

import com.rapgru.ampel.model.Subscription;

import java.util.List;

public interface SubscriptionDAO {



    void storeSubscription(Subscription subscription);

    List<Subscription> getSubscriptionWithUsername(String username);

}