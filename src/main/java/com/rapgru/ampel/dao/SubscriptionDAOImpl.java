package com.rapgru.ampel.dao;

import com.dieselpoint.norm.Database;
import com.rapgru.ampel.model.Subscription;
import com.rapgru.ampel.object.SubscriptionDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class SubscriptionDAOImpl implements SubscriptionDAO {

    private final Database database;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataFetchDaoImpl.class);

    public SubscriptionDAOImpl(Database database) {
        this.database = database;
    }

    @Override
    public void deleteSubscription(Subscription subscription) {
        database.where("id=?", subscription.getId()).delete();
    }

    @Override
    public void storeSubscription(Subscription subscription) {
        SubscriptionDO subscriptionDO = new SubscriptionDO(
                subscription.getDate().toString(),
                subscription.getUserId(),
                subscription.getGkz()
        );

        database.insert(subscriptionDO);
    }

    @Override
    public List<String> getUsernamesSubscribedTo(int gkz) {
        return database
                .where("gkz=?", gkz)
                .results(SubscriptionDO.class)
                .stream()
                .map(SubscriptionDO::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Subscription> getSubscriptionWithUsername(String username) {
        return database
                .where("userId=?", username)
                .results(SubscriptionDO.class)
                .stream()
                .map(this::toSubscription)
                .collect(Collectors.toList());
    }

    private Subscription toSubscription(SubscriptionDO subscriptionDO) {
        return new Subscription(
                subscriptionDO.getId(),
                Instant.parse(subscriptionDO.getDate()),
                subscriptionDO.getUserId(),
                subscriptionDO.getGkz()
        );
    }
}
