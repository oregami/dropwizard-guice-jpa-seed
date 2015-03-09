package org.oregami.entities;

import org.hibernate.envers.RevisionListener;
import org.oregami.dropwizard.User;
import org.oregami.service.ServiceCallContext;

public class CustomRevisionListener implements RevisionListener {

    public static final ThreadLocal<ServiceCallContext> context =
            new ThreadLocal<ServiceCallContext>() {
                @Override
                public ServiceCallContext initialValue() {
                    return new ServiceCallContext();
                }
            };


    @Override
    public void newRevision(Object rev) {
        CustomRevisionEntity revEntity = (CustomRevisionEntity) rev;

        if (context.get() != null) {
            User user = context.get().getUser();
            if (user != null) {
                revEntity.setUserId(user.getUsername());
            }
            revEntity.setEntityId(context.get().getEntityId());
            revEntity.setEntityDiscriminator(context.get().getEntityDiscriminator());
            context.remove();
        }

    }
}
