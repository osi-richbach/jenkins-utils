#!/usr/bin/env groovy

import gov.ca.cwds.jenkins.semver.PullRequestEvent

def call() {
    pullRequestEvent = new PullRequestEvent(this)
    pullRequestEvent.getPRLabels()
}
