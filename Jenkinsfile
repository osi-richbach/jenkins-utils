import groovy.transform.Field

@Library('jenkins-pipeline-utils') _

switch(env.BUILD_JOB_TYPE) {
  case "master": buildMaster(); break;
  default: buildPullRequest();
}

@Field
def newTag

@Field
def GITHUB_CREDENTIALS_ID = '433ac100-b3c2-4519-b4d6-207c029a103b'

def buildPullRequest() {
  node('linux') {
    def triggerProperties = githubPullRequestBuilderTriggerProperties()
    properties([
      githubConfig(),
      pipelineTriggers([triggerProperties])
    ])
    try {
      checkoutStage()
      docker.image('groovy:alpine').inside {
        lintingStage()
        unitTestStage()
      }
      checkForLabelPullRequest()
      checkForConventionalCommitsInPr()
    } catch(Exception exception) {
      currentBuild.result = "FAILURE"
      throw exception
    } finally {
      cleanWs()
    }
  }
}

def buildMaster() {
  node('linux') {
    triggerProperties = pullRequestMergedTriggerProperties('jenkins-pipeline-utils-master')
    properties([
      githubConfig(),
      pipelineTriggers([triggerProperties])
    ])
    try {
      checkoutStage()
      docker.image('groovy:alpine').inside {
        lintingStage()
        unitTestStage()
      }
      incrementTagStage()
      tagRepoStage()
      updateChangelogStage()
      updateReleaseNotesStage()
    } catch(Exception exception) {
      currentBuild.result = "FAILURE"
      throw exception
    } finally {
      cleanWs()
    }
  }
}

def checkoutStage() {
  stage('Checkout') {
    deleteDir()
    checkout scm
  }
}

def updateChangelogStage() {
  stage('Update Changelog') {
    updateChangelog('jenkins-pipeline-utils', GITHUB_CREDENTIALS_ID)
  }
}

def updateReleaseNotesStage() {
  stage('Update Release Notes') {
    updateReleaseNotes('jenkins-pipeline-utils', GITHUB_CREDENTIALS_ID)
  }
}

def lintingStage() {
  stage('Linting') {
    sh "./gradlew check"
  }
}

def unitTestStage() {
  stage('Unit Tests') {
    sh "./gradlew test"
  }
}

def checkForLabelPullRequest() {
  stage('Verify SemVer Label') {
    checkForLabel("jenkins-pipeline-utils")
  }
}

def checkForConventionalCommitsInPr() {
  stage('Verify Commits') {
    checkForConventionalCommits('jenkins-pipeline-utils')
  }
}

def incrementTagStage() {
  stage("Increment Tag") {
    newTag = newSemVer()
  }
}

def tagRepoStage() {
  stage('Tag Repo') {
    tagGithubRepo(newTag, GITHUB_CREDENTIALS_ID)
  }
}

def githubConfig() {
  githubConfigProperties('https://github.com/ca-cwds/jenkins-pipeline-utils')
}
