package Site;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

class PlaceCRUD_front {

	private static WebDriver driver;

	@BeforeAll
	static void setUp() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"D:\\workspace-spring-tool-suite-4-4.14.0.RELEASE\\SchedulingSportsPractices\\src\\test\\java\\files\\chromedriver.exe");
		
		driver = new ChromeDriver();
		
		// caso não encontre um elemento (em uma busca), espera n segundos (fazendo
		// novas buscas) antes de lançar erro.
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@BeforeEach
	void beforeEach() throws InterruptedException {
		Thread.sleep(1000);
	}

	@AfterAll
	static void tearDown() throws InterruptedException {
		Thread.sleep(2000);
		driver.quit();
	}

	@Test
	@DisplayName("criar local no banco - procedimento com sucesso")
	void createPlace() {
		driver.get("http://localhost:3000/createPlace");
		
		// Campos de preenchiemtno sendo setados com valores respectivos de:
		// Local / Referência / Capacidade máxima / é público?
		writerFields("Quadra", "Logo na entrada", "12", false);
		
		// botão salvar
		WebElement buttonSave = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/button[1]");
		buttonSave.click();

		// card de sucesso
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();

		assertAll("Testes do front ao criar place",
				/* aviso de sucesso */
				() -> assertTrue(cardTitle.equals("Sucesso")),
				() -> assertTrue(cardMsg.equals("Local criado com Sucesso!")),
				() -> assertTrue(driver.getCurrentUrl().toString().equals("http://localhost:3000/listPlaces"))
		);
	}

	private void writerFields(String placeName, String referency, String capacityM, boolean isPublic) {
		WebElement element;
		
		// caixa nome
		if(placeName != null) {
			element = getElementById("lab01");
			element.sendKeys(placeName);
		}
		
		// caixa referência
		if(referency != null) {
			element = getElementById("lab02");
			element.sendKeys(referency);
		}

		// caixa capacidade total
		if(capacityM != null) {
			element = getElementById("lab03");
			element.sendKeys(capacityM);
		}

		// caixa "é público?"
		if(isPublic) {
			element = getElementById("flexCheckDefault");
			element.click();
		}
	}

	private WebElement getElementById(String id) {
		return driver.findElement(By.id(id));
	}

	private WebElement getElementByClass(String className) {
		return driver.findElement(By.className(className));
	}

	private WebElement getElementByXPath(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}
}
