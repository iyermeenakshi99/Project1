package Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class CrossAssyst {
	
	WebDriver driver;
	JavascriptExecutor js= (JavascriptExecutor)driver;
	Properties prop;
	
	@BeforeTest
	public void dataInitialization() throws IOException
	{
		prop=new Properties();
		FileInputStream fis = new FileInputStream("C:\\Users\\Admin\\eclipse-workspace\\Cross_Assyst\\src\\Test\\data.properties");
		prop.load(fis);		
	}
	
	@BeforeTest
	public void driverInitialization() 
	{
	System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\Downloads\\chromedriver_win32\\chromedriver.exe");
	driver=new ChromeDriver();
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	driver.get(prop.getProperty("URL"));
	}
	
	@BeforeTest
	public void jSEInitialization()
	{
		js= (JavascriptExecutor)driver;
	}
	
	
	
	//Create Account
	@Test(priority=1)
	public void createAccount()
	{		
		driver.findElement(By.xpath("//a[contains(text(),'Sign in')]")).click();
		driver.findElement(By.cssSelector("input[id='email_create']")).sendKeys(prop.getProperty("Email"));
		driver.findElement(By.id("SubmitCreate")).click();		
		driver.findElement(By.xpath("//input[@id='id_gender2']")).click();
		driver.findElement(By.id("customer_firstname")).sendKeys(prop.getProperty("FName"));
		driver.findElement(By.id("customer_lastname")).sendKeys(prop.getProperty("LName"));
		driver.findElement(By.id("passwd")).sendKeys(prop.getProperty("Password"));		
		Select s= new Select(driver.findElement(By.xpath("//select[@id='days']")));
		s.selectByIndex(9);		
		Select s1= new Select(driver.findElement(By.xpath("//select[@id='months']")));
		s1.selectByIndex(9);		
		Select s2= new Select(driver.findElement(By.xpath("//select[@id='years']")));
		s2.selectByValue(prop.getProperty("Year"));		
		driver.findElement(By.id("optin")).click();	
		driver.findElement(By.id("address1")).sendKeys(prop.getProperty("Address"));
		driver.findElement(By.id("city")).sendKeys(prop.getProperty("City"));	
		Select s3= new Select(driver.findElement(By.xpath("//select[@name='id_state']")));	
		s3.selectByVisibleText(prop.getProperty("State"));	
		driver.findElement(By.id("postcode")).sendKeys(prop.getProperty("Postcode"));
		driver.findElement(By.id("phone_mobile")).sendKeys(prop.getProperty("Number"));
		driver.findElement(By.xpath("//button[@name='submitAccount']")).click();	
		driver.findElement(By.xpath("//a[@title='Log me out']")).click();
	}
	
	//Login 	
	@Test(priority=2)
	public void login() 
	{				
		driver.findElement(By.xpath("//a[contains(text(),'Sign in')]")).click();
		driver.findElement(By.id("email")).sendKeys(prop.getProperty("Email"));
		driver.findElement(By.id("passwd")).sendKeys(prop.getProperty("Password"));
		driver.findElement(By.id("SubmitLogin")).click();
	}
		
	//Placing Order
	@Test(priority=3)
	public void placingOrder() throws InterruptedException
	{	
		driver.findElement(By.xpath("//a[@title='Women']")).click();
		Thread.sleep(2);		
		WebElement ele1=driver.findElement(By.xpath("//a[@class='product_img_link'] //img[@src='http://automationpractice.com/img/p/8/8-home_default.jpg']"));
		js.executeScript("arguments[0].scrollIntoView();", ele1);			
		Actions action= new Actions(driver);
		action.moveToElement(ele1).build().perform();		
		Thread.sleep(2);		
		driver.findElement(By.xpath("//div[@class='product-image-container'] //a[@href='http://automationpractice.com/index.php?id_product=3&controller=product'] //span[contains(text(),'Quick view')]")).click();		
		driver.switchTo().frame(0);		
		driver.findElement(By.className("icon-plus")).click();
		driver.findElement(By.xpath("//button[@name='Submit']")).click();
		driver.switchTo().defaultContent();
	}	
		
	//Checkout
	@Test(priority=4)
	public void checkout() 	
	{		
		driver.findElement(By.className("button-medium")).click();		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//p[@class='cart_navigation clearfix'] //a[@title='Proceed to checkout']")));		
		String txt2=driver.findElement(By.xpath("//span[@id='total_price']")).getText();
		Assert.assertEquals(txt2, prop.getProperty("amount"));
		System.out.println("Total Amount verification Done!!");		
		driver.findElement(By.xpath("//p[@class='cart_navigation clearfix'] //a[@title='Proceed to checkout']")).click();
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//button[@type='submit']//span[contains(text(),'Proceed to checkout')]")));
		driver.findElement(By.xpath("//button[@type='submit']//span[contains(text(),'Proceed to checkout')]")).click();	
		driver.findElement(By.xpath("//input[@name='cgv']")).click();
		driver.findElement(By.xpath("//button[@name='processCarrier']")).click();		
		String txt3=driver.findElement(By.xpath("//span[@id='total_price']")).getText();
		Assert.assertEquals(txt3, prop.getProperty("amount"));
		System.out.println("Total Amount verification Done!!");		
		driver.findElement(By.xpath("//a[@title='Pay by check.']")).click();		
		driver.findElement(By.xpath("//button[@type='submit']/span[contains(text(),'I confirm my order')]")).click();
	}	
	
	//Verify the amount Under ORDER HISTORY
	@Test(priority=5)
	public void verify()
	{
		driver.findElement(By.xpath("//a[@title='View my customer account']")).click();
		driver.findElement(By.xpath("//a[@title='Orders']")).click();		
		String txt=driver.findElement(By.xpath("//td[@class='history_price']/span")).getText();
		Assert.assertEquals(txt, prop.getProperty("amount"));
		System.out.println("Final Amount verification Done!!");				
	}
	
	@AfterTest
	public void close()
	{
		driver.findElement(By.className("logout")).click();		
		driver.close();
	}
	

}
