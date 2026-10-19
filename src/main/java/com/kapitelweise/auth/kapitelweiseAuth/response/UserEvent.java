package com.kapitelweise.auth.kapitelweiseAuth.response;

public record UserEvent(EventType type, Long userId, String username) {
}
