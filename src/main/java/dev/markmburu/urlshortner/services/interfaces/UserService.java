package dev.markmburu.urlshortner.services.interfaces;

import dev.markmburu.urlshortner.dtos.UserDto;
import dev.markmburu.urlshortner.dtos.requests.PaginatedRequest;
import dev.markmburu.urlshortner.dtos.responses.PaginatedResponse;
import dev.markmburu.urlshortner.exceptios.EntityExists;

public interface UserService {
    PaginatedResponse<UserDto> getUsers(PaginatedRequest request);
    UserDto createUser(UserDto user) throws EntityExists;
}
