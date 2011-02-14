#!/bin/bash


javac phosphor.java
javac FTPListing.java
cd ..
cd org
cd exnode
javac exNode.java
javac exNodeDir.java
cd ..
cd ..
jar cf phosphor.jar phosphor/phosphor.class /phosphor/FTPListing.class org/
cp phosphor.jar ./phosphor
cd phosphor
jarsigner -keystore phosphorStore -storepass phosphorStorePass -keypass phosphorKeyPass -signedjar phosphorSigned.jar phosphor.jar phosphorKeys
mv phosphorSigned.jar ../../chrome/content/
rm phosphor.jar
cd ../..
zip -r phosphor.xpi *
