package org.oregami.resources;

import org.apache.log4j.Logger;
import org.oregami.data.RevisionInfo;
import org.oregami.dropwizard.User;
import org.oregami.entities.*;
import org.oregami.service.ServiceCallContext;
import org.oregami.service.ServiceResult;
import org.oregami.service.TopLevelEntityService;

import javax.persistence.OptimisticLockException;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by sebastian on 18.03.15.
 */
public abstract class ResourceHelper {

    public static Response create(User user, BaseEntityUUID entity, TopLevelEntityService service) {
        try {
            ServiceCallContext context = new ServiceCallContext(user);
            ServiceResult<BaseEntityUUID> serviceResult = service.createNewEntity(entity, context);
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

    public static Response update(User user, String id, BaseEntityUUID entity, TopLevelEntityService service) {
        if (entity.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            ServiceCallContext context = new ServiceCallContext(user);
            ServiceResult<BaseEntityUUID> serviceResult = service.updateEntity(entity, context);
            if (serviceResult.hasErrors()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .type("text/json")
                        .entity(serviceResult.getErrors()).build();
            }
        } catch (OptimisticLockException e) {
            Logger.getLogger(ResourceHelper.class).warn("OptimisticLockException", e);
            return Response.status(Response.Status.BAD_REQUEST).tag("OptimisticLockException").build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(entity).build();
    }


    public static Response getRevision(String id, String revision, GenericDAOUUIDImpl dao) {
        BaseEntityUUID entity = dao.findRevision(id, Integer.parseInt(revision));
        if (entity != null) {
            return Response.ok(entity).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    public static Response getRevisions(String id, GenericDAOUUIDImpl dao) {
        List<RevisionInfo> revisionList = dao.findRevisions(id);
        if (revisionList != null) {
            return Response.ok(revisionList).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    public static Response get(String id, GenericDAOUUIDImpl dao) {
        BaseEntityUUID entity = dao.findOne(id);
        if (entity != null) {
            return Response.ok(entity).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    public static Response delete(User user, String id, TopLevelEntityService service) {
        try {
            service.deleteEntity(id);
        } catch (NoSuchElementException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}
