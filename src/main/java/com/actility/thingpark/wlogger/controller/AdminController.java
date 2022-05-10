package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.dto.ResponseSimple;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.service.AdminService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/admins")
@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class AdminController {

    private AdminService service;

    @Inject
    void inject(final AdminService service) {
        this.service = service;
    }

    @POST
    @Path("/create")
    //    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseSimple add(
            @FormParam("login") String login, @FormParam("password") String password)
            throws WloggerException {
        return service.create(login, password);
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseSimple get(@PathParam("id") String id) throws WloggerException {
        return service.get(id);
    }

    @POST
    @Path("/get")
    //    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseSimple getByLogin(@FormParam("login") String login) throws WloggerException {
        return service.getByLogin(login);
    }

    @POST
    @Path("/delete")
    //    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseSimple deleteByLogin(@FormParam("login") String login) throws WloggerException {
        return service.deleteByLogin(login);
    }
}
