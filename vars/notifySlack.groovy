#!/usr/bin/env groovy
import gov.ca.cwds.jenkins.SlackNotifier

def call(webhookUrl, projectName, error) {
  slackNotifer = new SlackNotifier(this, webhookUrl)
  slackNotifer.notifySlack(projectName, error)
}
