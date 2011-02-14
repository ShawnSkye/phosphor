In firefox, file->open and find phosphor.xpi

You need java enabled, as it is an applet. Questions/concerns/help, get me on jabber or matthew.jaffee@gmail.com

I've been developing this in Xubuntu 10.04, so it will be interesting to see how it behaves on windows/mac.

Included is a folder called sampleExnodes... you can direct phosphor to look at this folder, and see how it handles exnodes and folders.



Some Notes (Linux):
You may need to edit /etc/java-6-sun/security/java.policy I added the following at the beginning of the file:
        grant {
                permission java.security.AllPermission
        };
        This is definitely a bad idea, and I'm sure there is a way to grant the appropriate permission without granting all permissions.
        Make sure you are using sun java jre http://www.ubuntugeek.com/how-to-install-java-runtime-environment-jre-in-ubuntu-904-jaunty.html


!!      To enable java console: Run $JAVA_HOME/bin/ControlPanel


