package com.example.demo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DatasetApiSmokeTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testInsertAndQuery() throws Exception {
    // Insert a record
    String json = """
      { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" }
    """;

    mockMvc.perform(post("/api/dataset/test_dataset/record")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.dataset").value("test_dataset"))
        .andExpect(jsonPath("$.recordId").value(1));

    
    mockMvc.perform(get("/api/dataset/test_dataset/query")
            .param("groupBy", "department"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.groupedRecords.Engineering[0].name").value("John Doe"));

    
    mockMvc.perform(get("/api/dataset/test_dataset/query")
            .param("sortBy", "age")
            .param("order", "asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sortedRecords[0].id").value(1));
  }
}
