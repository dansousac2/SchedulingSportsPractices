package Site;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@TestMethodOrder(OrderAnnotation.class)
public class SportCRUDFrontTests {
	
	private static WebDriver driver;

	@BeforeAll
	static void setUp() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\REGIVALDO\\Desktop\\Keilla\\DAC\\workspace-spring-tool-suite-4-4.14.0.RELEASE\\SchedulingSportsPractices\\src\\test\\files\\keilla\\chromedriver.exe");
		
		driver = new ChromeDriver();
		
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}

	@AfterEach
	void beforeEach() throws InterruptedException {
		Thread.sleep(2000);
	}

	@AfterAll
	static void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		driver.quit();
	}

	@Test
	@DisplayName("Cadastrando um novo esporte - Caso Válido")
	@Order(1)
	void creatingValidSport() throws InterruptedException {
		driver.get("http://localhost:3000/createSport");
		
		WebElement nameSport = driver.findElement(By.id("lab01"));
		nameSport.sendKeys("Basquete");
		WebElement saveButton = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/button[1]");
		saveButton.click();
		
		
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();

		
		assertEquals("Sucesso", title);
		assertEquals("Esporte criado com sucesso!", message);
		
		assertEquals("http://localhost:3000/listSports", driver.getCurrentUrl().toString());
		
				
		
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3})
	@DisplayName("Cadastrando Esporte - Casos Inválidos")
	@Order(2)
	void createInvalidSport(int caseInvalid) throws InterruptedException {
		driver.get("http://localhost:3000/createSport");
		
		String errorMessage = null;
		
		switch (caseInvalid) {
			// Passando o nome de um esporte que já existe no banco de dados
			case 1:
				writerField("Basquete");
				
				errorMessage = "Já existe um esporte com nome Basquete";
				break;
				
			// Tentando cadastrar um esporte com o campo nome em branco
			case 2:
				writerField("");
				
				errorMessage = "É obrigatório informar o nome do esporte!";
				break;
				
				
			// Passando um nome com mais de três caracteres, mas com um caracter especial
			case 3:
				writerField("Bas@"); 
				errorMessage = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais";
				break;
		}
		
		Thread.sleep(2000);
		
		WebElement saveButton = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/button[1]");
		saveButton.click();
		
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();
		
		Thread.sleep(1000);
		
		assertEquals("Erro", title);
		assertEquals(errorMessage, message);
		
		assertEquals("http://localhost:3000/createSport", driver.getCurrentUrl().toString());
	}
	
	@Test
	@DisplayName("Atualizando um Esporte - Caso Válido")
	@Order(3)
	void updatingValidSport() throws InterruptedException {
		driver.get("http://localhost:3000/listSports");
		
				String nameSport = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr[1]/td[2]").getText();
				
				getElementByXPath("//button[@class='btn btn-warning']").click();
				
				WebElement nameSportWeb = getElementById("lab01");
				
				String sportId = getElementById("lab00").getAttribute("value");
				
				assertAll("Os campos da página de edição já devem estar preenchidos",
						() -> assertTrue(sportId.matches("^\\d+$")),
						() -> assertEquals(nameSportWeb.getAttribute("value"), nameSport),
						() -> assertTrue(driver.getCurrentUrl().contains("http://localhost:3000/updateSport/"))
				);
				
				Thread.sleep(1500);
				
				// Passando novos valores para os atributos
				String newNameSport = "Futebol";
				
				
				nameSportWeb.clear();
				nameSportWeb.sendKeys(newNameSport);
				
				
				Thread.sleep(1500);
				
				// clicando no botão Atualizar
				WebElement buttonUpdate = getElementByXPath("//button[@class='btn btn-primary']");
				
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("arguments[0].click()", buttonUpdate);
				
				
				assertAll("Fazendo a verificação do card de sucesso",
						() -> assertEquals("Sucesso", getElementByClass("toast-title").getText()),
						() -> assertEquals("Esporte atualizado com sucesso!", getElementByClass("toast-message").getText()),
						() -> assertEquals("http://localhost:3000/listSports", driver.getCurrentUrl())
				);
				
				Thread.sleep(1500);
				
				//Testando botão cancelar
				getElementByXPath("//button[@class='btn btn-warning']").click();
				
				Thread.sleep(1000);
				
				assertTrue(driver.getCurrentUrl().contains("http://localhost:3000/updateSport/"));
				
				WebElement buttonCancel = getElementByXPath("//button[@class='btn btn-danger']");
	
				jse.executeScript("arguments[0].click()", buttonCancel);
				
				Thread.sleep(1000);
				
				assertEquals("http://localhost:3000/listSports", driver.getCurrentUrl());
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1, 2})
	@DisplayName("Atualizando um Esporte - Casos Inválidos")
	@Order(4)
	void updatingInvalidSport(int caseInvalid) throws InterruptedException {
		driver.get("http://localhost:3000/listSports");
		
		Thread.sleep(500);
		
		// botão atualizar da página de listagem
		getElementByXPath("//button[@class='btn btn-warning']").click();
		
		String errorMessage = null;
		
		switch (caseInvalid) {
		case 1:
			
			// Tentando atualizar o nome de um esporte deixando o campo em branco
			writerFieldUpdate("");
			errorMessage = "É obrigatório informar o nome do esporte!";
			break;
		case 2:
			writerFieldUpdate("Bas#");
			errorMessage = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais";
			break;
		
		}
		
		Thread.sleep(1500);
		
		// Clicando no botão Atualizar
		WebElement buttonUpdate = getElementByXPath("//button[@class='btn btn-primary']");
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].click()", buttonUpdate);
		
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();
		
		Thread.sleep(2000);
		
		assertEquals("Erro", title);
		assertEquals(errorMessage, message);
		
		assertNotEquals("http://localhost:3000/listSports", driver.getCurrentUrl().toString());
		
		

		
	}
	
	@Test
	@DisplayName("Deletando esporte")
	@Order(5)
	void deletingSport() throws InterruptedException {

		driver.get("http://localhost:3000/listSports");
		
		WebElement idElementWeb = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr/td[3]");
		String id = idElementWeb.getText();
		
		WebElement buttonDelete = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr/td[3]/button[2]");
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].click()", buttonDelete);
		
		Thread.sleep(800);
		
		String tableBody = getElementByTagName("TBODY").getText();
		
		assertFalse(tableBody.contains(id));
	}
	
	
	private void FieldClear(WebElement element) {
		String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
		element.sendKeys(del);
	}
	
	private void writerFieldUpdate(String sportName) {
		WebElement element;

		element = getElementById("lab01");
		clearField(element);
		if(sportName != null) {
			element.sendKeys(sportName);
		}
		
	}

	// Método criado devido a um bug encontrado ao usar clear() do WebElement
	private void clearField(WebElement element) {
		String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
		element.sendKeys(del);
	}
	
		
	private void writerField(String sportName) throws InterruptedException {
		WebElement element;

		element = getElementById("lab01");
		element.sendKeys(sportName);

		Thread.sleep(1500);

		
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

