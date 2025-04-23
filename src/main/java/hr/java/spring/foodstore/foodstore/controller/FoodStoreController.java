package hr.java.spring.foodstore.foodstore.controller;

import hr.java.spring.foodstore.foodstore.dto.FoodItemDTO;
import hr.java.spring.foodstore.foodstore.service.FoodService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/foodStore")
@AllArgsConstructor
@Slf4j
public class FoodStoreController {

    private FoodService foodService;

    @GetMapping("/all")
    public List<FoodItemDTO> getAllFoodItems() {
        log.info("getAllFoodItems called");
        return foodService.findAll();
    }

    @GetMapping("/foodItem/{name}")
    public List<FoodItemDTO> getFoodItemsByName(@PathVariable String name) {
        return foodService.findByName(name);
    }

    @GetMapping("/foodItem/minKcal")
    public FoodItemDTO getFoodItemWithMinKcal() {
        return foodService.findWithMinKcal().get();
    }

    @PostMapping("/foodItem")
    public ResponseEntity<FoodItemDTO> saveNewFoodItem(@Valid @RequestBody FoodItemDTO foodItemDTO) {
        Optional<FoodItemDTO> savedFoodItemDTO = foodService.saveOrUpdate(foodItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFoodItemDTO.get());
    }

    @PutMapping("/foodItem/{id}")
    public ResponseEntity<FoodItemDTO> updateFoodItem(@Valid @RequestBody FoodItemDTO foodItemDTO,
                                                      @PathVariable Integer id)
    {
        if(!foodItemDTO.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<FoodItemDTO> updatedFoodItemDtoOptional = foodService.saveOrUpdate(foodItemDTO);

        if(updatedFoodItemDtoOptional.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(updatedFoodItemDtoOptional.get());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        foodService.deleteFoodItem(id);
    }


}
