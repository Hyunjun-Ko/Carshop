package com.packt.cardatabase.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

//@RepositoryRestResource(path="vehicles") this changes the endpoint path of Car entity
// from /api/cars to /api/vehicles
@RepositoryRestResource
public interface CarRepository extends CrudRepository<Car, Long> {
    @Query("select c from Car c where c.brand = ?1")
    List<Car> findByBrand(@Param("brand") String brand);

    @Query("select c from Car c where c.brand like %?1")
    List<Car> findByBrandEndsWith(String brand);

    List<Car> findByColor(@Param("color") String color);

    List<Car> findByYear(int year);

    List<Car> findByBrandAndModel(String brand, String model);

    List<Car> findByBrandOrColor(String brand, String color);

    List<Car> findByBrandOrderByYearAsc(String brand);
}
