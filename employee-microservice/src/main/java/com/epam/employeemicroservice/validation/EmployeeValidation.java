package com.epam.employeemicroservice.validation;

import com.epam.employeemicroservice.dto.EmployeeDTO;
import com.epam.employeemicroservice.entity.Employee;
import com.epam.employeemicroservice.exception.ValidationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;

/**
 * Util class to provide validations methods for employee data constraints
 */
public class EmployeeValidation {

    private EmployeeValidation() {

    }

    /**
     * For full validation of employeeDTO
     *
     * @param employeeDTOValidate employee to validate
     * @param bindingResult       collecting validation errors
     * @param manager             the manager of the employee
     */
    public static void validateAll(EmployeeDTO employeeDTOValidate, BindingResult bindingResult, EmployeeDTO manager) {

        if (bindingResult == null) {
            bindingResult = new BeanPropertyBindingResult(employeeDTOValidate, "EmployeeTo");
        }

        LocalDate dateOfBirth = employeeDTOValidate.getDateOfBirth();
        LocalDate hiringDate = employeeDTOValidate.getHiringDate();
        LocalDate dismissalDate = employeeDTOValidate.getDismissalDate();


        if (hiringDate.isBefore(dateOfBirth)) {
            bindingResult.addError(new ObjectError("EmployeeTo", "Hiring date should be later than date of birth"));
        }

        if (dismissalDate != null && dismissalDate.isBefore(hiringDate)) {
            bindingResult.addError(new ObjectError("EmployeeTo", "Dismissal date should be later than hiring date"));
        }

        if (manager != null && manager.getSalary() < employeeDTOValidate.getSalary()) {
            bindingResult.addError(new ObjectError("EmployeeTo", "Employee salary should be less than manager's one"));
        }

        if (manager != null && employeeDTOValidate.getManager()) {
            bindingResult.addError(new ObjectError("EmployeeTo", "Head of the department has already defined"));
        }

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
    }

    /**
     * For full validation of employee
     *
     * @param employeeValidate employee to validate
     * @param bindingResult       collecting validation errors
     * @param manager             the manager of the employee
     */
    public static void validateAll(Employee employeeValidate, BindingResult bindingResult, EmployeeDTO manager) {

        if (bindingResult == null) {
            bindingResult = new BeanPropertyBindingResult(employeeValidate, "EmployeeTo");
        }

        LocalDate dateOfBirth = employeeValidate.getDateOfBirth();
        LocalDate hiringDate = employeeValidate.getHiringDate();
        LocalDate dismissalDate = employeeValidate.getDismissalDate();


        if (hiringDate.isBefore(dateOfBirth)) {
            bindingResult.addError(new ObjectError("EmployeeTo", "Hiring date should be later than date of birth"));
        }

        if (dismissalDate != null && dismissalDate.isBefore(hiringDate)) {
            bindingResult.addError(new ObjectError("EmployeeTo", "Dismissal date should be later than hiring date"));
        }

        if (manager != null && manager.getSalary() < employeeValidate.getSalary()) {
            bindingResult.addError(new ObjectError("EmployeeTo", "Employee salary should be less than manager's one"));
        }

        if (manager != null && employeeValidate.getManager()) {
            bindingResult.addError(new ObjectError("EmployeeTo", "Head of the department has already defined"));
        }

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
    }


    /**
     * For dismissal operation only. Validates just employee dismissal date.
     */
    public static void validateDismissalDate(Employee employeeToValidate) {

        BindingResult bindingResult = new BeanPropertyBindingResult(employeeToValidate, "EmployeeTo");

        LocalDate hiringDate = employeeToValidate.getHiringDate();
        LocalDate dismissalDate = employeeToValidate.getDismissalDate();


        if (dismissalDate != null && dismissalDate.isBefore(hiringDate)) {
            bindingResult.addError(new ObjectError("EmployeeTo", "Dismissal date should be later than hiring date"));
            throw new ValidationException(bindingResult.getAllErrors());
        }

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
    }
}
