#!/usr/bin/env groovy

import gov.ca.cwds.jenkins.ReleaseNotesUpdater

def call(applicationName, credentialsId) {
  def releaseNotesUpdater = new ReleaseNotesUpdater(this)
  releaseNotesUpdater.update(applicationName, credentialsId)
}
