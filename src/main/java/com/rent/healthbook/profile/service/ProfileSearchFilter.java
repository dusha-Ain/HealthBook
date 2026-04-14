package com.rent.healthbook.profile.service;

public record ProfileSearchFilter(
        String email,
        String lastName,
        String firstName,
        String patronymic,
        Integer pageSize,
        Integer pageNumber
) {
}
