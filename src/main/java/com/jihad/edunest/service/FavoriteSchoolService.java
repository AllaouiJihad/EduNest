package com.jihad.edunest.service;

import com.jihad.edunest.web.vms.responce.school.FavoriteSchoolDTO;

import java.util.List;

public interface FavoriteSchoolService {

    void addFavoriteSchool(String username, Long schoolId, String notes);
    void removeFavoriteSchool(String username, Long schoolId);
    List<FavoriteSchoolDTO> getFavoriteSchools(String username);
}
