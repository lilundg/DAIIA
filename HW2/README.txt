To run the agents you need to do the following
1. Download JADE framework from http://jade.tilab.com/.
2. Compile project files. Don't forget to include path to "jade/lib/jade.jar" in your classpath when compiling.
3. Run agents:

To run the simple auction agents for n Buyers, use:
MAC OSX:
java -cp '<path to jade/lib/jade.jar>':'<path to jade/lib/commons-codec/commons-codec-1.3.jar>':'<path to root folder of compiled project>' jade.Boot -agents "<Aucitoneer-name>:agents.curator.CuratorAgent;<Buyer-1>:agents.profiler.ProfilerAgent;...;<Buyer-n>:agents.profiler.ProfilerAgent"
Windows:
java -cp ".;<path to jade/lib/jade.jar>;<path to jade/lib/commons-codec/commons-codec-1.3.jar>" jade.Boot -agents "<Aucitoneer-name>:agents.curator.CuratorAgent;<Buyer-1>:agents.profiler.ProfilerAgent;...;<Buyer-n>:agents.profiler.ProfilerAgent"

To run the defined auction agents for n Buyers, use:
MAC OSX:
java -cp '<path to jade/lib/jade.jar>':'<path to jade/lib/commons-codec/commons-codec-1.3.jar>':'<path to root folder of compiled project>' jade.Boot -agents "<Aucitoneer-name>:agents.redefined.curator.RCuratorAgent;<Buyer-1>:agents.redefined.profiler.RProfilerAgent;...;<Buyer-n>:agents.redefined.profiler.RProfilerAgent"
Windows:
"Run from class root folder."
java -cp ".;<path to jade/lib/jade.jar>;<path to jade/lib/commons-codec/commons-codec-1.3.jar>" jade.Boot -agents "<Aucitoneer-name>:agents.redefined.curator.RCuratorAgent;<Buyer-1>:agents.redefined.profiler.RProfilerAgent;...;<Buyer-n>:agents.redefined.profiler.RProfilerAgent"

Do not forget to replace the text marked with <>, and to add as many buyer agensta as you want