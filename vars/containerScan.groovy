#!/usr/bin/env groovy
import gov.ca.cwds.jenkins.ContainerScanner

def call(String containerName, String containerVersion) {
  containerScanner = new ContainerScanner(this)
  containerScanner.sendNotification(containerName, containerVersion);
}
