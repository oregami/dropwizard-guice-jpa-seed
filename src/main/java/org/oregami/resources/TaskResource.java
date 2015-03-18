package org.oregami.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import org.oregami.dropwizard.User;
import org.oregami.entities.Task;
import org.oregami.entities.TaskDao;
import org.oregami.service.TaskService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

	@Inject
	private TaskDao dao = null;

	@Inject
	private TaskService service = null;

	public TaskResource() {}

	@GET
	public List<Task> list() {
		List<Task> ret = dao.findAll();
		return ret;
	}

    @GET
    @Path("/{id}")
	public Response get(@PathParam("id") String id) {
        return ResourceHelper.get(id, dao);
	}

    @GET
    @Path("/{id}/revisions")
    public Response getRevisions(@PathParam("id") String id) {
        return ResourceHelper.getRevisions(id, dao);
    }

    @GET
    @Path("/{id}/revisions/{revision}")
    public Response getRevision(@PathParam("id") String id, @PathParam("revision") String revision) {
        return ResourceHelper.getRevision(id, revision, dao);
    }

	@POST
	public Response create(@Auth User user, Task t) {
		return ResourceHelper.create(user, t, service);
	}


	@PUT
	@Path("{id}")
	public Response update(@Auth User user, @PathParam("id") String id, Task t) {
		return ResourceHelper.update(user, id, t, service);
	}


    @DELETE
    @Path("{id}")
    public Response delete(@Auth User user, @PathParam("id") String id) {
        return ResourceHelper.delete(user, id, service);
    }
}
