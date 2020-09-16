package com.rapgru.ampel.service.discord;

import com.rapgru.ampel.model.DistrictChange;

import java.util.List;

public interface NotificationService {

    public void pushChanges(List<DistrictChange> changes);
}
