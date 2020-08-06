#!/usr/bin/env groovy

import gov.ca.cwds.jenkins.semver.LabelChecker

def call(String projectName, List tagPrefixes = [], username = '', accesskey = '') {
  labelChecker = new LabelChecker(this)
  labelChecker.check(projectName, tagPrefixes, username, accesskey)
}
