package org.oregami.resources;

import io.dropwizard.auth.Auth;
import org.apache.log4j.Logger;
import org.oregami.dropwizard.User;
import org.oregami.entities.BaseEntityUUID;
import org.oregami.entities.Task;
import org.oregami.service.ServiceCallContext;
import org.oregami.service.ServiceResult;
import org.oregami.service.TopLevelEntityService;

import javax.persistence.OptimisticLockException;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Created by sebastian on 18.03.15.
 */
public abstract class TopLevelEntityResource {

    public Response create(User user, BaseEntityUUID entity, TopLevelEntityService service) {
        try {
            ServiceCallContext context = new ServiceCallContext(user);
            ServiceResult<Task> serviceResult = service.createNewEntity(entity, context);
            if (serviceResult.hasErrors()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .type("text/json")
                        .entity(serviceResult.getErrors()).build();
            }
            return Response.created(new URI(serviceResult.getResult().getId())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).type("text/plain")
                    .entity(e.getMessage()).build();
        }
    }

    public Response update(User user,String id, BaseEntityUUID entity, TopLevelEntityService service) {
        if (entity.getId()==null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            ServiceCallContext context = new ServiceCallContext(user);
            ServiceResult<Task> serviceResult = service.updateEntity(entity, context);
            if (serviceResult.hasErrors()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .type("text/json")
                        .entity(serviceResult.getErrors()).build();
            }
        } catch (OptimisticLockException e) {
            Logger.getLogger(this.getClass()).warn("OptimisticLockException", e);
            return Response.status(Response.Status.BAD_REQUEST).tag("OptimisticLockException").build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(entity).build();
    }
}
