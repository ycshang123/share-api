package com.ycshang.contentservice.repository;

import com.ycshang.contentservice.domain.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share,Integer> {
}
