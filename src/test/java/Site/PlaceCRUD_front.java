package Site;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@TestMethodOrder(OrderAnnotation.class)
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

	@AfterEach
	void beforeEach() throws InterruptedException {
		Thread.sleep(1000);
	}

	@AfterAll
	static void tearDown() throws InterruptedException {
		Thread.sleep(2000);
		driver.quit();
	}

	@Test
	@DisplayName("criar local no banco - sucesso")
	@Order(1)
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

		// pega todos os elementos da tabela
		String tableInfo = getElementByTagName("TBODY").getText();
		
		assertAll("Testes do front ao criar place",
				/*aviso de sucesso*/
				() -> assertEquals("Sucesso", cardTitle),
				() -> assertEquals("Local criado com Sucesso!", cardMsg),
				
				/*se o redirecionamento foi feito à página informada*/
				() -> assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl().toString()),
				
				/*elementos retornados na tabela*/
				() -> assertTrue(tableInfo.contains("Quadra")),
				() -> assertTrue(tableInfo.contains("Logo na entrada")),
				() -> assertTrue(tableInfo.contains("12")),
				() -> assertTrue(tableInfo.contains("Não"))
		);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "2", "3", "4", "5", "6", "7"})
	@DisplayName("criar local no banco - falha por campos em branco e regras de negócio")
	@Order(2)
	void createPlaceFail(String s) throws InterruptedException {
		driver.get("http://localhost:3000/createPlace");
		
		final String messageErro;
		
		switch (s) {
		case "1":
			writerFields(null, "Logo na entrada", "12", false);
			messageErro = "É obrigatório informar o nome do local!";
			break;
		case "2":
			writerFields("Quadra", null, "12", false);
			messageErro = "É obrigatório informar um local de referência!";
			break;
		case "3":
			writerFields("Quadra", "Logo na entrada", null, true);
			messageErro = "É obrigatório informar a capacidade máxima do local!";
			break;
		case "4":
			writerFields("Blo", "Logo na entrada", "12", true); // "Blo" - 3 caracteres
			messageErro = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais";
			break;
		case "5":
			writerFields("Quadra", "Logo na entrada", "-1", false); // -1 para capacidade
			messageErro = "A capacidade de participantes deve ser um valor positivo!";
			break;
		case "6":
			writerFields("Quadra", "Logo na entrada", "401", false); // 401 excede a capacidade máxima
			messageErro = "O valor máximo para capacidade de participantes é 400!";
			break;
		case "7":
			writerFields("Quadra", "Logo na entrada", "12", false); // local já está cadastrado no bancod de dados
			messageErro = "Já existe um local com nome Quadra";
			break;
		default:
			messageErro = "";
		}
		
		Thread.sleep(1500);
		
		// botão salvar
		WebElement buttonSave = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/button[1]");
		buttonSave.click();

		Thread.sleep(500);
		
		// card de sucesso
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();

		assertAll("Testes do front ao criar place - campos nulos",
				/*aviso de falha*/
				() -> assertEquals("Erro", cardTitle),
				() -> assertEquals(messageErro, cardMsg),
				
				/*a página ainda deve ser a mesma depois do erro*/
				() -> assertEquals("http://localhost:3000/createPlace", driver.getCurrentUrl().toString())
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
	
	private WebElement getElementByTagName(String tag) {
		return driver.findElement(By.tagName(tag));
	}
}
