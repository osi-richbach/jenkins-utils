#!/usr/bin/env groovy

import gov.ca.cwds.jenkins.ManifestUpdater

def call(applicationName, manifestName, credentialsId, version) {
  manifestUpdater = new ManifestUpdater(this)
  manifestUpdater.update(applicationName, manifestName, credentialsId, version)
}
