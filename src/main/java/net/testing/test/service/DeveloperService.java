package net.testing.test.service;

import net.testing.test.entity.DeveloperEntity;

import java.util.List;

public interface DeveloperService {

    DeveloperEntity saveDeveloper(DeveloperEntity entity);

    DeveloperEntity updateDeveloper(DeveloperEntity entity);

    DeveloperEntity getDeveloperById(Integer id);

    DeveloperEntity getDeveloperByEmail(String email);


    List<DeveloperEntity> getAllDeveloper();


    List<DeveloperEntity> getDeveloperActiveBySpecialty(String specialty);

    void softDeleteById(Integer id);

    void hardDeleteById(Integer id);


}
