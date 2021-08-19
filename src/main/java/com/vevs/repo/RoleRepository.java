package com.vevs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vevs.entity.common.Role;
import com.vevs.entity.common.RoleName;

import java.util.Optional;

/**
 * Created by Anuj kumar pal
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
