# Civilization

A 2D version of Civilization coded in Java using only the standard libraries.

![An example of the civilization game running](https://seanjparker.me/images/repos/gh_main_civ.png)

## Prerequisites
Building the Civilization source requires Java (JDK v1.8 or later).

Once installed, you can then build and run the program using the commands from the following section.

## Building a JAR file from source (Linux/Mac)
Open terminal/console, change directory to top level of the `civ-2d` folder
Then, run the following command:
```
$ ./create-civ
```

## Running the program
Once you have completed the above section, you can run the program using the following methods:

Run the following from the command line in the `civ-2d` directory to launch the JAR file from the command line:
```
$ java -jar civ-2d.jar
```
or, to launch the program using the compiled source
```
$ java -cp bin civ.core.Civilization
```
or double-click on the file `civ-2d.jar` to launch the program

## License
MIT
