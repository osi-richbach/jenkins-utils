#!/usr/bin/env groovy

import gov.ca.cwds.jenkins.GithubRepoTagger

def call(String tag, String credentials) {
  githubRepoTagger = new GithubRepoTagger(this)
  githubRepoTagger.tagWith(tag, credentials)
}
