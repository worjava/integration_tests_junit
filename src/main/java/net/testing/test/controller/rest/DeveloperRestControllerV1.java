package net.testing.test.controller.rest;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import net.testing.test.dto.DeveloperDto;
import net.testing.test.dto.DtoError;
import net.testing.test.entity.DeveloperEntity;
import net.testing.test.entity.Status;
import net.testing.test.exceptiom.DeveloperNotFoundException;
import net.testing.test.exceptiom.DeveloperWithDuplicateExecution;
import net.testing.test.service.DeveloperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/developers")
@RequiredArgsConstructor
public class DeveloperRestControllerV1 {

    private final DeveloperService developerService;


    @PostMapping
    public ResponseEntity<?> createDeveloper(@RequestBody DeveloperDto dto) {
        try {
            DeveloperEntity entity = dto.toEntity();

            DeveloperEntity createdDeveloper = developerService.saveDeveloper(entity);
            DeveloperDto result = DeveloperDto.fromEntity(createdDeveloper);
            return ResponseEntity.ok(result);
        } catch (DeveloperWithDuplicateExecution e) {
            return ResponseEntity.badRequest()
                    .body(DtoError.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateDeveloper(@RequestBody DeveloperDto developerDto) {
        try {
            DeveloperEntity entity = developerDto.toEntity();

            DeveloperEntity update = developerService.updateDeveloper(entity);
            DeveloperDto dto = DeveloperDto.fromEntity(update);
            return ResponseEntity.ok(dto);
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body(DtoError.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDeveloperById(@PathVariable Integer id) {
        try {
            DeveloperEntity developer = developerService.getDeveloperById(id);
            DeveloperDto result = DeveloperDto.fromEntity(developer);
            return ResponseEntity.ok(result);
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(DtoError.builder()
                            .status(404)
                            .message(e.getMessage())
                            .build());
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllDevelopers() {
        List<DeveloperEntity> developers = developerService.getAllDeveloper();
        List<DeveloperDto> developerDto = developers.stream()
                .map(DeveloperDto::fromEntity).toList();
        return ResponseEntity.ok(developerDto);

    }

    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<?> getAllDevelopersBySpecialty(@PathVariable String specialty) {
        List<DeveloperEntity> entities = developerService.getDeveloperActiveBySpecialty(specialty);
        List<DeveloperDto> developerDtos = entities.stream().map(
                DeveloperDto::fromEntity).toList();
        return ResponseEntity.ok(developerDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeveloperById(@PathVariable("id") Integer id,
                                                 @RequestParam(value = "isHard", defaultValue = "false") boolean isHard) {
        try {
            if (isHard) {
                developerService.hardDeleteById(id);
            } else {
                developerService.softDeleteById(id);
            }
            return ResponseEntity.ok().build();
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body(DtoError.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build());
        }
    }


}