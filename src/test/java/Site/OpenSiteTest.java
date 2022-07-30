package Site;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

class OpenSiteTest {
	
	private static WebDriver driver;

	@BeforeAll
	static void setUp() {
		System.setProperty("webdriver.chrome.driver", "D:\\workspace-spring-tool-suite-4-4.14.0.RELEASE\\SchedulingSportsPractices\\src\\test\\java\\files\\chromedriver.exe");
		driver = new ChromeDriver();
		// caso não encontre um elemento (em uma busca), espera 10s (fazendo novas buscas) antes de lançar erro.
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); 
	}
	
	@AfterAll
	static void tearDown() {
		driver.quit();
	}

	@Test
	@DisplayName("axcess to homepage")
	void home() throws InterruptedException {
		driver.get("http://localhost:3000");
		Thread.sleep(2000);
	}
	
	@Test
	@DisplayName("verify title of tab")
	void title() {
		String title = driver.getTitle();
		assertAll("Testes da página de login",
				() -> assertTrue(title.contentEquals("SAPE")),
				() -> assertTrue(title.length() == 4)
		);
	}

	
}
