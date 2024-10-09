package pages;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;

public class GmailPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public GmailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    
    public void login(String email, String password) {
        try {
        	System.out.print("login");
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("identifierId")));
            emailField.sendKeys(email);
            driver.findElement(By.id("identifierNext")).click();
            Thread.sleep(2000);
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='password']/div[1]/div/div[1]/input")));
            passwordField.sendKeys(password);
            driver.findElement(By.id("passwordNext")).click();
            
            if (isAlertPresent()) {
                handleAlert();
            }
            
            // Call the screenshot method after a successful login
            takeScreenshotAfterLogin("D:/gmail_login_success.png");
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }


	public void composeEmail(String recipient, String subject, String body) {
        try {
            WebElement composeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Compose']")));
            composeButton.click();

            WebElement toField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='To']//input[@aria-label='To recipients']")));
            toField.sendKeys(recipient);

            WebElement subjectField = wait.until(ExpectedConditions.elementToBeClickable(By.name("subjectbox")));
            subjectField.sendKeys(subject);

            WebElement bodyField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@aria-label='Message Body']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", bodyField);
            bodyField.sendKeys(body);

            WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Send']")));
            sendButton.click();

            // Wait for the "Message sent" confirmation pop-up
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']//div[@class='vh']")));
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            System.out.println("Error composing email: " + e.getMessage());
        }
    }
  
   
	public void navigateToSentAndVerify(String subject) {
	    try {
	        // Click on the Sent folder
	        WebElement sentFolder = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='TN bzz aHS-bnu']//div[@class='aio UKr6le']")));
	        sentFolder.click();
	        System.out.println("Clicked on Sent folder.");
	        Thread.sleep(4000);

	        // Wait for the email list to load
	        List<WebElement> sentEmails = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@role='main']//table[@class='F cf zt']//tr[contains(@class, 'zA')]")));

	        if (sentEmails.isEmpty()) {
	            System.out.println("Sent folder is empty.");
	            return; 
	        }

	        // Check for the subject in the list of sent emails
	        boolean isEmailFound = sentEmails.stream()
	                .peek(row -> System.out.println("Row text: " + row.getText())) 
	                .anyMatch(row -> {
	                    try {
	                        String emailSubject = row.findElement(By.xpath(".//span[@class='bog']")).getText().trim();
	                        return emailSubject.equals(subject.trim());
	                    } catch (NoSuchElementException e) {
	                        return false; 
	                    }
	                });

	        if (isEmailFound) {
	            System.out.println("Test Passed: Email with subject '" + subject + "' found in Sent folder.");
	        } else {
	            System.out.println("Test Failed: Email with subject '" + subject + "' not found in Sent folder.");
	        }
	    } catch (Exception e) {
	        System.out.println("Error navigating to Sent folder and verifying email: " + e.getMessage());
	    }
	}

	public void readFirstInboxEmail() {
	    try {
	        System.out.print("Inbox email reading... ");
	        WebElement inboxTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='TN bzz aHS-bnt']//div[@class='aio UKr6le']")));
	        inboxTab.click();
	        Thread.sleep(2000);
	        WebElement emailList = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@class='F cf zt']")));
	        List<WebElement> emails = emailList.findElements(By.xpath(".//tr[contains(@class, 'zA')]"));
	        
	        if (emails.isEmpty()) {
	            System.out.println("Inbox is empty.");
	            return; 
	        }
	        
	        // Assuming you want to read the first email
	        WebElement firstEmail = emails.get(0);
	        Actions actions = new Actions(driver);
	        actions.moveToElement(firstEmail).click().perform();
	        
	        // Now, fetch the subject and body of the email
	        String subject = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@class='hP']"))).getText();
	        String body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='a3s aiL ']"))).getText();
	        
	        System.out.println("Subject: " + subject);
	        System.out.println("Body: " + body);

	    } catch (Exception e) {
	        System.out.println("Error reading email: " + e.getMessage());
	    }
	}
    public void logout() {
        try {
            Thread.sleep(1000);
            WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class,'gb_A gb_Za gb_0')]")));
            profileIcon.click();
            Thread.sleep(3000);
            String jsSelector = "var element = document.querySelector(\"a[class='V5tzAf jFfZdd Dn5Ezd'] div[class='SedFmc']\"); if (element) { element.click(); } else { console.error('Element not found'); }";
            ((JavascriptExecutor) driver).executeScript(jsSelector);
            
            System.out.println("Successfully signed out.");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Error logging out: " + e.getMessage());
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        }
    }
    
    private void takeScreenshotAfterLogin(String filePath) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='TN bzz aHS-bnt']")));
            TakesScreenshot ts = (TakesScreenshot) driver;
            File srcFile = ts.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved to: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }
    
    public void handleAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert text: " + alert.getText());
            alert.accept(); 
        } catch (NoAlertPresentException e) {
            System.out.println("No alert present: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error handling alert: " + e.getMessage());
        }
    }
    
    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }
}
