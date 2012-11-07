@ECHO OFF
cd bin
java -cp ".;C:\JADE\lib\*;C:\JADE\lid\commons-codec\*" jade.Boot -agents "Buyer1:agents.profiler.ProfilerAgent;Buyer2:agents.profiler.ProfilerAgent;Buyer3:agents.profiler.ProfilerAgent;Buyer4:agents.profiler.ProfilerAgent;Auctioneer:agents.curator.RedefinedCuratorAgent"
PAUSE