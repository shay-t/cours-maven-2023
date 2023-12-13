package fr.imt.shay.machine.cucumber.steps;

import fr.imt.shay.machine.machine.CoffeeMachine;
import fr.imt.shay.machine.machine.exception.CannotMakeCremaWithSimpleCoffeeMachine;
import fr.imt.shay.machine.machine.exception.CoffeeTypeCupDifferentOfCoffeeTypeTankException;
import fr.imt.shay.machine.machine.exception.LackOfWaterInTankException;
import fr.imt.shay.machine.machine.exception.MachineNotPluggedException;
import fr.imt.shay.storage.storage.cupboard.coffee.type.CoffeeType;
import fr.imt.shay.storage.storage.cupboard.container.*;
import fr.imt.shay.storage.storage.cupboard.exception.CupNotEmptyException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class CucumberStepsCoffeeMachineMakeACoffeeTest {

    public CoffeeMachine coffeeMachine;
    public Mug mug;
    public Cup cup;
    public CoffeeContainer containerWithCoffee;

    @Given("a coffee machine with {double} l of min capacity, {double} l of max capacity, {double} l per h of water flow for the pump")
    public void givenACoffeeMachine(double minimalWaterCapacity, double maximalWaterCapacity, double pumpWaterFlow){
        coffeeMachine = new CoffeeMachine(minimalWaterCapacity, maximalWaterCapacity, minimalWaterCapacity, maximalWaterCapacity, pumpWaterFlow);
    }
    @Given("a coffee machine with {double} l of min capacity, {double} l of max capacity, {double} l per h of water flow for the pump that is out of order")
    public void givenACoffeeMachineOutOfOrder(double minimalWaterCapacity, double maximalWaterCapacity, double pumpWaterFlow){
        coffeeMachine = new CoffeeMachine(minimalWaterCapacity, maximalWaterCapacity, minimalWaterCapacity, maximalWaterCapacity, pumpWaterFlow);
        coffeeMachine.setOutOfOrder(true);
    }
    @And("a {string} with a capacity of {double}")
    public void aWithACapacityOf(String containerType, double containerCapacity) {
        if ("mug".equals(containerType))
            mug = new Mug(containerCapacity);
        if ("cup".equals(containerType))
            cup = new Cup(containerCapacity);
    }
    @When("I plug the machine to electricity")
    public void iPlugTheMachineToElectricity() {
        coffeeMachine.plugToElectricalPlug();
    }
    @And("I add {double} liter of water in the water tank")
    public void iAddLitersOfWater(double waterVolume) {
        coffeeMachine.addWaterInTank(waterVolume);
    }
    @And("I add {double} liter of {string} in the bean tank")
    public void iAddLitersOfCoffeeBeans(double beanVolume, String coffeeType) {
        coffeeMachine.addCoffeeInBeanTank(beanVolume, CoffeeType.valueOf(coffeeType));
    }
    @And("I made a coffee {string}")
    public void iMadeACoffee(String coffeeType) throws
            InterruptedException, CupNotEmptyException, LackOfWaterInTankException,
            MachineNotPluggedException, CoffeeTypeCupDifferentOfCoffeeTypeTankException,
            CannotMakeCremaWithSimpleCoffeeMachine{

        if (mug != null)
            containerWithCoffee = coffeeMachine.makeACoffee(mug, CoffeeType.valueOf(coffeeType));
        if (cup != null)
            containerWithCoffee = coffeeMachine.makeACoffee(cup, CoffeeType.valueOf(coffeeType));

    }
    @Then("the coffee machine return a coffee mug not empty")
    public void theCoffeeMachineReturnACoffeeMugNotEmpty() {
        Assertions.assertFalse(containerWithCoffee.isEmpty());
    }
    @And("a coffee volume equals to {double}")
    public void aCoffeeVolumeEqualsTo(double coffeeVolume) {
        assertThat(coffeeVolume, is(containerWithCoffee.getCapacity()));
    }
    @And("a coffee {string} containing a coffee type {string}")
    public void aCoffeeMugContainingACoffeeType(String containerType, String coffeeType) {
        if ("mug".equals(containerType))
            assertThat(containerWithCoffee, instanceOf(CoffeeMug.class));
        if ("cup".equals(containerType))
            assertThat(containerWithCoffee, instanceOf(CoffeeCup.class));

        assertThat(containerWithCoffee.getCoffeeType(), is(CoffeeType.valueOf(coffeeType)));
    }
    @Then("the coffee machine is plugged")
    public void theCoffeMachineIsPlugged(){
        Assertions.assertTrue(coffeeMachine.isPlugged());
    }

    @And("the coffee machine water tank contains {double} l of water")
    public void theCoffeMachineContainsWater(double waterAdded){
        Assertions.assertEquals(waterAdded, coffeeMachine.getWaterTank().getActualVolume());
    }

    @Then("nothing is returned")
    public void machineOutOfOrder() throws InterruptedException, CupNotEmptyException, LackOfWaterInTankException, MachineNotPluggedException, CoffeeTypeCupDifferentOfCoffeeTypeTankException, CannotMakeCremaWithSimpleCoffeeMachine{
        containerWithCoffee = coffeeMachine.makeACoffee(cup, CoffeeType.ARABICA_CREMA);
        Assertions.assertNull(containerWithCoffee);
    }







}
