# Binocles

The software’s purpose is to facilitate the beta reading of texts by their users. It allows the user to:
- define and manage their own commenting nomenclature with different comment styles.
- manage the texts in different books containing chapters, each text being one chapter.
- comment the texts with comments of different types with visual feedback and indicators on the text in review.
- save their current work to re-open it at a later date.
- export/import current or previous work.
- render their reviews in different formats so it can be displayed or shared easily on different platforms.

# Dependencies

Only [Java 21 JDK](https://openjdk.org/projects/jdk/21/) is required for runtime.

This software is developed with [Gradle](https://gradle.org/) as dependency manager.

It uses the following dependencies (already included as-is in the jar):

- [Apache Commons Text](https://commons.apache.org/proper/commons-text/)
- [JavaFX 21](https://openjfx.io/)
- [Guava](https://github.com/google/guava)
- [Log4J](https://logging.apache.org/log4j/2.x/)
- [RichTextFX](https://github.com/FXMisc/RichTextFX)
- [SnakeYAML](https://bitbucket.org/snakeyaml/snakeyaml/src)

Testing dependencies:

- [JUnit 5 (Jupiter)](https://junit.org/junit5/)
- [Hamcrest](https://hamcrest.org/JavaHamcrest/)
- [Mockito](https://site.mockito.org/)

The project is a gradle multi-project with the following sub-projects:

- `model` model of the application
  - uses `utils` as dependency
- `ui` everything regarding the ui
  - uses `model` and `utils` as dependency
- `utils` utils unrelated to business logic

# Installation

Just download the latest JAR.

# Executing the software

The project is still in development, there are no executable yet.

From the `ui` project:

```
gradle run
```

In order to open a file when starting the software with gradle:

```
gradle run --args='<path-to-file>'
```

# Compilation

`./gradlew compileJava`

# Unit tests

`./gradlew test`

# Help

Check out the [wiki on Github](https://github.com/Sylordis/binocles/wiki).

# Author & Contributors

The only contributor and author is Sylvain "[Sylordis](https://github.com/Sylordis/)" Domenjoud.

# License

This project is distributed under the license [GNU Affero General Public License v3.0](https://www.gnu.org/licenses/agpl-3.0.en.html).

# Links

Project website: https://github.com/Sylordis/binocles
