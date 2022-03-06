# studentservice
The Spring Boot application with performed testing by using AssertJ library and mocks.

**Basic info:**
- Unit Testing is a testing in isolation. In Integration Testing we combine all unit tests and we do not use a mock of repository. In intergration testing we send real get, post, deleted requests to whole app
- JUNIT5 is a Java Testing Frameworks
- Annotation examples - @Tests, @ParamizedTest, @RepetedTest, @BeforeAll
- In the Class Assertion is for examples method assertEqual, but there is better class called AssertJ.
- In the Class AssertJ methods start with an assertThat and then we choose method as startsWith and hasSize etc even in the same line like in streams
- Exceptions in AssertJ are written like assertThatThrownBy(() → {throw new Exception(“boom”);})


**Maven:**
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
	<scope>test</scope>
</dependency>

**Create a simple unit test:**
- Write test in a package called test
- tests are divided to 3 main parts – given (introduce variables), when (action), then (assert result)
- Use Jupiter5 (org.junit.jupiter.api) to use test (@Test) 
and use AssertJ (org.assertj.core.api.Assertions) to user assertThat method
- crtl+alt+v to extract the variable (from a method or constant value)

**H2 database:**
H2 database is the in-memory database. Instead of testing tests by using real database instance, we can run in H2 database, but first the configuration is needed.
1) Add dependencies in a pom file
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<scope>test</scope>
</dependency>
2) Add a resource directory (folder) in a test folder (under the main one)
3) Copy an application.properties from the resources that exists in the main directory and paste to the resources in a test folder that has been already created
4) Adjust new application.properties and add connection to the H2 database

**Spring Repository Unit Test:**
- Repository is an interface that extends JpaRepository that gives methods as save, delete, findAll etc. Moreover there can be initialized my own method that will influence on the database.
- Test only own prepared method from a repository, because those methods that are not created by myself, but provided by JPA (as save, delete etc) are already checked.
1) Go to the repository interface and press shift+command+t to create a new, plain test.
The test is prepared automatically in the test directory with the same package and name (with extra word test) as the tested interface
2) Provide an instance of the repository and auto wired it (@Autowired) to have a real instance 
3) given – an object (student); when – method from the tested interface; then – assert that (ex. is true)
4) Add @DataJpaTest above the class to glue a prepared test with a Spring project – an auto wired repository and database (H2)
5) TearDown method is used to delete tests at the end (press alt+enter). In a method add for example underTest.deleteAll(). Then we have a clean state (ex. we can test with a save method to database).


**Spring Service Unit Test with Mockito:**
- In a Service layer is described business logic, so the methods that are initialized in the repository or that which are given by JPA extension, are developed.
- In Service Class we have a reference to Repository, so in a Service use a mock (~sztuczny model) of this Repository to test Service by using Mockito Library.
- Mocks allow to replace a real object (as repository) with an object that can be controlled in tests. The benefit from using @Mock in a unit test is speed because we do not have to bring database, create a table, insert a new modeled object as Student, so all what was already tested in a Repository Unit Test (avoid repeating unit tests).
    1. Test a Get Method from a Service Class
1) Go to the Service Class, press sift+crtl+t and choose set up and those methods that you want to test. If you choose more methods then you can always add @Disabled annotation, then those won’t run.
2) Initialize (make a reference) to the Repository (will be needed to initialize a service) with @Mock.
3) Initialize Service with name underTest above and inside the setUp method.
4) Above a Test Class add @ExtendedWith(MockitoExtension.class)
5) Change name of a method that you want to test that will look as a question (can, is, exists)
6) Create a test: given – is already done in a setup; when – provide a method that you want to test; 
then – when the method is called – verify(repositoryReference).methodFromRepoThatMakesGoodTest
Verify is imported as a static method from mockito.org
 
**Test an Add Method from a Service Class (with Argument Capture):**
In the above test you were checking if you can find/get results (so verify with Mokito library). 
Now check adding an object to the mocked repo and then compare results (assert that with AssertJ).
1) Change name of tested method to sound like a question
2) given – create a modeled object ex. Student; when – invoke/call an add method to be tested; 
then – check if the repository was invoked with the same object that was passed in the when section.
3) given:
- Initialize a captured object of the model object by using ArgumentCapture, so take a capture/shot from an added object (described in a Model Class) that was adding to the database by using mocked repository 
	Remember that in the Add Method in the real Service Class (that is tested now) an object is 	being added to the database. 
	In the “When Section” in your test is performed the Add Method, so the object is being added. 	Moreover, in the Add Method is used a Save method from JPA Library.
- Point what method you want to verify ex. the save method that is presented in the add method.
- Take a value of the captured object that was taken during performing the Add Method
- compare (assert that) the captured object (value) with the real object from the Model Class
	Remember that the under test service receives a modeled object is the When Section


**Test a throw method:**
1) Compare when and then by wrapping up a method with assert that thrown by
+ We can control what happened and even force the test to make a thrown by given method from the Mockito Library. Hence, if we want to have true then we can force that ex. the email does not exist 
and then I can check if in this situation the test will pass
2) Check if a thrown message is the same as we want to (so the same as was in the real, under test service) by using has message containing
3) Remember that thrown new works like a break in a function so it stops the method, so we can check if after thrown the object for sure will not the save to the database (if after throw is save method)

