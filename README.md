# Android-CFG

This project is to extract Control Flow Graphs (CFG) from the Andoird application apk.

This project is written in Java.

Eclipse is the recommended platform to run this project.

Before using the code of this project, you need to setup several small things first~

1. Two projects are needed first and accessible in https://github.com/secure-software-engineering/soot-infoflow-android and https://github.com/secure-software-engineering/soot-infoflow

2. Download those two projects and add them to this project by using the "Build Path" option when you right-click this project.

3. The soot packages are required to add to the "Build Path", which are able to be found in the "required soot packages" folder in GitHub. 

4. The Android source library can be found in "required Android source packages" folder. 

5. In our case, cocos2d_android.apk is the target app we are analyzing. It is a game engine.  

6. All the directories or paths used in the code of this project should be modified according to your contexts.  
