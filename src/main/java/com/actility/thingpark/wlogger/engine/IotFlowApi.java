package com.actility.thingpark.wlogger.engine;

import com.actility.thingpark.wlogger.engine.model.DecodeBatchInputItem;
import com.actility.thingpark.wlogger.engine.model.DecodeBatchOutputItem;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/v1")
// @RegisterRestClient(configKey = "engine-api")
@RegisterRestClient
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public interface IotFlowApi {

  @POST
  @Path("/decode/batch")
  List<DecodeBatchOutputItem> decodeBatch(List<DecodeBatchInputItem> decodeBatchInput,
                                          @QueryParam("account") String subscriberId,
                                          @HeaderParam("X-Realm-ID") String realmId);
}
