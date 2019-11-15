# CPSC501Assignment3

to get ip do:

hostname -I | awk '{print $1}'
cd src/main/<br>
javac -cp ".:../../../jdom-2.0.2.jar" Server.java<br>
java -cp ".:../../../jdom-2.0.2.jar" Server

or 

javac -cp ".:../../../jdom-2.0.2.jar" Client.java<br>
java -cp ".:../../../jdom-2.0.2.jar" Client
