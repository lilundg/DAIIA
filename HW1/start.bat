@ECHO OFF
java -cp ".;C:\JADE\lib\*;C:\JADE\lid\commons-codec\*" jade.Boot -agents "Profiler:agents.profiler.ProfilerAgent;TourGuide:agents.tourguide.TourGuideAgent;Curator:agents.curator.CuratorAgent"
PAUSE