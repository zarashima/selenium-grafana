import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoginTest {
	private WebDriver driver;

	@BeforeTest
	public void setUp(ITestContext context) {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		context.setAttribute("driver", driver);
	}

	@Test
	public void loginTest() {
		driver.get("https://another-nodejs-shopping-cart.herokuapp.com/user/signin");
		driver.findElement(By.id("email")).sendKeys("admin");
		driver.findElement(By.id("password")).sendKeys("password");
		driver.findElement(By.xpath("//button[text()='Sign In']")).click();
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}
}
