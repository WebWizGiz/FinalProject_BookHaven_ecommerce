package org.dci.bookhaven.repository;

import org.dci.bookhaven.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    UserType findByUserTypeName(String userTypeName);

}