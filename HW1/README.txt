To run the agents you need to do the following
1. Download JADE framework from http://jade.tilab.com/.
2. Compile project files. Don't forget to include path to "jade/lib/jade.jar" in your classpath.
3. Run agents:
MAC OSX:
java -cp '<path to jade/lib/jade.jar>':'<path to jade/lib/commons-codec/commons-codec-1.3.jar>':'<path to root folder of compiled project>' jade.Boot -agents "TourAgent:agents.tourguide.TourGuideAgent;Curator:agents.curator.CuratorAgent;Profiler:agents.profiler.ProfilerAgent"
Windows:
java -cp ".;<path to jade/lib/jade.jar>;<path to jade/lib/commons-codec/commons-codec-1.3.jar>" jade.Boot -agents "Profiler:agents.profiler.ProfilerAgent;TourGuide:agents.tourguide.TourGuideAgent;Curator:agents.curator.CuratorAgent"