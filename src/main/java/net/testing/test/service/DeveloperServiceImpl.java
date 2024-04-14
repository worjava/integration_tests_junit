package net.testing.test.service;

import lombok.RequiredArgsConstructor;
import net.testing.test.entity.DeveloperEntity;
import net.testing.test.entity.Status;
import net.testing.test.exceptiom.DeveloperNotFoundException;
import net.testing.test.exceptiom.DeveloperWithDuplicateExecution;
import net.testing.test.repository.DeveloperRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {


    private final DeveloperRepository developerRepository;

    @Override
    public DeveloperEntity saveDeveloper(DeveloperEntity entity) {
        DeveloperEntity candidateDuplicateEntity = developerRepository.
                findByEmail(entity.getEmail());
        if (Objects.nonNull(candidateDuplicateEntity)) {
            throw new DeveloperWithDuplicateExecution("Developer with defined email is already exist");
        }
        entity.setStatus(Status.ACTIVE);
        return developerRepository.save(entity);
    }

    @Override
    public DeveloperEntity updateDeveloper(DeveloperEntity entity) {
        boolean isExist = developerRepository.existsById(entity.getId());

        if (!isExist) {
            throw new DeveloperNotFoundException("Developer not found");
        }
        return developerRepository.save(entity);
    }


    @Override
    public DeveloperEntity getDeveloperById(Integer id) {

        return developerRepository.findById(id).orElseThrow(
                () -> new DeveloperNotFoundException("Developer not found")
        );


    }

    @Override
    public DeveloperEntity getDeveloperByEmail(String email) {
        DeveloperEntity developerEntity = developerRepository.findByEmail(email);

        if (Objects.isNull(developerEntity)) {
            throw new DeveloperNotFoundException("Developer not found");
        }
        return developerEntity;
    }

    @Override
    public List<DeveloperEntity> getAllDeveloper() {
        return developerRepository.findAll()
                .stream().filter(d -> {
                    return d.getStatus().equals(Status.ACTIVE);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DeveloperEntity> getDeveloperActiveBySpecialty(String specialty) {
        return developerRepository.findByAllActiveBySpecialty(specialty);
    }

    @Override
    public void softDeleteById(Integer id) {
        DeveloperEntity developer = developerRepository.findById(id).orElseThrow(
                () -> new DeveloperNotFoundException("Developer not found")
        );
        developer.setStatus(Status.DELETED);
        developerRepository.save(developer);
    }

    @Override
    public void hardDeleteById(Integer id) {
        DeveloperEntity developer = developerRepository.findById(id).orElseThrow(
                () -> new DeveloperNotFoundException("Not found"));

        developerRepository.deleteById(developer.getId());
    }


}
