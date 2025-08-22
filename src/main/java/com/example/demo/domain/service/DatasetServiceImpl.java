package com.example.demo.domain.service;

import com.example.demo.domain.model.DatasetRecord;
import com.example.demo.repository.DatasetRecordRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DatasetServiceImpl implements DatasetService {

  private final DatasetRecordRepository repository;
  private final ObjectMapper mapper;

  public DatasetServiceImpl(DatasetRecordRepository repository, ObjectMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Long insertRecord(String datasetName, Map<String, Object> recordMap) {
    if (!StringUtils.hasText(datasetName) || recordMap == null || recordMap.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
    }

    Long recordId = null;
    Object idVal = recordMap.get("id");
    if (idVal instanceof Number n) recordId = n.longValue();
    else if (idVal instanceof String s && s.matches("-?\\d+")) recordId = Long.parseLong(s);

    if (recordId != null && repository.existsByDatasetNameAndRecordId(datasetName, recordId)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate id for dataset");
    }

    try {
      String payload = mapper.writeValueAsString(recordMap);
      DatasetRecord entity = new DatasetRecord();
      entity.setDatasetName(datasetName);
      entity.setRecordId(recordId);
      entity.setPayload(payload);
      repository.save(entity);
      return recordId;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON");
    }
  }

  @Override
  public Map<String, List<Map<String, Object>>> queryGrouped(String datasetName, String groupBy) {
    if (!StringUtils.hasText(groupBy)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "groupBy is required");
    }
    List<Map<String, Object>> records = new ArrayList<>();
    for (DatasetRecord r : repository.findAllByDatasetName(datasetName)) {
      try {
        records.add(mapper.readValue(r.getPayload(), new TypeReference<>() {}));
      } catch (Exception ignored) {}
    }
    return records.stream().collect(Collectors.groupingBy(
        rec -> String.valueOf(rec.getOrDefault(groupBy, "null")),
        LinkedHashMap::new,
        Collectors.toCollection(ArrayList::new)
    ));
  }

  @Override
  public List<Map<String, Object>> querySorted(String datasetName, String sortBy, boolean ascending) {
    if (!StringUtils.hasText(sortBy)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sortBy is required");
    }
    List<Map<String, Object>> records = new ArrayList<>();
    for (DatasetRecord r : repository.findAllByDatasetName(datasetName)) {
      try {
        records.add(mapper.readValue(r.getPayload(), new TypeReference<>() {}));
      } catch (Exception ignored) {}
    }

    Comparator<Map<String, Object>> cmp = Comparator.comparing(
        m -> {
          Object v = m.get(sortBy);
          if (v == null) return null;
          if (v instanceof Number n) return n.doubleValue();
          if (v instanceof Boolean b) return b ? 1 : 0;
          return String.valueOf(v);
        },
        (a, b) -> {
          if (a == b) return 0;
          if (a == null) return 1;
          if (b == null) return -1;
          if (a instanceof Comparable<?> ca && a.getClass().isInstance(b)) {
            @SuppressWarnings("unchecked") Comparable<Object> c = (Comparable<Object>) ca;
            return c.compareTo(b);
          }
          return String.valueOf(a).compareTo(String.valueOf(b));
        }
    );
    if (!ascending) cmp = cmp.reversed();
    records.sort(cmp);
    return records;
  }
}
