package com.example.demo.domain.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "dataset_record",
       uniqueConstraints = @UniqueConstraint(name = "uq_dataset_record",
         columnNames = {"dataset_name","record_id"}))
public class DatasetRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "dataset_name", nullable = false, length = 128)
  private String datasetName;

  @Column(name = "record_id")
  private Long recordId;

  @Lob
  @Column(name = "payload", nullable = false)
  private String payload;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @PrePersist
  void onCreate() {
    var now = OffsetDateTime.now();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = OffsetDateTime.now();
  }

  // getters/setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getDatasetName() { return datasetName; }
  public void setDatasetName(String datasetName) { this.datasetName = datasetName; }

  public Long getRecordId() { return recordId; }
  public void setRecordId(Long recordId) { this.recordId = recordId; }

  public String getPayload() { return payload; }
  public void setPayload(String payload) { this.payload = payload; }

  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

  public OffsetDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
