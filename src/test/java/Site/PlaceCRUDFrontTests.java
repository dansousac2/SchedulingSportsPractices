package Site;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@TestMethodOrder(OrderAnnotation.class)
class PlaceCRUDFrontTests {

	private static WebDriver driver;

	@BeforeAll
	static void setUp() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"D:\\workspace-spring-tool-suite-4-4.14.0.RELEASE\\SchedulingSportsPractices\\src\\test\\java\\files\\chromedriver.exe");
		
		driver = new ChromeDriver();
		
		// caso não encontre um elemento (em uma busca), espera n segundos (fazendo
		// novas buscas) antes de lançar erro. OBS: o getCurrentURL não se enquadra.
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}

	@AfterEach
	void beforeEach() throws InterruptedException {
		Thread.sleep(1000);
	}

	@AfterAll
	static void tearDown() throws InterruptedException {
		Thread.sleep(1000);
		driver.quit();
	}

	@Test
	@DisplayName("criar local no banco - CASO POSITIVO")
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
		String tBody = getElementByTagName("TBODY").getText();
		
		// criando pattern com id do place em questão para pegar linha da tabela contendo as informações deste place
		Pattern p = Pattern.compile("\\n?\\d+ Quadra.*");
		Matcher m = p.matcher(tBody);
		final String lineOnTable;
		if(m.find()) {
			// gerando linhas específica da tabela qeu evidencia os valores deste exato place editado.
			lineOnTable = m.group(0);
		} else {
			lineOnTable = "";
		}
		
		assertAll("Testes do front ao criar place",
				/*aviso de sucesso*/
				() -> assertEquals("Sucesso", cardTitle),
				() -> assertEquals("Local criado com Sucesso!", cardMsg),
				
				/*se o redirecionamento foi feito à página informada*/
				() -> assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl().toString()),
				
				/*elementos retornados na tabela*/
				() -> assertTrue(lineOnTable.contains("Quadra")),
				() -> assertTrue(lineOnTable.contains("Logo na entrada")),
				() -> assertTrue(lineOnTable.contains("12")),
				() -> assertTrue(lineOnTable.contains("Não"))
		);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "2", "3", "4", "5", "6", "7"})
	@DisplayName("criar local no banco - CASO NEGATIVO por campos em branco e regras de negócio")
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

		assertAll("Testes do front ao criar place - mesangem de campos",
				/*aviso de falha*/
				() -> assertEquals("Erro", cardTitle),
				() -> assertEquals(messageErro, cardMsg),
				
				/*a página ainda deve ser a mesma depois do erro*/
				() -> assertEquals("http://localhost:3000/createPlace", driver.getCurrentUrl())
		);
	}

	@Test
	@DisplayName("Atualizar local - CASO POSITIVO - verificando se dados chegaram nos campos corretamente e botão cancelar")
	@Order(3)
	void updatePlaceValid() throws InterruptedException {
		driver.get("http://localhost:3000/listPlaces");
		
		
		// elementos da tabela (primeira linha)
		String name = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr[1]/td[2]").getText();
		String reference = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr[1]/td[3]").getText();
		String capacity = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr[1]/td[4]").getText();
		String isPublicString = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr[1]/td[5]").getText();
		boolean isPublic = isPublicString.equals("Sim");
		
		// outro modo de usar o xPath - pega o primeiro elemento com a classe especificada - botão atualizar
		getElementByXPath("//button[@class='btn btn-warning']").click();
		
		WebElement nameWE = getElementById("lab01");
		WebElement referenceWE = getElementById("lab02");
		WebElement capacityWE = getElementById("lab03");
		WebElement isPublicWE = getElementById("flexCheckDefault");
		
		String placeId = getElementById("lab00").getAttribute("value");
		
		assertAll("Elemento devem estar nos campus da página de edição",
				/*no campo ID deve conter apenas números*/
				() -> assertTrue(placeId.matches("^\\d+$")),
				/*conferindo campos: nome, referência, capacidade, e se é público respectivamente*/
				() -> assertEquals(nameWE.getAttribute("value"), name),
				() -> assertEquals(referenceWE.getAttribute("value"), reference),
				() -> assertEquals(capacityWE.getAttribute("value"), capacity),
				/*a página deve ser a de edição*/
				() -> assertTrue(driver.getCurrentUrl().contains("http://localhost:3000/updatePlace/"))
		);
		
		Thread.sleep(1500);
		
		// novos valores dos atributos
		String newName = "Sala de Reuniões";
		String newReference = "Próximo ao refeitório";
		String newCapacity = "8";
		boolean newIsPublic = !isPublic;
		
		nameWE.clear();
		nameWE.sendKeys(newName);
		referenceWE.clear();
		referenceWE.sendKeys(newReference);
		capacityWE.clear();
		capacityWE.sendKeys(newCapacity);
		// inverte o valor bolleano atual de isPublic
		if(newIsPublic) {
			isPublicWE.click();
		}
		
		Thread.sleep(1500);
		
		// clicando no botão de Atualizar
		WebElement buttonUpdate = getElementByXPath("//button[@class='btn btn-primary']");
		
		// resolve problema do botão sem poder ser alcançado no click
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].click()", buttonUpdate);

		// pegando todas as tuplas da tabela
		String tBody = getElementByTagName("TBODY").getText();
		
		// criando pattern com id do place em questão para pegar linha da tabela contendo as informações deste place
		Pattern p = Pattern.compile(String.format("\\n?%s.*", placeId));
		Matcher m = p.matcher(tBody);
		final String lineOnTable;
		if(m.find()) {
			// gerando linhas específica da tabela qeu evidencia os valores deste exato place editado.
			lineOnTable = m.group(0);
		} else {
			lineOnTable = "";
		}
		
		assertAll("Verificando valores após edição de todos, assim como verifica card",
				() -> assertEquals("Sucesso", getElementByClass("toast-title").getText()),
				() -> assertEquals("Local atualizado com sucesso!", getElementByClass("toast-message").getText()),
				() -> assertTrue(lineOnTable.contains(newName)),
				() -> assertTrue(lineOnTable.contains(newReference)),
				() -> assertTrue(lineOnTable.contains(newCapacity)),
				() -> assertTrue(lineOnTable.contains((newIsPublic) ? "Sim" : "Não")),
				() -> assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl())
		);
		
		Thread.sleep(1500);
		
		/*clicando no botão cancelar ao carregar a página de update*/
		// clicando no botão de Atualizar já na página de listagem
		getElementByXPath("//button[@class='btn btn-warning']").click();
		
		Thread.sleep(1000);
		
		// a página atual deve ser a de edição
		assertTrue(driver.getCurrentUrl().contains("http://localhost:3000/updatePlace/"));
		
		WebElement buttonCancel = getElementByXPath("//button[@class='btn btn-danger']");
		// resolve problema do botão sem poder ser alcançado no click
		jse.executeScript("arguments[0].click()", buttonCancel);
		
		Thread.sleep(1000);
		
		// a página deve ser a de listagem dos locais
		assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "2", "3", "4", "5", "6"})
	@DisplayName("atualizar local - CASO NEGATIVO por campos em branco e regras de negócio")
	@Order(4)
	void updatePlaceInvalid(String s) throws InterruptedException {
		driver.get("http://localhost:3000/listPlaces");
		
		Thread.sleep(500);
		
		// botão atualizar da página de listagem
		getElementByXPath("//button[@class='btn btn-warning']").click();
		
		final String messageErro;
		
		switch (s) {
		case "1":
			writerFieldsUpdate(null, "Segundo Andar", "2", true);
			messageErro = "É obrigatório informar o nome do local!";
			break;
		case "2":
			writerFieldsUpdate("Biblioteca mesa01", null, "2", false);
			messageErro = "É obrigatório informar um local de referência!";
			break;
		case "3":
			writerFieldsUpdate("Biblioteca mesa01", "Segundo Andar", null, true);
			messageErro = "É obrigatório informar a capacidade máxima do local!";
			break;
		case "4":
			writerFieldsUpdate("Bib", "Segundo Andar", "2", true); // "Bib" - 3 caracteres
			messageErro = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais";
			break;
		case "5":
			writerFieldsUpdate("Biblioteca mesa01", "Segundo Andar", "-1", true); // -1 para capacidade
			messageErro = "A capacidade de participantes deve ser um valor positivo!";
			break;
		case "6":
			writerFieldsUpdate("Biblioteca mesa01", "Segundo Andar", "401", true); // 401 excede a capacidade máxima
			messageErro = "O valor máximo para capacidade de participantes é 400!";
			break;
		default:
			messageErro = "";
		}
		
		Thread.sleep(1500);
		
		// selecionando botão de Atualizar
		WebElement buttonUpdate = getElementByXPath("//button[@class='btn btn-primary']");
		// resolve problema do botão sem poder ser alcançado para click
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].click()", buttonUpdate);
		
		Thread.sleep(500);
		
		// card de sucesso
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();

		assertAll("Testes do front ao atualizar place - mensagem de campos",
				/*aviso de falha*/
				() -> assertEquals("Erro", cardTitle),
				() -> assertEquals(messageErro, cardMsg),
				
				/*a página ainda deve ser a mesma depois do erro*/
				() -> assertTrue(driver.getCurrentUrl().contains("http://localhost:3000/updatePlace"))
		);
	}
	
	@Test
	@DisplayName("Deletando um local partindo da página de listagem")
	@Order(5)
	void deletePlace() throws InterruptedException {
		driver.get("http://localhost:3000/listPlaces");
		
		// id do primeiro local da lista
		String id = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr/td[1]").getText();
		
		// precionar o primeiro botão "excluir" da tabela
		getElementByXPath("//button[@class='btn btn-danger']").click();
		
		// necessário para que a tabela tenha tempo de ser atualizada
		Thread.sleep(500);
		
		String tBody = getElementByTagName("TBODY").getText();
		System.out.println(tBody);
		assertAll("Exclusão de local",
				() -> assertFalse(tBody.contains(id)),
				() -> assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl())
		);
	}
	
	private void writerFieldsUpdate(String placeName, String reference, String capacityM, boolean isPublic) {
		WebElement element;

		element = getElementById("lab01");
		clearField(element);
		if(placeName != null) {
			element.sendKeys(placeName);
		}
		
		element = getElementById("lab02");
		clearField(element);
		if(reference != null) {
			element.sendKeys(reference);
		}
		
		element = getElementById("lab03");
		clearField(element);
		if(capacityM != null) {
			element.sendKeys(capacityM);
		}
		
		if(isPublic) {
			getElementById("flexCheckDefault").click();
		}
	}

	// Método criado devido a um bug encontrado ao usar clear() do WebElement
	private void clearField(WebElement element) {
		String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
		element.sendKeys(del);
	}

	private void writerFields(String placeName, String reference, String capacityM, boolean isPublic) {
		WebElement element;
		
		// caixa nome
		if(placeName != null) {
			element = getElementById("lab01");
			element.sendKeys(placeName);
		}
		
		// caixa referência
		if(reference != null) {
			element = getElementById("lab02");
			element.sendKeys(reference);
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
