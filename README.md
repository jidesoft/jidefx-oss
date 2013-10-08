Welcome to the JideFX Common Layer, a collection of various extensions and utilities for to JavaFX platform. The JideFX Common Layer is the equivalent to the JIDE Common Layer in the JIDE components for Swing.

Instead of packaging everything into one large jar, we decided to split it into several modules which each module creates its own jar. For now, under the same umbrella, we have six modules. Each module can be built and used independently. We may introduce more modules to the JideFX Common Layer in the future. 

JideFX Common Layer at https://github.com/jidesoft/jidefx-oss.git<BR>

We use gradle to build this project. All you need to do is:
1. install the gradle
2. modify the JDK_HOME in gradle.properties
3. run "gradle" from the command line
Each jar will be generated under the "dist" folder for binary, javadoc and source code respectively. 

If you need a specific part, you just go to the specific sub-folder, then run "gradle". 

In addition, there is a build folder that contains all the jars as well as the examples, developer guides and javadocs. Here is the structure:
*********************************
   Example Directory Structure
*********************************
<pre>
build
  |--release-x.x.x
       |-- .idea                 ; IntelliJ IDEA project folder
       |-- doc                   ; developer guides of modules in PDF format
       |-- examples              ; examples source code
       |-- javadoc               ; javadoc jars
       |-- lib                   ; required libs for examples
       |-- src	                 ; source code jars
       |-- build.gradle          ; gradle build script for examples, TextFieldsDemo will be executed by default
       |-- jidefx-demo.iml       ; IntelliJ IDEA module file
</pre>   
Since JavaFX is still a work in progress, we built our products on top of JDK 8 and JavaFX 8 early access releases so that we can leverage the latest features from this new technology. So, in order to try out these products, you will have to download the latest EA release from https://jdk8.java.net/download.html. We will keep JideFX in sync with the latest JDK 8 EA so that the latest version will always work with the latest JDK 8 EA. The JDK 8 will be formally released in a couple of months which will give us enough time to complete our beta phase.

We have changed jidefx-oss release number format to follow the release number of JDK8 from b109.

Please search for jidefx in Maven center http://search.maven.org/ if you prefer to get a binary release.
