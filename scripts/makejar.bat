javac -d . -cp ..\..\java-advanced-2024\artifacts\info.kgeorgiy.java.advanced.implementor.jar ..\java-solutions\info\kgeorgiy\ja\Presniakov_Arsenii\implementor\Implementor.java

# :NOTE: generate not executable jar
jar cmvf .\META-INF\MANIFEST.MF Implementor.jar .\info\kgeorgiy\ja\Presniakov_Arsenii\implementor\*.class

pause