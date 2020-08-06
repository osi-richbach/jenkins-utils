#!/usr/bin/env groovy
import gov.ca.cwds.jenkins.GithubPullRequestBuilderTriggerProperties

def call(jenkinsUrl = 'https://jenkins.dev.cwds.io') {
  trigger = new GithubPullRequestBuilderTriggerProperties(this)
  trigger.triggerProperties(jenkinsUrl)
}
