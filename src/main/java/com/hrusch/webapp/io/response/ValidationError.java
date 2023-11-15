package com.hrusch.webapp.io.response;

public record ValidationError(String identifier, String errorMessage) {
}
