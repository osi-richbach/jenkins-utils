#!/usr/bin/env groovy

import gov.ca.cwds.jenkins.semver.LabelChecker

def call(String projectName, String credentials, List tagPrefixes = []) {
  labelChecker = new LabelChecker(this)
  labelChecker.check(projectName, credentials, tagPrefixes)
}
