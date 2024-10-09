package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExcelUtils;
import pages.GmailPage;
import java.util.List;

public class GmailAutomation {
    public static void main(String[] args) {
        String excelPath = "D:/gmail_enteries.xlsx"; 
        ExcelUtils excelUtils = new ExcelUtils(excelPath, "Sheet1"); 
        List<String[]> testData = excelUtils.getData();
        
        // Set the desired browser type
        String browserType = "chrome";

       
        WebDriver driver = createDriver(browserType);
        GmailPage gmailPage = new GmailPage(driver);

        for (int i = 1; i < testData.size(); i++) { 
            String[] row = testData.get(i);
            String email = row[0];
            String password = row[1];
            String recipient = row[2];
            String subject = row[3];
            String body = row[4];

            // Launch Gmail
            System.out.print("Launch gmail");
            driver.manage().window().maximize();
            driver.get("https://www.gmail.com");

            // Log in to Gmail
            System.out.print("Login Gmail");
            gmailPage.login(email, password);

            // Compose and send the email
            System.out.print("Compose Email");
            gmailPage.composeEmail(recipient, subject, body);

            // Navigate to Sent folder and verify the email
            gmailPage.navigateToSentAndVerify(subject);

            // Read the first email in the inbox
            gmailPage.readFirstInboxEmail();

            // Log out from Gmail
            gmailPage.logout();
        }

        // Close resources
        driver.quit();
        excelUtils.close();
    }

    private static WebDriver createDriver(String browserType) {
        WebDriver driver = null;
        switch (browserType.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
             //   options.addArguments("headless");
                driver = new FirefoxDriver(options);
                options.addPreference("dom.webnotifications.enabled", false);
                options.addPreference("permissions.default.desktop-notification", 2);
                options.addPreference("permissions.default.camera", 2);
                options.addPreference("permissions.default.microphone", 2);
                break;
            default:
                throw new IllegalArgumentException("Browser type not supported: " + browserType);
        }
        return driver;
    }
}
