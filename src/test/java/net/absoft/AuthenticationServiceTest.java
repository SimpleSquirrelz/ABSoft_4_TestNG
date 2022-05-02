package net.absoft;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.absoft.data.Response;
import net.absoft.services.AuthenticationService;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class AuthenticationServiceTest extends BaseTest {

    private final int THREAD_SLEEP_TIME = 2000;

    @BeforeMethod
    public void setUp() {
        System.out.println(" buv povnistyu ");
    }

    @AfterMethod()
    public void tearDowns() {
        System.out.println(" prityanutiy ");
    }

    @Test(groups = {"auth-positive"})
    public void testSuccessfulAuthentication() {
        SoftAssert softAss = new SoftAssert();
        Response response = new AuthenticationService().authenticate("user1@test.com", "password1");

        softAss.assertEquals(response.getCode(), 200, "Response code should be 200");
        softAss.assertTrue(validateToken(response.getMessage()),
                "Token should be the 32 digits string. Got: " + response.getMessage());

        softAss.assertAll();
    }

    @Test(groups = {"auth-positive"})
    public void testFail(){
        fail();
    }

    @DataProvider(name = "auth-negative-data")
    public Object[][] NegativeDataProvider() {
        return new Object[][]{
                {"user1@test.com", "wrong_password1", new Response(401, "Invalid email or password")},
                {"", "password1", new Response(400, "Email should not be empty string")},
                {"", "password1", new Response(400, "Email should not be empty string")},
                {"user1", "password1", new Response(400, "Invalid email")},
                {"user1@test", "", new Response(400, "Password should not be empty string")},
        };
    }

    @Test(groups = {"auth-negative", "data-provider"},
            dataProvider = "auth-negative-data")
    public void testAuthenticationWithInvalidData(String email, String password, Response expectedResponse) {
        assertNegativeResponses(email, password, expectedResponse);
    }

    @Test(groups = {"auth-negative"})
    public void testAuthenticationWithWrongPassword() {
        assertNegativeResponses("user1@test.com", "wrong_password1",
                new Response(401, "Invalid email or password"));
    }

    @Test(groups = {"auth-negative"})
    public void testAuthenticationWithEmptyEmail() {
        assertNegativeResponses("", "password1",
                new Response(400, "Email should not be empty string"));
    }

    @Test(groups = {"auth-negative"})
    public void testAuthenticationWithInvalidEmail() {
        assertNegativeResponses("user1", "password1",
                new Response(400, "Invalid email"));
    }

    @Test(groups = {"auth-negative"})
    public void testAuthenticationWithEmptyPassword() {
        assertNegativeResponses("user1@test", "",
                new Response(400, "Password should not be empty string"));
    }

    private void assertNegativeResponses(String email, String password, Response expectedResponse) {
        assertNegativeResponses(email, password, expectedResponse, "Responses should be the same");
    }

    private void assertNegativeResponses(String email, String password, Response expectedResponse, String message) {
        try {
            Thread.sleep(THREAD_SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Response actualResponse = new AuthenticationService().authenticate(email, password);
        assertEquals(actualResponse, expectedResponse, message);
    }

    private boolean validateToken(String token) {
        final Pattern pattern = Pattern.compile("\\S{32}", Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(token);
        return matcher.matches();
    }
}