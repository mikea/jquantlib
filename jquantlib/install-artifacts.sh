#!/bin/bash

##############################################################
# =========================
# R E A D   M E   F I R S T
# =========================
# This script is intended to manually publish dependencies 
# in our Maven repositories.
#
# I M P O R T A N T
# -----------------
# All the dependencies belonging to groupId=tmp.* are not
# properly known at this time. We are simply inserting the
# artifacts we have into the repository. Ideally, this
# information should be properly filled in or the
# artifact should be replaced by a newer one.
#
###############################################################

url=http://www.jquantlib.org/maven2/repos


list_artifacts() {
cat << EOD
external_free jal jal 20031117 (none) jar                      ${HOME}/artifacts/jal-20031117.jar
external_free gr.spinellis UmlGraph 4.9.0 (none) jar           ${HOME}/artifacts/UmlGraph-4.9.0.jar
external_free net.sf.latextaglet latextaglet 0.1.0 (none) jar  ${HOME}/artifacts/latextaglet-0.1.0.jar
EOD
}

#external_free org.jscience jscience 4.3.1 (none) jar           ${HOME}/artifacts/jscience-4.3.1.jar


validate_arguments() {
  list_artifacts | while read repos group artifact version classifier format file ;do
    if [ ! -f "${file}" ] ; then
      echo File not found: ${file}
      exit 1
    fi
  done
}

list_dependencies() {
  list_artifacts | while read repos group artifact version classifier format file ;do
    echo "    <dependency>"
    echo "      <groupId>${group}</groupId>"
    echo "      <artifactId>${artifact}</artifactId>"
    echo "      <version>${version}</version>"
    echo "    </dependency>"
  done
}

install_artifacts() {
  list_artifacts | while read repos group artifact version classifier format file ;do
    params="-Durl=dav:${url}/${repos} -DrepositoryId=${repos} -DgroupId=${group} -DartifactId=${artifact} -Dversion=${version}"
    if [ ! ${classifier} == "(none)" ] ; then
      params="${params} -Dclassifier=${classifier}"
    fi
    params="${params} -Dpackaging=${format} -Dfile=${file}"
    echo mvn deploy:deploy-file ${params}
    mvn deploy:deploy-file ${params}
  done
}


###
# main
###

validate_arguments
#list_dependencies
install_artifacts
