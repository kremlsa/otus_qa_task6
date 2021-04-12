import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features= "src/test/resources",
        glue= {"stepdefs"})
public class RunnerTest extends AbstractTestNGCucumberTests {

}