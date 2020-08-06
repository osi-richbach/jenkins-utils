#!/usr/bin/env groovy

import gov.ca.cwds.jenkins.semver.VersionIncrement

def call(labels) {
    new VersionIncrement().increment(labels)
}
