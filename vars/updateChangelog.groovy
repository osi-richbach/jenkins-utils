#!/usr/bin/env groovy

import gov.ca.cwds.jenkins.ChangelogUpdater

def call(applicationName, credentialsId) {
  changelogUpdater = new ChangelogUpdater(this)
  changelogUpdater.update(applicationName, credentialsId)
}
