package bg.project;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class ErrorHandler {

    private static final Logger logger = Logger.getLogger(ErrorHandler.class);

    @ServerExceptionMapper
    public Response handleInvalidProduct(InvalidProductException e){
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(400, e.getMessage()))
                .build();
    }


    @ServerExceptionMapper
    public Response handleWebApplication(WebApplicationException e){
        int status = e.getResponse().getStatus();

        return Response.status(status)
                .entity(new ErrorResponse(status, e.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleUnexpected(Exception e){
        logger.error("Unexpected error", e);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(500, "Internal server error"))
                .build();
    }
}
