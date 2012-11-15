To run the agents you need to do the following
1. Download JADE framework from http://jade.tilab.com/.
2. Compile project files. Don't forget to include path to "jade/lib/jade.jar" in your classpath.
3. Run agents:
In three different consoles, run:
MAC OSX:
java -cp '<path to jade/lib/jade.jar>':'<path to jade/lib/commons-codec/commons-codec-1.3.jar>':'<path to root folder of compiled project>' jade.Boot -agents "Profiler:agents.profiler.ProfilerAgent;Controller:agents.controller.ControllerAgent"
java -cp '<path to jade/lib/jade.jar>':'<path to jade/lib/commons-codec/commons-codec-1.3.jar>':'<path to root folder of compiled project>' jade.Boot -container -agents "Malta:agents.curator.CuratorAgent"
java -cp '<path to jade/lib/jade.jar>':'<path to jade/lib/commons-codec/commons-codec-1.3.jar>':'<path to root folder of compiled project>' jade.Boot -container -agents "Galileo:agents.curator.CuratorAgent"
Windows:
java -cp ".;<path to jade/lib/jade.jar>;<path to jade/lib/commons-codec/commons-codec-1.3.jar>" jade.Boot -agents "Profiler:agents.profiler.ProfilerAgent;Controller:agents.controller.ControllerAgent"
java -cp ".;<path to jade/lib/jade.jar>;<path to jade/lib/commons-codec/commons-codec-1.3.jar>" jade.Boot -container -agents "Malta:agents.curator.CuratorAgent"
java -cp ".;<path to jade/lib/jade.jar>;<path to jade/lib/commons-codec/commons-codec-1.3.jar>" jade.Boot -container -agents "Galileo:agents.curator.CuratorAgent"