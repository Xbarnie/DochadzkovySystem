package sk.majo.attendancesystem.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sk.majo.attendancesystem.entities.Department;
import sk.majo.attendancesystem.repositories.DepartmentRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/api/department")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }
}
