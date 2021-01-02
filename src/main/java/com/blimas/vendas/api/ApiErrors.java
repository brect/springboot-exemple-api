package com.blimas.vendas.api;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ApiErrors {


    private List<String> errors;

    public ApiErrors(List<String> errors) {
        this.errors = errors;
    }

    public ApiErrors(String errors) {
        this.errors = Arrays.asList(errors);
    }
}
