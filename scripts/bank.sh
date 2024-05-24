javac -d "./output" -cp \
"../lib/apiguardian-api-1.1.2.jar:../lib/junit-jupiter-api-5.10.2.jar:../lib/junit-jupiter-engine-5.10.2.jar:../lib/junit-platform-commons-1.10.2.jar:../lib/junit-platform-engine-1.10.2.jar:../lib//junit-platform-launcher-1.10.2.jar" \
"../java-solutions/info/kgeorgiy/ja/Presniakov_Arsenii/bank"/account/*.java \
"../java-solutions/info/kgeorgiy/ja/Presniakov_Arsenii/bank"/bank/*.java \
"../java-solutions/info/kgeorgiy/ja/Presniakov_Arsenii/bank"/person/*.java \
"../java-solutions/info/kgeorgiy/ja/Presniakov_Arsenii/bank"/*.java \

java -jar ../lib/junit-platform-console-standalone-1.10.2.jar --class-path ./output --select-class info.kgeorgiy.ja.Presniakov_Arsenii.bank.BankTest
