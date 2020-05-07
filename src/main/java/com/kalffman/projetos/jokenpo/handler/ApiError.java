package com.kalffman.projetos.jokenpo.handler;

public class ApiError {
    public static final String NOT_FOUND = "Recurso não encontrado";
    public static final String BAD_REQUEST = "Não permitido";

    private final String error;
    private final String description;

    public ApiError(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }

    public static ApiError buildError(String error, String description){
        return new ApiError(error, description);
    }
}
