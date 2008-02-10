This README is always under construction.


Once JQuantLib is written in Java and depends on open source tools, most of
them also written in Java, these instructions can be applied to any operating
system.

However, the examples of commands presented here are *ALWAYS* intended to be
used in Linux shell. Most of the examples will work fine to all Linux users.
Some examples are specific to Debian and derivatives, like Ubuntu and Kubuntu.



1. Obtaining JQuantLib

    Requirements:
    -------------
    1. Install Subversion (SVN) into your system. You can obtain instructions
       at http://subversion.tigris.org/project_packages.html.
       Under Debian and derivatives it's as simple as

            apt-get install subversion
            
    2. Check out JQuantLib from SourceForge by typing
    
        cd
        mkdir workspace
        svn ckeckout http://jquant.svn.sourceforge.net/svnroot/jquant

    Note: I suppose that you would like to use Eclipse for development. That's
          why we are checking out under the directory "workspace".



2. Compiling JQuantLib under Eclipse

If you are impatient or you don't need Eclipse, jump to 
"Compiling JQuantLib under command prompt"

JQuantLib was developed in Java, under Eclipse IDE and depends on Maven2 for 
its build process.

    Requirements:
    -------------
    1. Install JavaSE version 1.6.0_03 or above in your environment. 
       http://java.sun.com/javase/downloads/index.jsp
       
    2. Install "Eclipse IDE v3.3.1.1 for Java developers".
       http://www.eclipse.org/download
       
    3. Install Subversion plugin in Eclipse. Simply add the update site: 
       http://downloads.open.collab.net/eclipse/merge-client/e3.3/site.xml
       
    4. Install Maven2 plugin in Eclipse. Simply add the update site:
       http://m2eclipse.codehaus.org/update/
       
    Installation tips (on the update manager)
    * Select m2eclipse and ask Eclipse to "Select Required"
    * Select Mylyn and Mylyn extras
    * Unselect broken references to BuckMinster products, if any.
          
       
Open JQuantLib project under Eclipse. Once you have the Maven2 eclipse plugin
properly installed it will automagically download all the requred dependencies
for you. Be patient, Maven2 will download all necessary dependencies for its 
own use and for the use of JQuantLib.

If you need to generate a .JAR file for JQuantLib, please follow the steps
described below at "Compiling JQuantLib under command prompt".



3. Compiling JQuantLib under command prompt

JQuantLib depends on Maven2 for its build process.

    Requirements:
    -------------
    1. Download and uncompress Maven2 distribution and put its "bin" directory
       in your PATH variable. http://maven.apache.org/download.html

You can compile JQuantLib and generate a .JAR file simply typing the command
below, suposing JQuantLib was checked out into ~/workspace/jquantlib :

    cd ~/workspace/jquantlib
    mvn clean compile package -Ddebug

The define "debug" tells Maven2 to ignore possible differences between your
local working copy and the SVN repository. This is, in general, what you
need to do when you are developing.



4. Installing JQuantLib

JQuantLib is a software library, intended to be included by developers in 
applications.



5. Additional documentation

The main source of information is our wiki at http://wiki.jquantlib.org/

====================================================
The contents below must be moved to the Wiki
====================================================


===Sanity check on floating point numbers===

In order to correctly test floating numbers for equality, JQuantLib has the following classes which adds functionality to JDK's counterparts:
* interface org.jquantlib.lang.Comparable
* class org.jquantlib.lang.Number
* class org.jquantlib.util.Date

For more information on this subject, please see:
* http://www.concentric.net/~Ttwang/tech/javafloat.htm
* http://www.ibm.com/developerworks/java/library/j-jtp0114/index.html
* http://www.physics.ohio-state.edu/~dws/grouplinks/floating_point_math.pdf
* http://www.cs.berkeley.edu/~wkahan/JAVAhurt.pdf
* http://www.cs.princeton.edu/introcs/91float/index.html

In order to perform this sanity check, execute the following commands.
You should not see any occurrence of any of these import lines listed below, i.e: the source code should never implicitly use these classes.
All uses of these classes are being done explicitly by the wrapper classes.
<pre>
cd jquantlib/src
find . -name '*.java' -exec grep -H -i 'import java.lang.Number' {} \;
find . -name '*.java' -exec grep -H -i 'import java.lang.Double' {} \;
find . -name '*.java' -exec grep -H -i 'import java.util.Date' {} \;
</pre>

===Sanity check on the Observable class===

Java conveniently defines the Observable class.
Unfortunately, for the purposes and complexity of the object model in use by JQuantLib, we'd better have Observable as an interface, instead of a class.

In order to perform this sanity check, execute the following commands.
You should not see any occurrence of any of these import lines listed below, i.e: the source code should never implicitly use these classes.
All uses of these classes are being done explicitly by the wrapper classes.
<pre>
cd jquantlib/src
find . -name '*.java' -exec grep -H -i 'import java.util.Observable' {} \;
</pre>
