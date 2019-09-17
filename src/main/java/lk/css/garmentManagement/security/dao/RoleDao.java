package lk.css.garmentManagement.security.dao;


import lk.css.garmentManagement.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface RoleDao extends JpaRepository< Role, Long> {
}
