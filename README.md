# 2048MMOG
A multi-user contest version of 2048

**Project setup:**

Build Client:

`cd client`
`npm install`
`npm run devbuild`

Because the application plugin is being used, you may directly run the application:
`./gradlew run`

...OR...

The build.gradle uses the Gradle shadowJar plugin to assemble the application and all it’s dependencies into a single "fat" jar.

To build the "fat jar"
`./gradlew shadowJar`

You may also run the fat jar as a standalone runnable jar:
`java -jar build/libs/gradle-simplest-3.2.1-fat.jar`
(You can take that jar and run it anywhere there is a Java 8+ JDK. It contains all the dependencies it needs so you don’t need to install Vert.x on the target machine).

Now point your browser at http://localhost:8080