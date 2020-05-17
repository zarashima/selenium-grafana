import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoginTest {
	private WebDriver driver;

	@BeforeTest
	public void setUp(ITestContext context) {
		WebDriverManager.firefoxdriver().setup();
		driver = new FirefoxDriver();
		context.setAttribute("driver", driver);
	}

	@Test
	public void loginTest() {
		driver.get("https://another-nodejs-shopping-cart.herokuapp.com/user/signin");
		driver.findElement(By.id("email")).sendKeys("admin");
		driver.findElement(By.id("password")).sendKeys("password");
		driver.findElement(By.xpath("//button[text()='Sign In']")).click();
		Assert.assertEquals(driver.findElement(By.xpath("/html/body/div/div/div/div")).getText(), "Successful");
	}

	@Test
	public void loginTestValidInfo() {
		driver.get("https://another-nodejs-shopping-cart.herokuapp.com/user/signin");
		driver.findElement(By.id("email")).sendKeys("admin");
		driver.findElement(By.id("password")).sendKeys("password");
		driver.findElement(By.xpath("//button[text()='Sign In']")).click();
		Assert.assertEquals(driver.findElement(By.xpath("/html/body/div/div/div/div")).getText(), "Email inválido!");
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}
}
