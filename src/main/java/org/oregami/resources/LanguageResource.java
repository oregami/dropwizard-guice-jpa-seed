package org.oregami.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import org.oregami.dropwizard.User;
import org.oregami.entities.Language;
import org.oregami.entities.LanguageDao;
import org.oregami.service.LanguageService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/language")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LanguageResource {

	@Inject
	private LanguageDao dao = null;

	@Inject
	private LanguageService service = null;

	public LanguageResource() {
	}


	@Path("{id}")
	@DELETE
	public Response delete(@Auth User user, @PathParam("id") String id) {
        return ResourceHelper.delete(user, id, service);
	}

	@GET
	public List<Language> list() {
		List<Language> ret = dao.findAll();
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
	public Response create(@Auth User user, Language l) {
		return ResourceHelper.create(user, l, service);
	}


	@PUT
	@Path("{id}")
	public Response update(@Auth User user, @PathParam("id") String id, Language l) {
		return ResourceHelper.update(user, id, l, service);
	}

}
