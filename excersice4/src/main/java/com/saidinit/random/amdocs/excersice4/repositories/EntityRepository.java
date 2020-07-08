package com.saidinit.random.amdocs.excersice4.repositories;

import com.saidinit.random.amdocs.excersice4.domain.Entity;

/**
 * The repository in JPA where you are accesing to store the data
 */
public interface EntityRepository {

	void save(Entity e);

}
