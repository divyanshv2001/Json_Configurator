package com.example.demo.repository;

import com.example.demo.domain.model.DatasetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DatasetRecordRepository extends JpaRepository<DatasetRecord, Long> {
  List<DatasetRecord> findAllByDatasetName(String datasetName);
  boolean existsByDatasetNameAndRecordId(String datasetName, Long recordId);
}
