import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v85.systeminfo.model.Size;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyStoreSteps {
    private WebDriver driver;

    @FindBy(id = "quantity_wanted")
    private WebElement Amount;  // Pole ilości

    @FindBy(id = "group_1")
    private WebElement size;  // Dropdown z rozmiarami

    @FindBy(className = "add-to-cart")
    private WebElement AddBtn;  // Przycisk 'Add to Cart'

    @Given("The user is logged in with username {string} and password {string}")
    public void theUserIsLoggedInWithLoginAndPassword(String login, String passwd) {
        driver = new ChromeDriver();  //inicjalizujemy driver
        driver.manage().window().maximize(); //maksymalizujemy okienko przeglądarki
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); //ustawiamy czas załadowania formularza
        driver.get("https://mystore-testlab.coderslab.pl/"); //przechodzimy na stronę sklepu
        WebElement signInButton = driver.findElement(By.className("user-info"));
        signInButton.click();
        String email = "rfeistppnujilwycig@nbmbb.com";
        String password = "Hasło1234#@!";
        driver.findElement(By.className("form-control")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.id("submit-login")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement userInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("user-info")));
        assertTrue(userInfo.isDisplayed()); //Sprawdzamy, czy element user-info jest widoczny na stronie, co potwierdza, że użytkownik jest zalogowany.

    }


    @When("The user adds {string} to the cart with size {string} and quantity {string}")
    public void theUserAddsToTheCartWithSizeAndQuantity(String sweater, String size, String quantity) {
        // Wyszukiwanie produktu
        driver.findElement(By.className("ui-autocomplete-input")).sendKeys(sweater); // Wpisuje nazwę swetra w pole wyszukiwania
        driver.findElement(By.className("ui-autocomplete-input")).sendKeys(Keys.RETURN);
        new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.findElement(By.className("product-description")).click();

        // Czekamy, aż pojawią się wyniki wyszukiwania
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement productThumbnail = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".thumbnail.product-thumbnail")));

        driver.findElement(By.className("material-icons search")).click();


        // // // // // // // // // // // // // // // // // // // // // //
        WebElement sizeDropdown = driver.findElement(By.id("group_1"));
        sizeDropdown.click();

        // Znajdujemy opcję dla rozmiaru M i klikamy ją
        WebElement sizeMOption = driver.findElement(By.xpath("//option[@value='2']"));
        sizeMOption.click();


        driver.findElement(By.id("group_1"));
        sizeDropdown.click();

        // Znajduje opcję dla rozmiaru M i klika ją
        driver.findElement(By.xpath("//option[@value='2']"));
        sizeMOption.click();


        // Czekamy, aż elementy staną się dostępne
        wait.until(ExpectedConditions.elementToBeClickable(Amount)); // Czekamy na pole ilości
        Amount.sendKeys(Keys.CONTROL + "a"); // Zaznaczamy ilość
        Amount.sendKeys("5"); // Ustawiamy ilość


        // Czekamy na przycisk "Add to Cart"
        wait.until(ExpectedConditions.elementToBeClickable(AddBtn));
        AddBtn.click(); // Klikamy przycisk dodania do koszyka


        // Czekamy na pojawienie się okna modalnego z koszykiem
        new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("btn btn-primary")));

    }

    @And("The user proceeds to the checkout page")
    public void theUserProceedsToTheCheckoutPage() {
        driver.findElement(By.className("btn btn-primary")).click(); // Przycisk 'Proceed to checkout'
    }

    @And("The user confirms the address")
    public void theUserConfirmsTheAddress() {
        driver.findElement(By.name("confirm-addresses")).click(); //Klikamy przycisk do potwierdzenia adresu dostawy

    }

    @And("The user selects {string} as the delivery method")
    public void theUserSelectsAsTheDeliveryMethod(String pick_up_in_store) {
        driver.findElement(By.className("row delivery-option js-delivery-option")).click();
        driver.findElement(By.className("continue btn btn-primary float-xs-right")).click();

    }

    @And("The user chooses {string} as the payment method")
    public void theUserChoosesAsThePaymentMethod(String pay_by_check) {
        driver.findElement(By.id("id=\"payment-option-1-container\"")).click(); //Naciśnięcie Pay by Check
        driver.findElement(By.className("condition-label")).click(); //Naciśnięcie checboxa "I agree.."
        driver.findElement(By.id("payment-confirmation")).click(); // Naćiśnięcie przycisku "Placeorder"


    }

    @Then("The user should see an order confirmation with the total amount")
    public void theUserShouldSeeAnOrderConfirmationWithTheTotalAmount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement totalAmountElement = driver.findElement(By.className("total-value font-weight-bold"));
    }

    @And("The user will take a screenshot of the order confirmation")
    public void theUserWillTakeAScreenshotOfTheOrderConfirmation() {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File("order_confirmation.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}