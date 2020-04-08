# Welcome to Spring-Loaded

## What is Spring Loaded?

Spring Loaded is a JVM agent for reloading class file changes whilst a JVM is running.  It transforms
classes at loadtime to make them amenable to later reloading. Unlike 'hot code replace' which only allows
simple changes once a JVM is running (e.g. changes to method bodies), Spring Loaded allows you
to add/modify/delete methods/fields/constructors. The annotations on types/methods/fields/constructors
can also be modified and it is possible to add/remove/change values in enum types.

Spring Loaded is usable on any bytecode that may run on a JVM, and is actually the reloading system
used in Grails 2.

## Build

```bash
./gradlew clean build
```

Generated jar file `springloaded/build/libs/springloaded-{VERSION}.jar`

## Use

#### Tomcat

Add extra java options in `catalina.sh` before start

```bash
JAVA_OPTS="${JAVA_OPTS} -javaagent:/path/to/springloaded-{VERSION}.jar -noverify"
```

#### Springboot

For springboot application, you have to unarchived the jar, otherwise the class file replacement won't work:

```bash
jar xf app.jar
java -javaagent:/path/to/springloaded-{VERSION}.jar -noverify pkg.to.MainClass
```

## Verified functionality

**Works**

- Change method body (include construction method)
- Add method
- Remove method
- Add static member variable
- Add non-static member variable
- Add construction method without parameter
- Add new class/interface/enum
- Add or modify enum item
- Add or remove class annotation
- Add or remove method in interface
- Modify non-static member variable

**Won't work**

- Modify main method
- Add construction method with parameter
- Modify static member variable

**Won't work and cause exception**

- Add or modify class inherit relationship
- Add or modify implemented interface list of class

## More information

See [origin repository](https://github.com/spring-projects/spring-loaded.git)
