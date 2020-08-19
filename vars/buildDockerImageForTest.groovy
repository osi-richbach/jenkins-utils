#!/usr/bin/env groovy
import gov.ca.cwds.jenkins.docker.Docker
import gov.ca.cwds.jenkins.common.BuildMetadata

def call(pathToDockerfile) {
  def docker = new Docker(this)
  def buildMetadata = new BuildMetadata(this, this.env.JOB_NAME, this.env.BUILD_ID, this.env.WORKSPACE, this.env.GITHUB_TOKEN)
  docker.createTestingImage(pathToDockerfile, buildMetadata)
}
