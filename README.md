# Development Log
Just a running list of notes and commentary regarding development of this app

## April 7th, 2022
- Add Search Box for searching for Actors
- Begin basic UI Styling
    - Install Saas as a development dependency as described here: [The Simplest Sass Compile Setup|https://pineco.de/the-simplest-sass-compile-setup/]

## February 25th, 2022
- Enable OAuth2 authentication in the UI
- Using [react-google-login|https://www.npmjs.com/package/react-google-login]

## February 18th, 2022
- Enable OAuth2 authentication using Spring Security
- Create OAuth credentials on Google Cloud Platform

## February 12th - February 17, 2022
- Learning [React JS](https://reactjs.org/) Javascript UI library
- Begin building simple UI (no styling yet) for:
    - Actor search
    - Actor details
        - Movies for Actor
        - Flatrate subscriptions for each Movie 

## February 11th, 2022
- Create a GitHub [personal access token](personal access token) that can be used for reading GitHub Packages published to forked `themoviedbapi`
- Changes to GitHub Actions on forked `themoviedbapi`:
    - PR to `master` triggers build
    - Push to `master` triggers publication of SNAPSHOT artifact to GitHub Package
 - Changes to GitHub Actions on `movie-finder`
     - Added Maven `settings.xml` to repo with `<server>` configuration packages published to `themoviedbapi`
     - Mofied GitHub Action to make the Maven build use the `settings.xml` and also injecting GitHub environment variables such that Maven can authenticate Maven repository published as GitHub Packages on `themoviedbapi`
     - Used this GitHub Community post as a reference for the GitHub Action configuration: [using a GitHub Packages hosted repo in a Java/maven action](https://github.community/t/using-a-github-packages-hosted-repo-in-a-java-maven-action/18003/5)

## February 9th, 2022
- Push latest movie-finder to GitHub but [build is failing](https://github.com/justinhrobbins/movie-finder/runs/5131324411?check_suite_focus=true) due to `Could not find artifact com.github.holgerbrandl:themoviedbapi:jar:1.12-SNAPSHOT`
    - This is because I have forked `com.github.holgerbrandl:themoviedbapi` and made modifications. Currently the latest code only resides on my local dev environment.
    - Plan to resolve this:
        - Create GitHub Action that builds the forked `themoviedbapi` in my repository
        - Modify the Gradle build publish the SNAPSHOT of the forked `themoviedbapi` to a [GitHub Package](https://github.com/features/packages) in my repository

## February 8th, 2022
- Continue to add properties to Watch/Provider POJO

## February 7th, 2022
- Begin adding `watch/providers` functionality to forked themoviedbapi repository:
    - Add new Movie Controller to `movie-finder` with GET mapping for `movie/{movieId}/watchproviders`
    - Add new `MovieMethod` "watch_providers" which can be added to the `append_to_response` query parameter on the Movie endpoint
        - The actual `append_to_response` parameter is named `watch/providers` but slash cannot be used in the Enum `MovieMethod`. For now I added some special handling in the Utils class that is specific to the watch/providers use-case. Will need to consider a better permanent solution.
    - Add new `WatchProviders` POJO for JSON mapping. Had some issues getting the Jackson annotation configured correctly. Currently only has minimal properties to prove the JSON mapping is working. Will need to enhance this class later to include all the required watch/provider properties.
    - Confirmed reqeust to new `movie-finder` Controller now returns minimal `watch/provider` info

## February 4th, 2022
- Research whether it might be better in the long-term to use the existing [RAML](https://api.stoplight.io/v1/versions/9WaNJfGpnnQ76opqe/export/raml.yaml) or [OAS](https://api.stoplight.io/v1/versions/9WaNJfGpnnQ76opqe/export/oas.json) spec for [The Movie Database API](https://developers.themoviedb.org/3/getting-started/introduction) to generate Java client rather than modify the existing Java wrapper project [themoviedbapi](https://github.com/holgerbrandl/themoviedbapi/).
    - Received errors importing the RAML spec into Postman. Others reported this too: [The Movie Database Support: Postman Collection](https://www.themoviedb.org/talk/570931c4c3a36810b4000096)
    - Use [OpenAPI Generator](https://openapi-generator.tech/) to create a Java project using the OAS/Swagger spec
        - [Generate Spring Boot REST Client with Swagger] (https://www.baeldung.com/spring-boot-rest-client-swagger-codegen)
        - Errors when generating project from the published TheMovieDb API OAS spec: `-attribute definitions.image-path.type is not of type string`
        - Tried converting the RAML -> Swagger using [Mulesoft OAS/RAML converter](https://mulesoft.github.io/oas-raml-converter/)
        - OpenAPI Generator now created a project successfully but upon inspection, the resulting REST client seemed highly flawed (eg. client methods returning void when they should return a Person or Movie)
        - Forum post [The Movie Database Support: Swagger generator produces bad-named models](https://www.themoviedb.org/talk/58b926d992514160840085fd) confirmed the existing spec needs correction: "we'll be migrating to a new platform but there's nothing that can be done about this in the currently state of the docs"

## February 3rd, 2022
- Fork themoviedbapi to [justinhrobbins/themoviedbapi](https://github.com/justinhrobbins/themoviedbapi)
- The themoviedbapi uses Gradle for build automation and includes a Gradle wrapper (Gradle version 6.7)
- Had several issues building the project via `./gradlew` command
    - `zsh: permission denied: ./gradlew` <<< Fixed with `chmod` command as recommended by this blog post: [permission denied: ./gradlew](https://topherpedersen.blog/2021/05/05/zsh-permission-denied-gradlew/)
    - Build fails with `General error during semantic analysis: Unsupported class file major version 61` <<< Fixed this by using SdkMan to downgrad Java version from 17 to 11. Relevant resources for Gradle / Java compatability: `sdk use java 11.0.14-tem`
    - [Unsupported class file major version 61 error](https://stackoverflow.com/questions/69425829/unsupported-class-file-major-version-61-error)
    - [Gradle Compatibility Matrix](https://docs.gradle.org/current/userguide/compatibility.html#java)
    - [Using SDKMAN! to work with multiple versions of Java](https://www.twilio.com/blog/sdkman-work-with-multiple-versions-java)
- Install themoviedbapi built from sources to local Maven repository at .m2: `./gradlew publishToMavenLocal`
- Confirmed movie-finder is using locally built themoviedbapi by
    - Adding a logging statement into themoviedbapi
    - Building and publishing themoviedbapi to local Maven repository
    - Building and running movie-finder and confirming the new log statement is present in the logs

## February 2nd, 2022
- Research whether it's currently possible to get Watch Providers from the existing themoviedbapi Java wrapper library
- Created [GitHub issue on themoviedbapi project](https://github.com/holgerbrandl/themoviedbapi/issues/122) confirming whether Watch Providers functionality already exists. Offer to potentially build it mysefl and contribute it via PR back to themoviedbapi at a later time

## February 1st, 2022
- Enable and minimall Spring Boot [Actuator](https://docs.spring.io/spring-boot/docs/2.5.0/reference/htmlsingle/#actuator)
    - Enable `/info` endpoint as described in [Actuator Security](https://docs.spring.io/spring-boot/docs/2.5.0/reference/htmlsingle/#features.security.actuator)
    - Add *build info* to `/info` endpoint as described in Baeldung's [Customizing the /info Endpoint](https://www.baeldung.com/spring-boot-actuators#info-endpoint)
    - Add *git info* to `/info` endpoint as described in Spring doc's [Generate Git Information](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/html/howto.html). Note: The [docs for the new](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/html/howto.html#howto.build.generate-git-info) `3.0.0-SNAPSHOT` version Spring Boot indicate this plugin is upgraded from `<groupId>pl.project13.maven</groupId>` to the new `<groupId>io.github.git-commit-id</groupId>`

## January 31th, 2022
- Improve development lifecycle
    -  Use [GitHub Flow](https://docs.github.com/en/get-started/quickstart/github-flow) to manage git branches
    -  Use GitHub issues to track changes, including [linking Pull Requests to Issues](https://docs.github.com/en/issues/tracking-your-work-with-issues/linking-a-pull-request-to-an-issue)
    -  Use GitHub Actions for simple CI (details below)
    -  Add [Workflow status badge](https://docs.github.com/en/actions/monitoring-and-troubleshooting-workflows/adding-a-workflow-status-badge) to ReadMe.md
-  GitHub Actions ([Quickstart](https://docs.github.com/en/actions/quickstart))
    - [.github/workflows/maven.yml](https://github.com/justinhrobbins/movie-finder/blob/master/.github/workflows/maven.yml) workflow based on OOTB "Java with Maven" template
    - Workflow is configured to run on `push` to `master` branch, PR's to `master` and can be triggered manually via `workflow_dispatch` event.
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
