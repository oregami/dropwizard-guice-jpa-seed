package org.oregami.resources;

import com.google.inject.Inject;
import org.oregami.entities.CustomRevisionEntity;
import org.oregami.entities.RevisionEntityDao;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/revisions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RevisionResource {

	@Inject
	private RevisionEntityDao dao = null;

	public RevisionResource() {
	}

	@GET
	public List<CustomRevisionEntity> list() {
		List<CustomRevisionEntity> ret = dao.findAll();
		return ret;
	}

}
