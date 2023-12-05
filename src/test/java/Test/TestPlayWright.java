package Test;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestPlayWright {

    private static Page page;
    private static final String ACCEPT_BUTTON = "Alles Accepteren";
    private static final String CLOSE_BUTTON = "Sluit venster";
    private static final String TEST_URL = "https://www.bol.com/nl/nl/";
    private static final String SELECTED_ARTICLE = "call of duty: modern warfare 3";
    private static final String EXPECTED_VALUE = "1";
    private static final String SEARCH_BAR = "#searchfor";
    private static final String SEARCH_BUTTON = "Zoeken";
    private static final String CARD_BUTTON = "//*[@id=\"9300000153468617\"]";
    private static final String LABEL = "aria-label";
    private static final String EXPECTED_BUTTON_TEXT = "In winkelwagen";
    private static final String COUNT = "Aantal";
    private static final String ERROR = "Page object is null. Check initialization.";

    @BeforeAll
    public static void setUp() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium()
                .launch(new BrowserType
                        .LaunchOptions()
                        .setHeadless(false));
        BrowserContext context = browser.newContext();
        page = context.newPage();
    }

    private static void handlePopups() {
        Locator acceptButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(
                        Pattern.compile(ACCEPT_BUTTON, Pattern.CASE_INSENSITIVE)));
        Locator closeButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(
                        Pattern.compile(CLOSE_BUTTON, Pattern.CASE_INSENSITIVE)));
        acceptButton.waitFor();
        acceptButton.click();
        closeButton.waitFor();
        closeButton.click();
    }

    public static void throwError(){
        System.out.println(ERROR);
    }

    @Test
    public void shouldNavigateToPage(){
        if(page != null){
            page.navigate(TEST_URL);
            handlePopups();
            Locator searchBar = page.locator(SEARCH_BAR);
            assertThat(searchBar).isVisible();
        }else {
            throwError();
        }
    }

    @Test
    public void shouldSearchArticle() {
        if (page != null) {
            Locator searchBar = page.locator(SEARCH_BAR);
            searchBar.waitFor();
            searchBar.fill(SELECTED_ARTICLE);
            Locator searchButton = page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName(
                            Pattern.compile(SEARCH_BUTTON, Pattern.CASE_INSENSITIVE)));
            searchButton.waitFor();
            searchButton.click();
            assertThat(searchBar).hasValue(SELECTED_ARTICLE);
        } else {
            throwError();
        }
    }

    @Test
    public void shouldAddArticleToCart() {
        if (page != null) {
            Locator cartButton = page.locator(CARD_BUTTON);
            assertThat(cartButton).hasAttribute(LABEL, EXPECTED_BUTTON_TEXT);
            cartButton.waitFor();
            cartButton.click();
        } else {
            throwError();
        }
    }

    @Test
    public void shouldBeArticleInCart() {
        if (page != null) {
            Locator itemCount = page.getByLabel(COUNT);
            assertThat(itemCount).hasValue(EXPECTED_VALUE);
        } else {
            throwError();
        }
    }

}
