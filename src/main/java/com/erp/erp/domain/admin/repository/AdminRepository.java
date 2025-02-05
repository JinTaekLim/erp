package com.erp.erp.domain.admin.repository;

import com.erp.erp.domain.admin.common.entity.Admin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

  Optional<Admin> findByIdentifierAndPassword(String identifier, String password);

}
