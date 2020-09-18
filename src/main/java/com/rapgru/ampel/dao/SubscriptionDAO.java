package com.rapgru.ampel.dao;

import com.rapgru.ampel.model.Subscription;

import java.util.List;

public interface SubscriptionDAO {

    void deleteSubscription(Subscription subscription);

    void storeSubscription(Subscription subscription);

    List<String> getUsernamesSubscribedTo(int gkz);

    List<Subscription> getSubscriptionWithUsername(String username);

}
