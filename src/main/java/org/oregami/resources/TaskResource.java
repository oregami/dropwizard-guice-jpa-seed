package org.oregami.resources;

import io.dropwizard.auth.Auth;

import java.util.List;

import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.oregami.entities.Task;
import org.oregami.entities.TaskDao;
import org.oregami.service.ServiceResult;
import org.oregami.service.TaskService;
import org.oregami.user.User;

import com.google.inject.Inject;


@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

	@Inject
	private TaskDao taskDao;
	
	@Inject
	private TaskService taskService;
	
	public TaskResource() {
	}
	
	
	@Path("{id}")
	@DELETE
	public Response delete(@Auth User user, @PathParam("id") String id) {
		Task task = taskDao.findOne(id);
		if (task==null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		taskDao.delete(task);
		return Response.ok().build();
	}
	  
	@GET
	public List<Task> list() {
		List<Task> ret = null;
		ret = taskDao.findAll();
		return ret;
	}
	
    @GET
    @Path("/{id}")
	public Response getTask(@PathParam("id") String id) {
    	Task task = taskDao.findOne(id);
    	if (task!=null) {
    		return Response.ok(task).build();
    	} else {
    		return Response.status(Response.Status.NOT_FOUND).build();
    	}
	}

    @GET
    @Path("/{id}/revisions")
    public Response getTaskRevisions(@PathParam("id") String id) {
        List<Number> revisionList = taskDao.findRevisions(id);
        if (revisionList!=null) {
            return Response.ok(revisionList).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/revisions/{revision}")
    public Response getTaskRevision(@PathParam("id") String id, @PathParam("revision") String revision) {
        Task t = taskDao.findRevision(id, Integer.parseInt(revision));
        if (t!=null) {
            return Response.ok(t).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
	@POST
	public Response create(Task t) {
		try {
			ServiceResult<Task> serviceResult = taskService.createNewTask(t);
			if (serviceResult.hasErrors()) {
				return Response.status(Status.BAD_REQUEST)
						.type("text/json")
		                .entity(serviceResult.getErrors()).build();
			}
			return Response.ok().build();
		} catch (Exception e) {
			return Response.status(Status.CONFLICT).type("text/plain")
	                .entity(e.getMessage()).build();
		}

	} 
	
	
	@PUT
	@Path("{id}")
	public Response update(@PathParam("id") String id, Task t) {
		if (t.getId()==null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			ServiceResult<Task> serviceResult = taskService.updateTask(t);
			if (serviceResult.hasErrors()) {
				return Response.status(Status.BAD_REQUEST)
						.type("text/json")
		                .entity(serviceResult.getErrors()).build();
			}			
		} catch (OptimisticLockException e) {
			Logger.getLogger(this.getClass()).warn("OptimisticLockException", e);
			return Response.status(Response.Status.BAD_REQUEST).tag("OptimisticLockException").build();
		}
		return Response.status(Response.Status.ACCEPTED).entity(t).build();
	}		
	
}
