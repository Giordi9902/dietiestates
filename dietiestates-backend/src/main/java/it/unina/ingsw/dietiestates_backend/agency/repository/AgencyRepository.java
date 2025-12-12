package it.unina.ingsw.dietiestates_backend.agency.repository;

import it.unina.ingsw.dietiestates_backend.agency.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency,Integer> {
}