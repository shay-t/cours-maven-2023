package fr.imt.shay.storage.storage.cupboard.container;


import fr.imt.shay.storage.storage.cupboard.coffee.type.CoffeeType;

public class CoffeeCup extends CoffeeContainer{
    public CoffeeCup(double capacity, CoffeeType coffeeType) {
        super(capacity, coffeeType);
    }

    public CoffeeCup(Container container, CoffeeType coffeeType) {
        super(container, coffeeType);
    }
}
