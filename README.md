# Development Log
Just a running list of notes and commentary regarding development of this app

## January 31th, 2022
- Improve development workflow
    -  Use [GitHub Flow](https://docs.github.com/en/get-started/quickstart/github-flow) to manage git branches
    -  Use GitHub issues to track changes, including [linking Pull Requests to Issues](https://docs.github.com/en/issues/tracking-your-work-with-issues/linking-a-pull-request-to-an-issue)
    -  Use GitHub Actions for simple CI (details below)
    -  Add [Workflow status badge](https://docs.github.com/en/actions/monitoring-and-troubleshooting-workflows/adding-a-workflow-status-badge) to ReadMe.md
-  GitHub Actions ([Quickstart](https://docs.github.com/en/actions/quickstart))
    - [.github/workflows/maven.yml](https://github.com/justinhrobbins/movie-finder/blob/master/.github/workflows/maven.yml) workflow based on OOTB "Java with Maven" template
    - Created GitHub Actions [encrypted secret](https://docs.github.com/en/actions/security-guides/encrypted-secrets) to inject required Environment Variable into the build

## January 30th, 2022
- Inject TMDB API Key as an environment variable
   - VS Code: Added Environment Variable to launch.json as described in the [Configure](https://code.visualstudio.com/docs/java/java-debugging#_configure) section of [Running and debugging Java](https://code.visualstudio.com/docs/java/java-debugging)
   - How to add the Environment Variable when building / starting the app from the command line:
       - Build: `mvn clean install -Dtmdb.api.key=ReplaceWithApiKeyValue`
       - Run: `mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DTMDB_API_KEY=ReplaceWithApiKeyValue"`
       - Other Option: `export TMDB_API_KEY="ReplaceWithApiKeyValue"` then build/start normally
- VS Code "Problems" tab was WARNING about `'tmdb.apikey' is an unknown property.`
    - This property had been added to the application.properties and was being injected into a Controller
    - Resolved by:
        - Createing a `@ConfigurationProperties` as described in Spring's [Configuration Metadata](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html) docs
        - Enabling `@ConfigurationPropertiesScan` for the Application
        - Adding option dependency on `spring-boot-configuration-processor` which generates the metadata needed by VS Code to recognize the property

## January 29th, 2022
- Move Development Log to GitHub Pages
    - [justinhrobbins.github.io/movie-finder/](https://justinhrobbins.github.io/movie-finder/)

## January 28th, 2022
- Remove api_key value from existing PostMan collection 
- Add Postman collection to GitHub (either [through Postman](https://learning.postman.com/docs/integrations/available-integrations/github/) integration or manually)
    - Tried to do it via Postman integration but it requires an upgrade to a PAID account
		    - [Backing up collections on GitHub](https://learning.postman.com/docs/integrations/available-integrations/github/)
	- Created new GitHub repo through browser
		- [movie-finder-postman](https://github.com/justinhrobbins/movie-finder-postman)

## January 27th, 2022
- Create git repo: [movie-finder](https://github.com/justinhrobbins/movie-finder)
- Create first Spring MVC Controller
- Basic usage of [themoviedbapi](https://github.com/holgerbrandl/themoviedbapi/) Java wrapper
- Run / Debug from within VS Code
- TheMovieDb forum post [Multiple results for Brad Pitt](https://www.themoviedb.org/talk/61f3504c64de35001bad54e6)
- The themoviedbapi Java wrapper Github Issue comment on [Support X-RateLimit headers](https://github.com/holgerbrandl/themoviedbapi/issues/103)
