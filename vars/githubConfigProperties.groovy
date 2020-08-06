#!/usr/bin/env groovy
import gov.ca.cwds.jenkins.GithubConfigProperties

def call(String githubUrl) {
  def githubConfigProperties = new GithubConfigProperties()
  githubConfigProperties.configure(githubUrl)
}
