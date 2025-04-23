package hr.java.spring.foodstore.foodstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.java.spring.foodstore.foodstore.dto.FoodItemDTO;
import hr.java.spring.foodstore.foodstore.model.FoodCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FoodStoreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private FoodItemDTO sampleFoodItem;

    @BeforeEach
    void setup() {
        sampleFoodItem = new FoodItemDTO(
                1,
                "Apple",
                FoodCategory.FRUIT,   // Assuming enum: FRUIT, VEGETABLE, etc.
                52,
                new BigDecimal("0.50"),
                new BigDecimal("1.00")
        );
    }

    @Test
    void testGetAllFoodItems() throws Exception {
        mockMvc.perform(get("/foodStore/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(List.class)));
    }

    @Test
    void testGetFoodItemsByName() throws Exception {
        mockMvc.perform(get("/foodStore/foodItem/Apple"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetFoodItemWithMinKcal() throws Exception {
        mockMvc.perform(get("/foodStore/foodItem/minKcal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kcal", notNullValue()));
    }

    @Test
    void testSaveNewFoodItem_ValidInput() throws Exception {
        FoodItemDTO newItem = new FoodItemDTO(
                null,
                "Banana",
                FoodCategory.FRUIT,
                89,
                new BigDecimal("0.40"),
                new BigDecimal("0.90")
        );

        mockMvc.perform(post("/foodStore/foodItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Banana")));
    }

    @Test
    void testUpdateFoodItem_Valid() throws Exception {
        sampleFoodItem.setName("Updated Apple");

        mockMvc.perform(put("/foodStore/foodItem/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleFoodItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Apple")));
    }

    @Test
    void testUpdateFoodItem_IdMismatch() throws Exception {
        sampleFoodItem.setId(2); // ID mismatch

        mockMvc.perform(put("/foodStore/foodItem/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleFoodItem)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateFoodItem_NotFound() throws Exception {
        FoodItemDTO notFoundItem = new FoodItemDTO(
                9999,
                "Ghost",
                FoodCategory.OTHER,
                10,
                new BigDecimal("0.20"),
                new BigDecimal("0.50")
        );

        mockMvc.perform(put("/foodStore/foodItem/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notFoundItem)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteFoodItem() throws Exception {
        mockMvc.perform(delete("/foodStore/1"))
                .andExpect(status().isNoContent());
    }
}

