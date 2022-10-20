package com.ycshang.contentservice.repository;

import com.ycshang.contentservice.domain.entity.Share;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Integer>, JpaSpecificationExecutor<Share> {

    Page<Share> findAll(Pageable pageable);

}
