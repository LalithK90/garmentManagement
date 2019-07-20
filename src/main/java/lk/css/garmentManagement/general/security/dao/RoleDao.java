package lk.css.garmentManagement.general.security.dao;

import lk.CSS.GarmentManagement.general.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {
}
