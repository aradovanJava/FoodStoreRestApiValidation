package hr.java.spring.foodstore.foodstore.validator;

import hr.java.spring.foodstore.foodstore.dto.FoodItemDTO;
import hr.java.spring.foodstore.foodstore.model.FoodItem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<PriceConstraint, FoodItemDTO> {

    @Override
    public boolean isValid(FoodItemDTO foodItemDTO, ConstraintValidatorContext constraintValidatorContext) {
        if(foodItemDTO.getSellingPrice().compareTo(foodItemDTO.getInitialPrice()) <= 0) {
            return false;
        }
        return true;
    }
}
