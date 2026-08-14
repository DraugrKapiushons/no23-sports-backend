package org.no23sports.membershipservice.repository;

import org.no23sports.membershipservice.model.MembershipPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipPackageRepo extends JpaRepository<MembershipPackage, Integer> {
}
