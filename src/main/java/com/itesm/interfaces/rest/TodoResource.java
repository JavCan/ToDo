package com.itesm.interfaces.rest;

import com.itesm.application.dto.CreateTodoDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.usecase.CreateTodoUseCase;
import com.itesm.application.usecase.GetMyTodosUseCase;
import com.itesm.application.usecase.GetTodosByUserUseCase;
import com.itesm.domain.models.Todo;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/todo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TodoResource {

    private final CreateTodoUseCase createTodoUseCase;
    private final AuthenticatedUserContext authenticatedUserContext;
    private final GetTodosByUserUseCase getTodosByUserUseCase;
    private final GetMyTodosUseCase getMyTodosUseCase;

    @Inject
    public TodoResource(CreateTodoUseCase createTodoUseCase, 
                        AuthenticatedUserContext authenticatedUserContext,
                        GetTodosByUserUseCase getTodosByUserUseCase,
                        GetMyTodosUseCase getMyTodosUseCase) {
        this.createTodoUseCase = createTodoUseCase;
        this.authenticatedUserContext = authenticatedUserContext;
        this.getTodosByUserUseCase = getTodosByUserUseCase;
        this.getMyTodosUseCase = getMyTodosUseCase;
    }

    @POST
    public Response createTodo(CreateTodoDto createTodoDto){
        Todo todo= createTodoUseCase.execute(createTodoDto);
        return Response.ok(todo).build();
    }

    @Path("/test")
    @GET
    public Response getTodo(){
        System.out.println("Desde el endpoint: "+authenticatedUserContext.getCurrentUser().getFullName());
        return Response.ok("TEST").build();
    }

    @Path("/my-todos")
    @GET
    public Response getMyTodos() {
        List<Todo> todos = getMyTodosUseCase.execute();
        return Response.ok(todos).build();
    }

    @Path("/user/{userId}")
    @GET
    public Response getTodosByUser(@PathParam("userId") UUID userId) {
        List<Todo> todos = getTodosByUserUseCase.execute(userId);
        return Response.ok(todos).build();
    }

}
