package it.angelobabini.calcifer;

import it.angelobabini.calcifer.stf.backend.data.Ricognizione;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/rest")
public class RESTServer {
	
	@GET
	@Path("/ricognizioni/exists/{param}")
	public Response existsRicognizione(@PathParam("param") String msg) {
		String result = "212";
		
		return Response.status(200).entity(result).build();
	}
	
	@GET
	@Path("/ricognizioni/get/{param}")
	@Produces("application/json")
	public Ricognizione getRicognizione(@PathParam("param") String msg) {
		Ricognizione ricognizione = null;

		return ricognizione;
	}
	
	@POST
	@Path("/ricognizioni")
	@Produces("application/json")
	public Boolean getProductInJSON() {
		return false;
	}
}
