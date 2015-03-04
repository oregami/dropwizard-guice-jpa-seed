package org.oregami.entities;

import org.hibernate.envers.RevisionListener;
import org.oregami.service.ServiceCallContext;

public class CustomRevisionListener implements RevisionListener {

    public static final ThreadLocal<ServiceCallContext> context = new ThreadLocal<>();

    @Override
    public void newRevision(Object rev) {
        CustomRevisionEntity revEntity = (CustomRevisionEntity) rev;

        if (context.get()!=null) {
            String userId = context.get().getUser().getUsername();
            revEntity.setUserId(userId);
            context.remove();
        }

    }
}
