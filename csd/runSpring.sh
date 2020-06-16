#!/bin/bash
sudo apt install maven
mvn install:install-file -Dfile='./lib/core-0.1.4.jar' -DgroupId='com.yahoo.ycsb' -DartifactId='core' -Dversion='0.1.4' -Dpackaging='jar'
mvn spring-boot:run