==About JQuantLib==

JQuantLib is a Quantitative Finance framework written in Java. It aims to
provide enough tools for a broad range of financial instrument valuation
using a number of algorithms, pricing engines, simulation methods, etc.

If  you are impatient, jump to ''Building JQuantLib from command line''
 

==Audience== 

The main audience is composed by Java developers interested on matters
related to Quantitative Finance and specially interested on financial 
instrument valuation. You also need to be comfortable to use Linux.
In particular, there are some examples which are suitable to Debian
and variants.


==What this README is about?==

This README file is about how JQuantLib can be obtained and built both
using command line commands in a Linux box or using the excellent
[http://www.eclipse.org Eclipse] IDE. You can jump directly to the
section which best matches your needs.


==Building JQuantLib from command line==

If you prefer to build JQuantLib under [http://www.eclipse.org/ Eclipse],
please jump to ''Building JQuantLib under Eclipse''.


===Overview===

We assume you already have Java6 installed into your computer. We will guide
you on how to install ''subversion'' and [http://maven.apache.org Maven]
so that you will be able to obtain the sources from our SVN repository and
''automagically'' build JQuantLib with a couple of commands.


===Install Subversion===

Install Subversion (SVN) into your system. You can obtain further instructions
at http://subversion.tigris.org/project_packages.html. For the impatient,
simply type the following command under Debian and variants:

 apt-get install subversion


===Install Maven===

JQuantLib depends on [http://maven.apache.org Maven2] for its build process.
In spite Maven is available as a Debian package, we prefer to install
it by hand from the official Apache download site because it's a better 
controlled method which does not imply on installation of the entire Java 
environment chosen by Debian packages. Doing so, you control which Java 
environment you are using and in the future, you will be able to switch to 
Java7 in order to take advantage of some advanced compilation options it 
offers.

* Download Maven2 from http://maven.apache.org/download.html;
* Uncompress Maven2 distribution archive in a place of your choice;
* Add Maven2 ''bin'' directory in your PATH variable.

For instance:

 # download Maven2
 mkdir -p ~/Downloads/Java/apache-maven/2.0.9
 cd ~/Downloads/Java/apache-maven/2.0.9
 wget http://www.mirrorservice.org/sites/ftp.apache.org/maven/binaries/apache-maven-2.0.9-bin.tar.bz2
 wget http://www.mirrorservice.org/sites/ftp.apache.org/maven/binaries/maven-ant-tasks-2.0.9.jar
 
 # Install Maven2
 mkdir -p /opt/JavaIDE
 cd /opt/JavaIDE
 tar xvpjf ~/Downloads/Java/apache-maven/2.0.9/apache-maven-2.0.9-bin.tar.bz2
 cp ~/Downloads/Java/apache-maven/2.0.9/maven-ant-tasks-2.0.9.jar apache-maven-2.0.9/lib
 
 # add to your PATH
 export PATH=$PATH:/opt/JavaIDE/apache-maven-2.0.9/bin
 echo 'export PATH=$PATH:/opt/JavaIDE/apache-maven-2.0.9/bin' >> ~/.bashrc

Now verify if your environment is properly configured. If you type

 mvn -version
 
You will obtain something similar to this:

  Maven version: 2.0.9
  Java version: 1.6.0_03
  OS name: "linux" version: "2.6.24-1-686" arch: "i386" Family: "unix"


===Compiling JQuantLib===

Now the magic begins.

With a couple of commands you can build everything, thanks to
the standard build environment offered by Maven:

 mkdir -p ~/workspace
 cd ~/workspace
 # obtain source code
 svn co http://jquant.svn.sourceforge.net/svnroot/jquant/trunk/jquantlib
 # build JQuantLib
 cd jquantlib
 mvn clean package
 
The first time you execute the ''mvn'' command above, Maven2 will download
all dependencies for you. This process will take some time. You can take a
break and have a coffee. When you return, you will find JQuantLib's .jar
file under your ''target'' directory.

 ls -al target

Now you can run JQuantLib Unit tests by typing

  mvn test


===Obtaining documentation===

JQuantLib provides JavaDocs which contains UML diagrams and mathematical
equations. It's done by [http://www.umlgraph.org UMLGraph] and 
[http://www.jquantlib.org/index.php/LaTeXtaglet LaTeXtaglet]. These tools
require a number of other tools that you can install easily in Debian and
variants simply by typing:

 apt-get install graphwiz gnuplot texlive-latex-extra texlive-latex-recommended

Maven is able to generate full documentation, including JavaDocs, results
of automated tests, quality reports, etc simply by typing:

 mvn site

Now browse the generated documentation;

 firefox site/index.html


==Building JQuantLib under Eclipse==

JQuantLib is developed in Java under Eclipse IDE and depends on Maven2 for 
its build process. We also present here all plug-ins we recommend to be
installed in your Eclipse IDE.

===Requirements===
We strongly recommend Sun Java 1.6.0_03 or above

* Install JavaSE version 1.6.0_03 or above in your environment. 
  http://java.sun.com/javase/downloads/index.jsp
       
* Install "Eclipse IDE v3.3.1.1 for Java developers".
  http://www.eclipse.org/download

===Install plugins===
We assume you know how plug-ins can be installed in Eclipse via update sites.
Please install the following plug-ins:


'''Essential plug-ins
       
* Subversive is a plugin which integrates Eclipse and Subversion. We've chosen
Subversive instead of [http://subclipse.tigris.org/ Subclipse] because the former
looks more mature and keeps history after refactoring whilst the latter doesn't.
 
 name: Subversive
 URL: http://download.eclipse.org/technology/subversive/0.7/update-site/

 name: Subversive SVN Connectors
 URL: http://www.polarion.org/projects/subversive/download/eclipse/2.0/update-site/

 name: Subversive Integrations
 URL: http://www.polarion.org/projects/subversive/download/integrations/update-site/

* [http://m2eclipse.codehaus.org M2Eclipse] integrates dependency management 
provided by Maven into your Eclipse IDE, among other features.

 name: m2eclipse
 URL: http://m2eclipse.codehaus.org/update/

Tips
** Select m2eclipse and ask Eclipse to "Select Required"
** Select Mylyn and Mylyn extras
** Unselect broken references to BuckMinster products, if any.
 
* [http://findbugs.cs.umd.edu FindBugs] analyses your code and point out
places where you probably have a bug or a bad practice listed in FindBugs
knowledge base.
       
 name: FindBugs
 URL: http://findbugs.cs.umd.edu/eclipse/

* [ http://www.eclemma.org EclEmma] allows you to easily verify the code coverage
of your JUnit tests.

 name: EclEmma
 URL: http://update.eclemma.org/


'''Optional plug-ins

Evaluate yourself if these plug-ins match your needs. In particular, compare
against functionalities offered by reports generated by Maven.

* [http://metrics.sourceforge.net Metrics] offers some features offered by
FindBugs and is also able to analyse complexity of your code. Maven offers
[http://www.pmd.org PMD] reports which are probably more convenient and
complete.

 name: Metrics
 URL: http://metrics.sourceforge.net/update


'''For the future

The following plug-ins are under evaluation and maybe adopted in the future.

* [http://springide.org/ Spring IDE] integrates some features of the excellent
[http://www.springframework.org Spring Framework] into Eclipse IDE. Spring is
composed of several components and probably a small set of features are
of interest of JQuantLib, in particular: dependency injection and integration
with [http://www.osgi.org OSGi] containers.

 name: SpringIDE
 URL: http://springide.org/updatesite
 

===Obtaining JQuantLib===

Open SVN Repository Exploring perspective and select the following location:

 http://jquant.svn.sourceforge.net/svnroot/jquant
 
Now go to ''trunk/jquantlib'' and check it out to a default project name
''jquantlib''.


===Compiling JQuantLib===

When you open Java perspective and open your ''jquantlib'' project, m2eclipse
plugin will perform a magic for you:

* analyse your pom.xml file;
* download all dependent ''artifacts'' JQuantLib and Maven depend on;
* compile JQuantLib

The first time you open ''jquantlib'' project, it may take a relatively long
time to compile due to the amount and file sizes of dependencies involved.
You can take a break and have a coffee.


===Running Unit tests===
Now let's run JQuantLib unit tests and at the same time obtain a test
coverage report.

* Right click on project ''jquantlib'';
* Select ''Coverage As >''
* Select ''JUnit Test''

EclEmma plugin will execute all test cases. You can see the output in
''Console'' window and coverage results in ''Coverage'' window. 


==Installing JQuantLib==

JQuantLib is a software library, intended to be included by developers in 
applications developed by them. Developers are responsible for chosing what
they think is the best what to distribute and install JQuantLib binaries.


==Additional documentation==

The main source of information is our wiki at http://wiki.jquantlib.org/

