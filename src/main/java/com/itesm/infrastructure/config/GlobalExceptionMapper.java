package com.itesm.infrastructure.config;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        log.error("Excepción global capturada por GlobalExceptionMapper:", exception);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String stackTrace = sw.toString();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "Error interno en el servidor");
        responseBody.put("message", exception.getMessage());
        responseBody.put("cause", exception.getCause() != null ? exception.getCause().toString() : "Ninguna");
        responseBody.put("stackTrace", stackTrace); // Enviaremos el stackTrace directo a Postman para diagnosticar

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(responseBody)
                .build();
    }
}
