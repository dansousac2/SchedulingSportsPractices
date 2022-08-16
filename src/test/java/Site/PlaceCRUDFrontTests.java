package Site;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import br.edu.ifpb.dac.ssp.presentation.dto.PlaceDTO;

@TestMethodOrder(OrderAnnotation.class)
class PlaceCRUDFrontTests {

	private static WebDriver driver;
	private static JavascriptExecutor jse;
	private static PlaceDTO place;

	@BeforeAll
	static void setUp() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\REGIVALDO\\Desktop\\Keilla\\DAC\\workspace-spring-tool-suite-4-4.14.0.RELEASE\\SchedulingSportsPractices\\src\\test\\files\\keilla\\chromedriver.exe");
		
		driver = new ChromeDriver();
		jse = (JavascriptExecutor)driver;
		
		place = new PlaceDTO();
		place.setName("Quadra");
		place.setReference("Logo na entrada");
		place.setMaximumCapacityParticipants(100);
		place.setPublic(false);
		
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
	void createPlace() throws InterruptedException {
		driver.get("http://localhost:3000/createPlace");
		
		String placeName = place.getName();
		String placeReference = place.getReference();
		String placeMaxCapacity = String.valueOf(place.getMaximumCapacityParticipants());
		boolean placeIsPublic = place.isPublic();
		
		// Campos de preenchiemtno sendo setados com valores respectivos de:
		writerFields(placeName, placeReference, placeMaxCapacity, placeIsPublic);
		
		// botão salvar
		WebElement buttonSave = getElementByXPath("//button[@class='btn btn-primary']");
		clickElement(buttonSave);

		// card de sucesso
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();

		// pega todos os elementos da tabela
		String tBody = getElementByTagName("TBODY").getText();
		
		// captura a linha específica que representa o objeto criado
		String lineOnTable = getSpecificLine(tBody, placeName);
		
		assertAll("Testes do front ao criar place",
				/*aviso de sucesso*/
				() -> assertEquals("Sucesso", cardTitle),
				() -> assertEquals("Local criado com Sucesso!", cardMsg),
				
				/*se o redirecionamento foi feito à página informada*/
				() -> assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl().toString()),
				
				/*elementos retornados na tabela*/
				() -> assertTrue(lineOnTable.contains(placeName)),
				() -> assertTrue(lineOnTable.contains(placeReference)),
				() -> assertTrue(lineOnTable.contains(placeMaxCapacity)),
				() -> assertTrue(lineOnTable.contains((placeIsPublic) ? "Sim" : "Não"))
		);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "2", "3", "4", "5", "6", "7"})
	@DisplayName("criar local no banco - CASO NEGATIVO por campos em branco e regras de negócio")
	@Order(2)
	void createPlaceFail(String s) throws InterruptedException {
		driver.get("http://localhost:3000/createPlace");
		
		String placeName = place.getName();
		String placeReference = place.getReference();
		String placeMaxCapacity = String.valueOf(place.getMaximumCapacityParticipants());
		boolean placeIsPublic = place.isPublic();
		
		final String messageErro;
		
		switch (s) {
		case "1":
			writerFields(null, placeReference, placeMaxCapacity, placeIsPublic);
			messageErro = "É obrigatório informar o nome do local!";
			break;
		case "2":
			writerFields(placeName, null, placeMaxCapacity, placeIsPublic);
			messageErro = "É obrigatório informar um local de referência!";
			break;
		case "3":
			writerFields(placeName, placeReference, null, placeIsPublic);
			messageErro = "É obrigatório informar a capacidade máxima do local!";
			break;
		case "4":
			// "Blo" - 3 caracteres
			writerFields("Blo", placeReference, placeMaxCapacity, placeIsPublic);
			messageErro = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais";
			break;
		case "5":
			// capacidade não positiva
			writerFields(placeName, placeReference, "0", placeIsPublic);
			messageErro = "A capacidade de participantes deve ser um valor positivo!";
			break;
		case "6":
			// 401 excede a capacidade máxima
			writerFields(placeName, placeReference, "401", placeIsPublic); 
			messageErro = "O valor máximo para capacidade de participantes é 400!";
			break;
		case "7":
			// local já está cadastrado no bancod de dados
			writerFields(placeName, placeReference, placeMaxCapacity, placeIsPublic);
			messageErro = "Já existe um local com nome " + placeName ;
			break;
		default:
			messageErro = "";
		}
		
		Thread.sleep(1500);
		
		// botão salvar
		WebElement buttonSave = getElementByXPath("//button[@class='btn btn-primary']");
		clickElement(buttonSave);

		Thread.sleep(500);
		
		// card de sucesso
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();

		assertAll("Testes do front ao criar place - mesangem de campos",
				/*aviso de falha para cada card*/
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

		// pegando informações da primeira linha da tabela à mostra para usuário
		String PlaceName = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr[1]/td[2]").getText();
		String placeReference = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr[1]/td[3]").getText();
		String PlaceCapacity = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr[1]/td[4]").getText();
		String placeIsPublicString = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr[1]/td[5]").getText();
		boolean placeIsPublic = placeIsPublicString.equals("Sim");
		
		// botão atualizar do primeiro elemento
		WebElement updateButton = getElementByXPath("//button[@class='btn btn-warning']");
		clickElement(updateButton);
		
		WebElement nameWE = getElementById("lab01");
		WebElement referenceWE = getElementById("lab02");
		WebElement capacityWE = getElementById("lab03");
		WebElement isPublicWE = getElementByClass("form-check-input");
		
		// campo já possui valor. Pegando valor que não é editável
		String placeId = getElementById("lab00").getAttribute("value");
		
		assertAll("Elemento devem estar nos campus da página de edição",
				/*no campo ID deve conter apenas números*/
				() -> assertTrue(placeId.matches("^\\d+$")),
				/*conferindo campos: nome, referência e capacidade. Por padrão a caixa de 'isPublic' não fica marcada*/
				() -> assertEquals(nameWE.getAttribute("value"), PlaceName),
				() -> assertEquals(referenceWE.getAttribute("value"), placeReference),
				() -> assertEquals(capacityWE.getAttribute("value"), PlaceCapacity),
				/*a página deve ser a de edição. A url contém ao final a id do place que está sendo editado*/
				() -> assertTrue(driver.getCurrentUrl().contains("http://localhost:3000/updatePlace/"))
		);
		
		Thread.sleep(1500);
		
		// novos valores dos atributos. Modificamos tudo mesno o ID, pois é inacessível
		String newName = "Sala de Reuniões";
		String newReference = "Próximo ao refeitório";
		String newCapacity = "8";
		boolean newIsPublic = !placeIsPublic;
		
		nameWE.clear();
		nameWE.sendKeys(newName);
		referenceWE.clear();
		referenceWE.sendKeys(newReference);
		capacityWE.clear();
		capacityWE.sendKeys(newCapacity);
		// inverte o valor bolleano atual de isPublic
		if(newIsPublic) {
			clickElement(isPublicWE);
		}
		
		Thread.sleep(1500);
		
		// clicando no botão de Atualizar
		WebElement buttonUpdate = getElementByXPath("//button[@class='btn btn-primary']");
		clickElement(buttonUpdate);

		// pegando todas as tuplas da tabela
		String tBody = getElementByTagName("TBODY").getText();
		
		// pegando linha específica do objeto que foi atualizado
		String lineOnTable = getSpecificLine(tBody, placeId);
		
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
		
		// clicando no botão de Atualizar da página de listagem
		WebElement updateButton02 = getElementByXPath("//button[@class='btn btn-warning']");
		clickElement(updateButton02);
		
		Thread.sleep(500);
		
		// a página atual deve ser a de edição. No final da Url há o Id do place.
		assertTrue(driver.getCurrentUrl().contains("http://localhost:3000/updatePlace/"));
		
		WebElement buttonCancel = getElementByXPath("//button[@class='btn btn-danger']");
		clickElement(buttonCancel);
		
		Thread.sleep(500);
		
		// a página deve ser a de listagem dos locais
		assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "2", "3", "4", "5", "6"})
	@DisplayName("atualizar local - CASO NEGATIVO por campos em branco e regras de negócio")
	@Order(4)
	void updatePlaceInvalid(String s) throws InterruptedException {
		String updatedName = "Biblioteca mesa01";
		String updatedReference = "Segundo Andar";
		String updatedMaxCapacity = "2";
		
		driver.get("http://localhost:3000/listPlaces");
		
		Thread.sleep(500);
		
		// botão atualizar da página de listagem - primeiro da lista
		WebElement updateButton = getElementByXPath("//button[@class='btn btn-warning']");
		clickElement(updateButton);
		
		final String erroMessage;
		
		switch (s) {
		case "1":
			writerFieldsUpdate(null, updatedReference, updatedMaxCapacity, true);
			erroMessage = "É obrigatório informar o nome do local!";
			break;
		case "2":
			writerFieldsUpdate(updatedName, null, updatedMaxCapacity, false);
			erroMessage = "É obrigatório informar um local de referência!";
			break;
		case "3":
			writerFieldsUpdate(updatedName, updatedReference, null, true);
			erroMessage = "É obrigatório informar a capacidade máxima do local!";
			break;
		case "4":
			writerFieldsUpdate("Bib", updatedReference, updatedMaxCapacity, true); // "Bib" - 3 caracteres
			erroMessage = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais";
			break;
		case "5":
			writerFieldsUpdate(updatedName, updatedReference, "-1", true); // -1 para capacidade
			erroMessage = "A capacidade de participantes deve ser um valor positivo!";
			break;
		case "6":
			writerFieldsUpdate(updatedName, updatedReference, "401", true); // 401 excede a capacidade máxima
			erroMessage = "O valor máximo para capacidade de participantes é 400!";
			break;
		default:
			erroMessage = "";
		}
		
		Thread.sleep(1500);
		
		// botão de Atualizar da página de atualizar
		WebElement updateButton02 = getElementByXPath("//button[@class='btn btn-primary']");
		clickElement(updateButton02);
		
		Thread.sleep(500);
		
		// card de erro
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();

		assertAll("Testes do front ao atualizar place - mensagem de campos",
				/*aviso de falha*/
				() -> assertEquals("Erro", cardTitle),
				() -> assertEquals(erroMessage, cardMsg),
				
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
		WebElement buttonExclude = getElementByXPath("//button[@class='btn btn-danger']");
		clickElement(buttonExclude);
		
		// necessário para que a tabela tenha tempo de ser atualizada
		Thread.sleep(500);
		
		String tBody = getElementByTagName("TBODY").getText();
		String lineWithId = getSpecificLine(tBody, id);
		
		assertAll("Exclusão de local",
				() -> assertTrue(lineWithId.isEmpty()),
				() -> assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl())
		);
	}
	
	private String getSpecificLine(String tBody, String idOrName) {
		Pattern pattern;
		Matcher matcher;
		String lineOnTable = "";
		
		// se conter apenas dígitos
		if(idOrName.matches("^\\d+$")) {
			pattern = Pattern.compile(String.format("\\n?%s.*", idOrName));
		} else {
			pattern = Pattern.compile(String.format("\\n?\\d+ %s.*", idOrName));
		}
		
		matcher = pattern.matcher(tBody);

		if(matcher.find()) { // método find é NECESSÁRIO para iniciar a busca da parte especificada pelo regex.
			lineOnTable = matcher.group(0); // captura toda a expressão
		}
		
		return lineOnTable;
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
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			WebElement we = getElementById("flexCheckDefault");
			try {
				we.click();
			} catch (Exception e) {
				jse.executeScript("arguments[0].click()", we);
			}
		}
	}
	
	private void clickElement(WebElement we) {
		try {
			we.click();
		} catch (Exception e) {
			jse.executeScript("arguments[0].click()", we);
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
			clickElement(element);
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
