# Link Shortener Application
Unit and Integration Tests for the application is saved in this repository. The main point of making the app was to learn unit and integration testing.

I have used TDD throughout the development.

Here's the basic usage of the application.
```
POST : app/shorten/really_long_url ->
	{
		status:201/3xx/403/500,
		id: 78951,
		original: really_long_url
	}
GET : app/url/id ->
	{
		status:201/3xx/403/500,
		short:adf.ly/something,
		original:really_long_url
	}
```

Notes:
```
@RunWith(SpringRunner.class or whatever runner) 
	enables Spring integration testing
@ContextConfiguration(classes = Config.class) 
	loads the Spring application context (not full as @SpringApplication ) as it is defined in the Config.class
@SpringApplicationConfiguration(classes = Config.class)
	loads the Spring application context in its full form: enables logging, loads external properties(application.properties, application.yml), etc.

MockMvc mocks Spring MVC in a way like the application is running in a container, but not actually. MockMvc can be built using MockMvcBuilders. This builder has 2 important methods
	- standaloneSetup()
			Useful for unit testing the controller. It expects the controller(s) to be manually initialized by the developer.
	- webAppContextSetup()
			Expects Spring Application Context to be provided. Doesn't expect controller(s) to be loaded explicitly. Good for integration testing.
```