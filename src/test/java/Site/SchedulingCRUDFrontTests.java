package Site;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

@TestMethodOrder(OrderAnnotation.class)
public class SchedulingCRUDFrontTests {

	private static WebDriver driver;
	
	@BeforeAll
	static void setUp() throws Exception {
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
		Thread.sleep(5000);
		driver.quit();
	}
	
	@Test
	@DisplayName("Navegação simples - Agendamento")
	@Order(1)
	public void navigatingScheduling() throws InterruptedException {
		driver.get("http://localhost:3000");
		
		Thread.sleep(1000);
		
		// Navegando para página de listagem de agendamentos
		getElementByXPath("//*[@id=\"navbarResponsive\"]/ul/li[6]/a").click();
		
		assertEquals("http://localhost:3000/listScheduling", driver.getCurrentUrl().toString());
		
		Thread.sleep(1000);
		
		// Navegando para página de cadastro de agendamentos por botão
		WebElement createButton = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/button");
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].click()", createButton);
		
		assertEquals("http://localhost:3000/createScheduling", driver.getCurrentUrl().toString());
		
		Thread.sleep(1000);
		
		// Cancelando cadastro e voltando para página de listagem
		WebElement cancelButton = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/button[2]");
		jse.executeScript("arguments[0].click()", cancelButton);
		
		assertEquals("http://localhost:3000/listScheduling", driver.getCurrentUrl().toString());
		
		Thread.sleep(1000);
		
		// Navegando para página de cadastro de agendamentos pelo navbar
		getElementByXPath("//*[@id=\"navbarResponsive\"]/ul/li[7]/a").click();
		
		assertEquals("http://localhost:3000/createScheduling", driver.getCurrentUrl().toString());
	}
	
	@Test
	@DisplayName("Criando Agendamento - Caso Positivo")
	@Order(2)
	public void createSchedulingOk() throws InterruptedException {
		driver.get("http://localhost:3000/createScheduling");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formattedDate = LocalDate.now().plusDays(1).format(dtf);
		
		writerFields(formattedDate, "15:00", "16:30", "Ginásio", "Futebol");
		
		WebElement saveButton = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/button[1]");
		saveButton.click();
		
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();
		
		
		assertEquals("Sucesso", title);
		assertEquals("Prática agendada com sucesso!", message);
		
		assertEquals("http://localhost:3000/listScheduling", driver.getCurrentUrl().toString());
		
		// Validações na tela de listagem de agendamento
		String tableBody = getElementByTagName("TBODY").getText();
		
		String date = LocalDate.now().plusDays(1).toString();
	
		Pattern p = Pattern.compile(String.format("\\n?.*%s.*15:00.*16:30.*Ginásio.*", date));
		Matcher m = p.matcher(tableBody);
		
		final String lineOnTable;
		if(m.find()) {
			lineOnTable = m.group(0);
		} else {
			lineOnTable = "";
		}
		
		assertAll("Verificando se elemento salvo está na listagem de agendamentos",
				() -> assertTrue(lineOnTable.contains(date)),
				() -> assertTrue(lineOnTable.contains("15:00")),
				() -> assertTrue(lineOnTable.contains("16:30")),
				() -> assertTrue(lineOnTable.contains("Ginásio")),
				() -> assertTrue(lineOnTable.contains("Futebol"))
		);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
	@DisplayName("Criando Agendamento - Casos Negativos")
	@Order(3)
	public void createSchedulingFail(int caseFail) throws InterruptedException {
		driver.get("http://localhost:3000/createScheduling");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String date;
		
		String errorMessage = null;
		
		switch (caseFail) {
			// Caso de colisão de agendamentos, passando os mesmos dados do teste CreateSchedulingOk
			case 1:
				date = LocalDate.now().plusDays(1).format(dtf);
				writerFields(date, "15:00", "16:30", "Ginásio", "Futebol");
				
				errorMessage = "Já existe uma prática agendada para esse horário!";
				break;
				
			// Caso de horário fora do funcionamento do campus
			case 2:
				date = LocalDate.now().plusDays(1).format(dtf);
				writerFields(date, "06:00", "07:00", "Ginásio", "Futebol");
				
				errorMessage = "O horário da prática deve ser entre 07:00 e 22:00";
				break;
				
			// Caso de duração inválida (menor que 0 minutos)	
			case 3:
				date = LocalDate.now().plusDays(1).format(dtf);
				writerFields(date, "10:00", "09:00", "Ginásio", "Futebol");
				
				errorMessage = "A duração da prática não deve ser igual ou menor que 0 minutos!";
				break;	
				
			// Caso de duração inválida (maior que o máximo permitido)	
			case 4:
				date = LocalDate.now().plusDays(1).format(dtf);
				writerFields(date, "09:00", "13:00", "Ginásio", "Futebol");
				
				errorMessage = "A prática agendada deve ter no máximo 180 minutos!";
				break;	
			
			// Caso de prática agendada para o passado	
			case 5:
				date = LocalDate.now().minusDays(1).format(dtf);
				writerFields(date, "09:00", "10:00", "Ginásio", "Futebol");
				
				errorMessage = "A data da prática não pode estar no passado!";
				break;	
				
			// Casos de campos em branco
			case 6:
				writerFields("", "09:00", "10:00", "Ginásio", "Futebol");
				
				errorMessage = "É obrigatório informar a data em que acontecerá a prática esportiva!";
				break;
				
			case 7:
				date = LocalDate.now().plusDays(1).format(dtf);
				writerFields(date, "", "10:00", "Ginásio", "Futebol");
				
				errorMessage = "É obrigatório informar o horário em que a prática esportiva começará!";
				break;
				
			case 8:
				date = LocalDate.now().plusDays(1).format(dtf);
				writerFields(date, "09:00", "", "Ginásio", "Futebol");
				
				errorMessage = "É obrigatório informar o horário em que a prática esportiva terminará!";
				break;
				
			case 9:
				date = LocalDate.now().plusDays(1).format(dtf);
				writerFields(date, "09:00", "10:00", null, "Futebol");
				
				errorMessage = "É obrigatório selecionar um local!";
				break;
				
			case 10:
				date = LocalDate.now().plusDays(1).format(dtf);
				writerFields(date, "09:00", "10:00", "Ginásio", null);
				
				errorMessage = "É obrigatório selecionar um esporte!";
				break;
		}
		
		Thread.sleep(1000);
		
		WebElement saveButton = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/button[1]");
		saveButton.click();
		
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();
		
		Thread.sleep(1000);
		
		assertEquals("Erro", title);
		assertEquals(errorMessage, message);
		
		assertEquals("http://localhost:3000/createScheduling", driver.getCurrentUrl().toString());
	}
	
	@Test
	@DisplayName("Excluindo Agendamento")
	@Order(4)
	public void deleteScheduling() throws InterruptedException {
		driver.get("http://localhost:3000/listScheduling");
		
		WebElement idElement = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr[1]/td[1]");
		String id = idElement.getText();
		
		WebElement deleteButton = getElementByXPath("//*[@id=\"root\"]/div/div[2]/header/fieldset/table/tbody/tr[1]/td[8]/button");
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].click()", deleteButton);
		
		Thread.sleep(1000);
		
		String tableBody = getElementByTagName("TBODY").getText();
		
		// Confirmando que elemento foi excluído
		assertFalse(tableBody.contains(id));
	}


	private void writerFields(String date, String startTime, String finishTime, String placeName, String sportName) throws InterruptedException {
		WebElement element;

		element = getElementById("lab01");
		element.sendKeys(date);
		
		Thread.sleep(1000);
		
		element= getElementById("lab02");
		element.sendKeys(startTime);
		
		Thread.sleep(1000);
		
		element= getElementById("lab03");
		element.sendKeys(finishTime);
		
		Thread.sleep(1000);
		
		Select places = new Select(getElementById("lab04"));
		if (placeName != null) {
			places.selectByVisibleText(placeName);
		}
		
		Thread.sleep(1000);
		
		Select sports = new Select(getElementById("lab05"));
		
		if (sportName != null) {
			sports.selectByVisibleText(sportName);
		}	
		
		Thread.sleep(1000);
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
