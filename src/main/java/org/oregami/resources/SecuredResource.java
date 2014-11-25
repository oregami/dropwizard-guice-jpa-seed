package org.oregami.resources;

import com.github.toastshaman.dropwizard.auth.jwt.hmac.HmacSHA512Signer;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebToken;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebTokenClaim;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebTokenHeader;
import io.dropwizard.auth.Auth;
import org.joda.time.DateTime;
import org.oregami.dropwizard.User;
import org.oregami.service.UserServiceFake;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/jwt")
@Produces(APPLICATION_JSON)
public class SecuredResource {

  private final byte[] tokenSecret;

  public SecuredResource(byte[] tokenSecret) {
    this.tokenSecret = tokenSecret;
  }

  @POST
  @Path("/login")
  public Response generate(
    @FormParam("username") String username,
    @FormParam("password") String password,
    @FormParam("expireInSeconds") Integer expireInSeconds
  ) {
    if (username==null || password==null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    if (expireInSeconds==null) {
      expireInSeconds = 60*30; //30 minutes
    }
    //verify user/password against the UserService:
    if (!UserServiceFake.getInstance().checkCredentials(username, password)) {
      //no Match! Return error:
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    //User-verification ok, continue and create token:
    final HmacSHA512Signer signer = new HmacSHA512Signer(tokenSecret);
    final JsonWebToken token = JsonWebToken.builder()
      .header(JsonWebTokenHeader.HS512())
      .claim(JsonWebTokenClaim.builder()
        .param("username", username)
        .issuedAt(new DateTime())
        .expiration(new DateTime().plusSeconds(expireInSeconds))
        .build())
      .build();
    final String signedToken = signer.sign(token);
    return Response.ok(singletonMap("token", signedToken)).build();
  }

  @GET
  @Path("/check-token")
  public Map<String, String> get(@Auth User user) {
    return singletonMap("username", user.getUsername());
  }

  @GET
  @Path("/test")
  public String test(@Auth User user) {
    return "if you can see this then your request was authenticated successfully!";
  }
}
