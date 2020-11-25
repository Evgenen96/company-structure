package com.epam.employeemicroservice.service;

import java.util.List;

/**
 * The base interface for any <i>service layer</i> interface/class.
 * Declares methods for <i>CRUD</i> operations
 *
 * @param <T> the type of elements that the service handles
 * @see EmployeeService
 * @see JobTitleService
 */
public interface BaseService<T> {

    /**
     * Fetches all existing data related to the type parameter
     * from the database.
     * @return List of found domain objects
     */
    List<T> findAll();

    /**
     * Fetches the domain object by its id from the database.
     * @param theId the primary key to determine the needed record in the database
     * @return the domain object
     */
    T findById(Long theId);

    /**
     * Saves the specified domain object to the database.
     * @param theDomain the object to be saved
     * @return the saved objects (with the gotten id)
     */
    T save(T theDomain);

    /**
     * Deletes the domain objects by its id from the database
     * @param theId the primary key to determine the needed record in the database
     */
    void deleteById(Long theId);
}
