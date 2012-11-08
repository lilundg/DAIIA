@ECHO OFF
cd bin
java -cp ".;C:\JADE\lib\*;C:\JADE\lid\commons-codec\*" jade.Boot -agents "Buyer1:agents.redefined.profiler.RProfilerAgent;Buyer2:agents.redefined.profiler.RProfilerAgent;Buyer3:agents.redefined.profiler.RProfilerAgent;Buyer4:agents.redefined.profiler.RProfilerAgent;Auctioneer:agents.redefined.curator.RCuratorAgent"
PAUSE