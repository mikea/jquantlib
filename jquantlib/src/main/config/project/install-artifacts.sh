#!/bin/bash

# installs joda-primitives in our repository
mvn deploy:deploy-file -Drole=manager \
		-Durl=sftp://ftp.jquantlib.org/srv/users/www-data/maven2/jquantlib.org/maven2/repos/external_free \
		-DrepositoryId=jquantlib-external_free \
		-DpomFile=lib/joda-primitives-0.6.pom \
		-Dfile=lib/joda-primitives-0.6.jar
