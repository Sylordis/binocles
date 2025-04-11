# Binocles

The software’s purpose is to facilitate the beta reading of texts by their users. It allows the user to:
- define and manage their own commenting nomenclature with different comment styles.
- manage the texts in different books containing chapters, each text being one chapter.
- comment the texts with comments of different types with visual feedback and indicators on the text in review.
- save their current work to re-open it at a later date.
- export/import current or previous work.
- render their reviews in different formats so it can be displayed or shared easily on different platforms.

# Dependencies

Only [Java 21 JDK](https://openjdk.org/projects/jdk/21/) is required for runtime, as all dependencies are included in the jar.

This software is developed with [Gradle](https://gradle.org/) as dependency manager.

The dependencies used by this project are indicated in the different gradle files:
- [java common conventions](buildSrc/src/main/groovy/com.github.sylordis.binocles.java-common-conventions.gradle) for common dependencies
- [model](model/build.gradle)
- [ui](ui/build.gradle) for UI related things (like JavaFX)
- [utils](utils/build.gradle)

The project is a gradle multi-project with the following sub-projects:

- `model` model of the application
- `ui` everything regarding the UI.
- `utils` utils unrelated to business logic

# Installation

Just download the latest JAR.

# Executing the software

The project is still in development, there are no executable yet.

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

# Report issues

Please report any found issues to [issues on Github](https://github.com/Sylordis/binocles/issues)

# Author & Contributors

The only contributor and author is Sylvain "[Sylordis](https://github.com/Sylordis/)" Domenjoud.

# License

This project is distributed under the license [GNU Affero General Public License v3.0](https://www.gnu.org/licenses/agpl-3.0.en.html).

# Links

Project website: [https://github.com/Sylordis/binocles](https://github.com/Sylordis/binocles)
