package ml.intellirn.api.controller;

import ml.intellirn.api.model.*;
import ml.intellirn.api.repository.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;

@CrossOrigin
@RestController
@Transactional
@RequestMapping("api/education")
public class EducationController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EducationRepository educationRepository;

    private List<Education> searchEducationById(Long id) {
        Optional<Education> educationOptional = this.educationRepository.findById(id);

        if (educationOptional.isPresent()) {
            List<Education> searchResults = new ArrayList<>();
            searchResults.add(educationOptional.get());
            return searchResults;
        }

        else {
            return null;
        }
    }

    private List<Education> searchEducationByEducationUrl(List<Education> allEducations, String sT) {
        String searchTerm = sT.toLowerCase();
        List<Education> searchResults = new ArrayList<>();

        for (Education eachEducation : allEducations) {
            if (eachEducation.getEducationUrl() != null
                    && eachEducation.getEducationUrl().equalsIgnoreCase(searchTerm)) {
                searchResults.add(eachEducation);
            }
        }

        return searchResults;
    }

    private List<Education> searchEducationByTitle(List<Education> allEducations, String sT) {
        String searchTerm = sT.toLowerCase();
        List<Education> searchResults = new ArrayList<>();

        for (Education eachEducation : allEducations) {
            if (eachEducation.getTitle() != null && eachEducation.getTitle().contains(searchTerm)) {
                searchResults.add(eachEducation);
            }
        }

        return searchResults;
    }

    private List<Education> searchEducationByCategoryName(List<Education> allEducations, String sT) {
        String searchTerm = sT.toLowerCase();
        List<Education> searchResults = new ArrayList<>();

        for (Education eachEducation : allEducations) {
            if (eachEducation.getCategory() != null
                    && eachEducation.getCategory().getName().equalsIgnoreCase(searchTerm)) {
                searchResults.add(eachEducation);
            }
        }

        return searchResults;
    }

    private List<Education> searchEducationByCategoryId(List<Education> allEducations, String sT) {
        String searchTerm = sT.toLowerCase();
        List<Education> searchResults = new ArrayList<>();

        try {
            int suppliedCategoryId = Integer.parseInt(searchTerm);

            for (Education eachEducation : allEducations) {
                if (eachEducation.getCategory() != null
                        && eachEducation.getCategory().getCategoryId() == suppliedCategoryId) {
                    searchResults.add(eachEducation);
                }
            }

            return searchResults;
        }

        catch (NumberFormatException e) {
            return searchResults;
        }
    }

    @GetMapping(path = "{educationId}")
    public ResponseEntity<?> getEducationById(@PathVariable("educationId") Long id) {
        List<Education> e = this.searchEducationById(id);

        if (e == null) {
            String message = String.format("Education with ID %d not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorBody(HttpStatus.NOT_FOUND, message));
        }

        else {
            return ResponseEntity.status(HttpStatus.OK).body(e.get(0));
        }
    }

    @GetMapping()
    public ResponseEntity<?> searchEducations(@RequestParam(name = "searchBy", required = false) String searchBy,
            @RequestParam(name = "searchTerm", required = false) String searchTerm) {

        List<Education> allEducations = this.educationRepository.findAll();

        if (searchBy == null || searchTerm == null) {
            return ResponseEntity.status(HttpStatus.OK).body(allEducations);
        }

        else if (searchBy.equals("") || searchTerm.equals("")) {
            return ResponseEntity.status(HttpStatus.OK).body(allEducations);
        }

        else if (searchBy.equalsIgnoreCase("educationurl")) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.searchEducationByEducationUrl(allEducations, searchTerm));
        }

        else if (searchBy.equalsIgnoreCase("title")) {
            return ResponseEntity.status(HttpStatus.OK).body(this.searchEducationByTitle(allEducations, searchTerm));
        }

        else if (searchBy.equalsIgnoreCase("categoryname")) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.searchEducationByCategoryName(allEducations, searchTerm));
        }

        else if (searchBy.equalsIgnoreCase("categoryid")) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.searchEducationByCategoryId(allEducations, searchTerm));
        }

        else {
            String message = "Invalid education search operation";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorBody(HttpStatus.BAD_REQUEST, message));
        }
    }

    private boolean isEducationUrlUnique(Education e) {
        boolean flag = true;
        String suppliedEducationUrl = e.getEducationUrl();

        if (suppliedEducationUrl == null || suppliedEducationUrl.equals("")) {
            flag = false;
        }

        else {
            List<Education> allEducations = this.educationRepository.findAll();

            for (Education eachEducation : allEducations) {
                if (eachEducation.getEducationId() == e.getEducationId()) {
                    continue;
                }

                if (eachEducation.getEducationUrl() != null
                        && eachEducation.getEducationUrl().equalsIgnoreCase(suppliedEducationUrl)) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }

    private boolean isEducationUrlValid(Education e) {
        String regex = "^[a-zA-Z0-9-_]+$";
        String suppliedEducationUrl = e.getEducationUrl();

        return suppliedEducationUrl.matches(regex);
    }

    @PostMapping
    public ResponseEntity<?> addEducation(@RequestBody Education e) {
        e.setEducationId(0L);
        e.setCreationDate(LocalDate.now());
        e.setLastUpdateDate(LocalDate.now());

        if (this.isEducationUrlValid(e)) {
            if (this.isEducationUrlUnique(e)) {
                Optional<Category> suppliedOptionalCategory = this.categoryRepository
                        .findById(e.getCategory().getCategoryId());

                if (!suppliedOptionalCategory.isPresent()) {
                    String message = String.format("Cannot find category with ID %d", e.getCategory().getCategoryId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ErrorBody(HttpStatus.BAD_REQUEST, message));
                }

                else {
                    Category suppliedCategory = suppliedOptionalCategory.get();
                    suppliedCategory.getEducationList().add(e);

                    // this.categoryRepository.save(suppliedCategory);
                    e.setCategory(suppliedCategory);
                    this.educationRepository.save(e);

                    String message = "Education added successfully";
                    return ResponseEntity.status(HttpStatus.OK).body(new ErrorBody(HttpStatus.OK, message));
                }
            }

            else {
                String message = String.format("\"%s\" URL is already taken", e.getEducationUrl());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorBody(HttpStatus.BAD_REQUEST, message));
            }
        }

        else {
            String message = String.format("\"%s\" URL is not valid", e.getEducationUrl());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorBody(HttpStatus.BAD_REQUEST, message));
        }
    }

    @PutMapping(path = "{educationId}")
    public ResponseEntity<?> updateEducation(@RequestBody Education e, @PathVariable("educationId") Long educationId) {
        Optional<Education> educationOptional = this.educationRepository.findById(educationId);

        if (educationOptional.isPresent()) {
            Education originalEducation = educationOptional.get();
            e.setEducationId(originalEducation.getEducationId());

            if (this.isEducationUrlValid(e)) {
                if (this.isEducationUrlUnique(e)) {
                    Optional<Category> suppliedOptionalCategory = this.categoryRepository
                            .findById(e.getCategory().getCategoryId());

                    if (!suppliedOptionalCategory.isPresent()) {
                        String message = String.format("Cannot find category with ID %d",
                                e.getCategory().getCategoryId());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ErrorBody(HttpStatus.BAD_REQUEST, message));
                    }

                    else {
                        Category suppliedCategory = suppliedOptionalCategory.get();
                        e.setCategory(suppliedCategory);

                        List<Education> newEducationList = new ArrayList<>();
                        List<Education> existingEducationList = suppliedCategory.getEducationList();

                        for (Education eachEducation : existingEducationList) {
                            if (eachEducation.getEducationId() != e.getEducationId()) {
                                newEducationList.add(eachEducation);
                            }

                            else {
                                newEducationList.add(e);
                            }
                        }

                        suppliedCategory.setEducationList(newEducationList);
                        this.categoryRepository.save(suppliedCategory);
                        this.educationRepository.save(e);

                        String message = "Education updated successfully";
                        return ResponseEntity.status(HttpStatus.OK).body(new ErrorBody(HttpStatus.OK, message));
                    }
                }

                else {
                    String message = String.format("\"%s\" URL is already taken", e.getEducationUrl());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ErrorBody(HttpStatus.BAD_REQUEST, message));
                }
            }

            else {
                String message = String.format("\"%s\" URL is not valid", e.getEducationUrl());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorBody(HttpStatus.BAD_REQUEST, message));
            }

        }

        else {
            String message = String.format("Education with ID %d not found", educationId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorBody(HttpStatus.NOT_FOUND, message));
        }
    }

    @DeleteMapping(path = "{educationId}")
    public ResponseEntity<?> deleteEducation(@PathVariable("educationId") Long educationId) {
        if (!this.educationRepository.existsById(educationId)) {
            String message = String.format("Education with ID %d not found", educationId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorBody(HttpStatus.NOT_FOUND, message));
        }

        else {
            Education toBeDelete = this.educationRepository.findById(educationId).get();

            Category categoryOfToBeDelete = toBeDelete.getCategory();

            List<Education> newEducationList = new ArrayList<>();
            List<Education> existingEducationList = categoryOfToBeDelete.getEducationList();

            for (Education eachEducation : existingEducationList) {
                if (eachEducation.getEducationId() != toBeDelete.getEducationId()) {
                    newEducationList.add(eachEducation);
                }

                else {
                    continue;
                }
            }
            categoryOfToBeDelete.setEducationList(newEducationList);

            this.educationRepository.deleteById(educationId);
            this.categoryRepository.save(categoryOfToBeDelete);

            String message = String.format("Education with ID %d deleted successfully", educationId);
            return ResponseEntity.status(HttpStatus.OK).body(new ErrorBody(HttpStatus.OK, message));
        }
    }
}
